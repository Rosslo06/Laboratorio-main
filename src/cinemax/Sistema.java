package cinemax;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe principale che gestisce la logica dell'applicazione CineMax.
 * 
 * @author Andrea
 * @version 1.1
 */
public class Sistema {
    private List<Proiezione> proiezioni;
    private List<Utente> utenti;
    private List<Prenotazione> prenotazioni;
    private Utente utenteCorrente;

    /**
     * Costruttore che inizializza le liste e carica i dati dal sistema di persistenza.
     */
    public Sistema() {
        this.proiezioni = new ArrayList<>();
        this.utenti = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
        this.utenteCorrente = null;
        caricaDati();
    }

    /**
     * Carica utenti, proiezioni e prenotazioni richiamando il GestoreDati.
     */
    private void caricaDati() {
        this.utenti = GestoreDati.caricaUtenti();
        this.proiezioni = GestoreDati.caricaProiezioni();
        this.prenotazioni = GestoreDati.caricaPrenotazioni(utenti, proiezioni);
    }

    /**
     * Effettua il login di un utente controllando credenziali e hash della password.
     * 
     * @param username l'username dell'utente
     * @param password la password in chiaro
     * @return true se il login ha successo, false altrimenti
     */
    public boolean login(String username, String password) {
        for (Utente u : utenti) {
            if (u.getUsername().equals(username) && u.verificaPassword(password)) {
                this.utenteCorrente = u;
                return true;
            }
        }
        return false;
    }

    /**
     * Registra un nuovo cliente nel sistema verificando l'univocità dell'username.
     * 
     * @param nome il nome del cliente
     * @param cognome il cognome del cliente
     * @param username l'username del cliente
     * @param password la password del cliente
     * @param dataNascita la data di nascita del cliente
     * @param luogoDomicilio il luogo di domicilio del cliente
     * @return true se la registrazione va a buon fine, false se l'username esiste già
     */
    public boolean registraCliente(String nome, String cognome, String username, String password,
            LocalDate dataNascita, String luogoDomicilio) {
        for (Utente u : utenti) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        Cliente cliente = new Cliente(nome, cognome, username, password, dataNascita, luogoDomicilio);
        utenti.add(cliente);
        GestoreDati.salvaUtenti(utenti);
        return true;
    }

    /**
     * Effettua il logout azzerando l'utente corrente.
     */
    public void logout() {
        this.utenteCorrente = null;
    }

    /**
     * Cerca proiezioni filtrando per molteplici criteri opzionali.
     * 
     * @param titolo il titolo del film (può essere null o vuoto)
     * @param genere il genere del film (può essere null o vuoto)
     * @param dataDa la data/ora di inizio minima (può essere null)
     * @param dataA la data/ora di fine massima (può essere null)
     * @param costoMin il costo minimo del biglietto
     * @param costoMax il costo massimo del biglietto
     * @return una lista di proiezioni che soddisfano i filtri
     */
    public List<Proiezione> cercaProiezioni(String titolo, String genere,
            LocalDateTime dataDa, LocalDateTime dataA,
            double costoMin, double costoMax) {
        List<Proiezione> risultati = new ArrayList<>();

        for (Proiezione p : proiezioni) {
            if (titolo != null && !titolo.isEmpty()) {
                if (!p.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                    continue;
                }
            }

            if (genere != null && !genere.isEmpty()) {
                if (!p.getFilm().getGenere().equalsIgnoreCase(genere)) {
                    continue;
                }
            }

            if (dataDa != null) {
                if (p.getDataOraProiezione().isBefore(dataDa)) {
                    continue;
                }
            }

            if (dataA != null) {
                if (p.getDataOraProiezione().isAfter(dataA)) {
                    continue;
                }
            }

            if (costoMin >= 0) {
                if (p.getCostoBiglietto() < costoMin) {
                    continue;
                }
            }

            if (costoMax >= 0) {
                if (p.getCostoBiglietto() > costoMax) {
                    continue;
                }
            }

            risultati.add(p);
        }

        return risultati;
    }

    /**
     * Aggiunge una nuova proiezione al palinsesto controllando conflitti di orario.
     * 
     * @param film il film da proiettare
     * @param dataOra la data e ora della proiezione
     * @param costoBiglietto il costo del biglietto
     * @return true se aggiunta con successo, false in caso di conflitto o errore
     */
    public boolean aggiungiProiezione(Film film, LocalDateTime dataOra, double costoBiglietto) {
        for (Proiezione p : proiezioni) {
            if (p.getDataOraProiezione().equals(dataOra)) {
                return false;
            }
        }

        int maxId = 0;
        for (Proiezione p : proiezioni) {
            if (p.getId() > maxId) {
                maxId = p.getId();
            }
        }
        int nuovoId = maxId + 1;

        Proiezione nuovaProiezione = new Proiezione(nuovoId, film, dataOra, costoBiglietto);
        proiezioni.add(nuovaProiezione);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }

    /**
     * Modifica la data e l'ora di una proiezione esistente se non possiede prenotazioni attive.
     * 
     * @ l'ID della proiezione da modificare
     * @param nuovaDataOra la nuova data e ora
     * @return true se modificata con successo, false altrimenti
     */
    public boolean modificaProiezione(int idProiezione, LocalDateTime nuovaDataOra) {
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return false;
        }

        int conteggioPrenotazioni = 0;
        for (Prenotazione p : prenotazioni) {
            if (p.getProiezione().getId() == idProiezione) {
                conteggioPrenotazioni++;
            }
        }

        if (conteggioPrenotazioni > 0) {
            return false;
        }

        proiezione.setDataOraProiezione(nuovaDataOra);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }

    /**
     * Elimina una proiezione dal sistema se non ha prenotazioni collegate.
     * 
     * @param idProiezione l'ID della proiezione da eliminare
     * @return true se eliminata con successo, false altrimenti
     */
    public boolean eliminaProiezione(int idProiezione) {
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return false;
        }

        int conteggioPrenotazioni = 0;
        for (Prenotazione p : prenotazioni) {
            if (p.getProiezione().getId() == idProiezione) {
                conteggioPrenotazioni++;
            }
        }

        if (conteggioPrenotazioni > 0) {
            return false;
        }

        proiezioni.remove(proiezione);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }

    /**
     * Crea una prenotazione per l'utente cliente attualmente loggato.
     * 
     * @param idProiezione l'ID della proiezione da prenotare
     * @param numeroBiglietti il numero di biglietti desiderati
     * @return l'oggetto Prenotazione creato, oppure null se fallisce
     */
    public Prenotazione creaPrenotazione(int idProiezione, int numeroBiglietti) {
        if (!(utenteCorrente instanceof Cliente)) {
            return null;
        }

        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return null;
        }

        if (LocalDateTime.now().isAfter(proiezione.getDataOraProiezione())) {
            return null;
        }

        int postiOccupati = calcolaPostiOccupati(idProiezione);
        int postiDisponibili = proiezione.getCapacitaSala() - postiOccupati;

        if (numeroBiglietti > postiDisponibili) {
            return null;
        }

        Prenotazione prenotazione = new Prenotazione((Cliente) utenteCorrente, proiezione, numeroBiglietti);
        prenotazioni.add(prenotazione);
        GestoreDati.salvaPrenotazioni(prenotazioni);
        return prenotazione;
    }

    /**
     * Modifica una prenotazione esistente cambiandone la proiezione associata.
     * 
     * @param codicePrenotazione il codice univoco della prenotazione
     * @param nuovaIdProiezione l'ID della nuova proiezione
     * @return true se modificata con successo, false altrimenti
     */
    public boolean modificaPrenotazione(String codicePrenotazione, int nuovaIdProiezione) {
        Prenotazione prenotazione = trovaPrenotazione(codicePrenotazione);
        if (prenotazione == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(prenotazione.getProiezione().getDataOraProiezione())) {
            return false;
        }

        Proiezione nuovaProiezione = trovaProiezione(nuovaIdProiezione);
        if (nuovaProiezione == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(nuovaProiezione.getDataOraProiezione())) {
            return false;
        }

        int postiOccupati = calcolaPostiOccupati(nuovaIdProiezione);
        int postiDisponibili = nuovaProiezione.getCapacitaSala() - postiOccupati;

        if (prenotazione.getNumeroBiglietti() > postiDisponibili) {
            return false;
        }

        prenotazione.setProiezione(nuovaProiezione);
        GestoreDati.salvaPrenotazioni(prenotazioni);
        return true;
    }

    /**
     * Elimina una prenotazione esistente se la proiezione non è ancora avvenuta.
     * 
     * @param codicePrenotazione il codice della prenotazione da eliminare
     * @return true se eliminata con successo, false altrimenti
     */
    public boolean eliminaPrenotazione(String codicePrenotazione) {
        Prenotazione prenotazione = trovaPrenotazione(codicePrenotazione);
        if (prenotazione == null) {
            return false;
        }

        if (LocalDateTime.now().isBefore(prenotazione.getProiezione().getDataOraProiezione())) {
            prenotazioni.remove(prenotazione);
            GestoreDati.salvaPrenotazioni(prenotazioni);
            return true;
        }

        return false;
    }

    /**
     * Calcola il numero totale di posti già occupati per una specifica proiezione.
     * 
     * @param idProiezione l'ID della proiezione
     * @return il numero di posti occupati
     */
    public int calcolaPostiOccupati(int idProiezione) {
        int totale = 0;
        for (Prenotazione p : prenotazioni) {
            if (p.getProiezione().getId() == idProiezione) {
                totale = totale + p.getNumeroBiglietti();
            }
        }
        return totale;
    }

    /**
     * Cerca e restituisce una proiezione in base al suo ID.
     * 
     * @param id l'ID della proiezione
     * @return l'oggetto Proiezione trovato, oppure null se inesistente
     */
    public Proiezione trovaProiezione(int id) {
        for (Proiezione p : proiezioni) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Cerca e restituisce una prenotazione in base al suo codice alfanumerico.
     * 
     * @param codice il codice della prenotazione
     * @return l'oggetto Prenotazione trovato, oppure null se inesistente
     */
    public Prenotazione trovaPrenotazione(String codice) {
        for (Prenotazione p : prenotazioni) {
            if (p.getCodicePrenotazione().equals(codice)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Restituisce la lista delle prenotazioni effettuate dal cliente attualmente loggato.
     * 
     * @return la lista delle prenotazioni del cliente
     */
    public List<Prenotazione> getPrenotazioniCliente() {
        List<Prenotazione> risultati = new ArrayList<>();
        if (!(utenteCorrente instanceof Cliente)) {
            return risultati;
        }

        for (Prenotazione p : prenotazioni) {
            if (p.getCliente().getUsername().equals(utenteCorrente.getUsername())) {
                risultati.add(p);
            }
        }
        return risultati;
    }

    /**
     * Restituisce la lista di tutte le prenotazioni programmate per la giornata odierna.
     * 
     * @return la lista delle prenotazioni di oggi
     */
    public List<Prenotazione> getPrenotazioniOggi() {
        List<Prenotazione> risultati = new ArrayList<>();
        LocalDate oggi = LocalDate.now();

        for (Prenotazione p : prenotazioni) {
            LocalDate dataPrenotazione = p.getProiezione().getDataOraProiezione().toLocalDate();
            if (dataPrenotazione.equals(oggi)) {
                risultati.add(p);
            }
        }
        return risultati;
    }

    /**
     * Cerca prenotazioni in base a molteplici criteri (usato dai bigliettai).
     * 
     * @param codice il codice della prenotazione (opzionale)
     * @param nomeCliente il nome del cliente (opzionale)
     * @param cognomeCliente il cognome del cliente (opzionale)
     * @param titoloFilm il titolo del film (opzionale)
     * @param dataDa la data di inizio minima (opzionale)
     * @param dataA la data di fine massima (opzionale)
     * @return la lista delle prenotazioni che soddisfano i criteri
     */
    public List<Prenotazione> cercaPrenotazioni(String codice, String nomeCliente,
            String cognomeCliente, String titoloFilm,
            LocalDateTime dataDa, LocalDateTime dataA) {
        List<Prenotazione> risultati = new ArrayList<>();

        for (Prenotazione p : prenotazioni) {
            if (codice != null && !codice.isEmpty()) {
                if (!p.getCodicePrenotazione().contains(codice)) {
                    continue;
                }
            }

            if (nomeCliente != null && !nomeCliente.isEmpty()) {
                if (!p.getCliente().getNome().toLowerCase().contains(nomeCliente.toLowerCase())) {
                    continue;
                }
            }

            if (cognomeCliente != null && !cognomeCliente.isEmpty()) {
                if (!p.getCliente().getCognome().toLowerCase().contains(cognomeCliente.toLowerCase())) {
                    continue;
                }
            }

            if (titoloFilm != null && !titoloFilm.isEmpty()) {
                if (!p.getProiezione().getFilm().getTitolo().toLowerCase().contains(titoloFilm.toLowerCase())) {
                    continue;
                }
            }

            if (dataDa != null) {
                if (p.getProiezione().getDataOraProiezione().isBefore(dataDa)) {
                    continue;
                }
            }

            if (dataA != null) {
                if (p.getProiezione().getDataOraProiezione().isAfter(dataA)) {
                    continue;
                }
            }

            risultati.add(p);
        }

        return risultati;
    }

    /**
     * Restituisce l'utente attualmente autenticato nel sistema.
     * 
     * @return l'utente corrente, oppure null se nessuno è loggato
     */
    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    /**
     * Restituisce la lista completa delle proiezioni presenti nel sistema.
     * 
     * @return la lista delle proiezioni
     */
    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }

    /**
     * Restituisce la lista completa delle prenotazioni registrate nel sistema.
     * 
     * @return la lista delle prenotazioni
     */
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }
}