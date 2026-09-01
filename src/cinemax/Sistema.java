package cinemax;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe principale che gestisce la logica dell'applicazione CineMax
 * Gestisce: login, proiezioni, prenotazioni, ricerche
 * 
 * @author Andrea
 * @version 1.0
 */
public class Sistema {
    private List<Proiezione> proiezioni;
    private List<Utente> utenti;
    private List<Prenotazione> prenotazioni;
    private Utente utenteCorrente;

    /**
     * Costruttore che inizializza il sistema
     * 
     */
    public Sistema() {
        this.proiezioni = new ArrayList<>();
        this.utenti = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
        this.utenteCorrente = null;
        caricaDati();
    }

    /**
     * Carica tutti i dati dal file
     */
    private void caricaDati() {
        this.utenti = GestoreDati.caricaUtenti();
        this.proiezioni = GestoreDati.caricaProiezioni();
        this.prenotazioni = GestoreDati.caricaPrenotazioni(utenti, proiezioni);
    }

    // ============ METODI DI LOGIN ============

    /**
     * Effettua il login di un utente
     * 
     * @param username l'username dell'utente
     * @param password la password dell'utente
     * @return true se il login è avvenuto con successo, false altrimenti
     * 
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
     * Registra un nuovo cliente
     * 
     * @param nome           il nome del cliente
     * @param cognome        il cognome del cliente
     * @param username       l'username del cliente
     * @param password       la password del cliente
     * @param dataNascita    la data di nascita del cliente
     * @param luogoDomicilio il luogo di domicilio del cliente
     * @return true se la registrazione è avvenuta con successo, false altr
     * 
     */
    public boolean registraCliente(String nome, String cognome, String username, String password,
            LocalDate dataNascita, String luogoDomicilio) {
        // Controlla se l'username esiste già
        for (Utente u : utenti) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        // Crea un nuovo cliente e lo aggiunge
        Cliente cliente = new Cliente(nome, cognome, username, password, dataNascita, luogoDomicilio);
        utenti.add(cliente);
        GestoreDati.salvaUtenti(utenti);
        return true;
    }

    /**
     * Effettua il logout
     */
    public void logout() {
        this.utenteCorrente = null;
    }

    // ============ METODI DI RICERCA PROIEZIONI ============

    /**
     * Cerca proiezioni in base ai criteri specificati
     * 
     * @param titolo   il titolo del film (può essere null o vuoto)
     * @param genere   il genere del film (può essere null o vuoto)
     * @param dataDa   la data di inizio (può essere null)
     * @param dataA    la data di fine (può essere null)
     * @param costoMin il costo minimo (può essere negativo per ignorare)
     * @param costoMax il costo massimo (può essere negativo per ignorare)
     * @return una lista di proiezioni che soddisfano i criteri
     * 
     */
    public List<Proiezione> cercaProiezioni(String titolo, String genere,
            LocalDateTime dataDa, LocalDateTime dataA,
            double costoMin, double costoMax) {
        List<Proiezione> risultati = new ArrayList<>();

        for (Proiezione p : proiezioni) {
            // Controlla titolo
            if (titolo != null && !titolo.isEmpty()) {
                if (!p.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                    continue;
                }
            }

            // Controlla genere
            if (genere != null && !genere.isEmpty()) {
                if (!p.getFilm().getGenere().equalsIgnoreCase(genere)) {
                    continue;
                }
            }

            // Controlla data inizio
            if (dataDa != null) {
                if (!p.getDataOraProiezione().isAfter(dataDa) && !p.getDataOraProiezione().isEqual(dataDa)) {
                    continue;
                }
            }

            // Controlla data fine
            if (dataA != null) {
                if (!p.getDataOraProiezione().isBefore(dataA) && !p.getDataOraProiezione().isEqual(dataA)) {
                    continue;
                }
            }

            // Controlla costo minimo
            if (costoMin >= 0) {
                if (p.getCostoBiglietto() < costoMin) {
                    continue;
                }
            }

            // Controlla costo massimo
            if (costoMax >= 0) {
                if (p.getCostoBiglietto() > costoMax) {
                    continue;
                }
            }

            // Se passa tutti i filtri, aggiungilo ai risultati
            risultati.add(p);
        }

        return risultati;
    }

    // ============ METODI PER AGGIUNGERE/MODIFICARE/ELIMINARE PROIEZIONI
    // ============

    /**
     * Aggiunge una nuova proiezione
     * 
     * @param film           il film da proiettare
     * @param dataOra        la data e ora della proiezione
     * @param costoBiglietto il costo del biglietto
     * @return true se la proiezione è stata aggiunta con successo, false altrimenti
     */
    public boolean aggiungiProiezione(Film film, LocalDateTime dataOra, double costoBiglietto) {
        // Controlla che non esista già una proiezione a quella data
        for (Proiezione p : proiezioni) {
            if (p.getDataOraProiezione().equals(dataOra)) {
                return false;
            }
        }

        // Crea la nuova proiezione
        int nuovoId = proiezioni.size();
        Proiezione nuovaProiezione = new Proiezione(nuovoId, film, dataOra, costoBiglietto);
        proiezioni.add(nuovaProiezione);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }

    /**
     * Modifica una proiezione
     * 
     * @param idProiezione l'ID della proiezione da modificare
     * @param nuovaDataOra la nuova data e ora della proiezione
     * @return true se la proiezione è stata modificata con successo, false
     *         altrimenti
     */
    public boolean modificaProiezione(int idProiezione, LocalDateTime nuovaDataOra) {
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return false;
        }

        // Controlla se ci sono prenotazioni
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
     * Elimina una proiezione
     * 
     * @param idProiezione l'ID della proiezione da eliminare
     * @return true se la proiezione è stata eliminata con successo, false
     *         altrimenti
     */
    public boolean eliminaProiezione(int idProiezione) {
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return false;
        }

        // Controlla se ci sono prenotazioni
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

    // ============ METODI PER PRENOTAZIONI ============

    /**
     * Crea una nuova prenotazione
     * 
     * @param idProiezione    l'ID della proiezione da prenotare
     * @param numeroBiglietti il numero di biglietti da prenotare
     * @return la prenotazione creata se avvenuta con successo, null altrimenti
     */
    public Prenotazione creaPrenotazione(int idProiezione, int numeroBiglietti) {
        // Solo i clienti possono prenotare
        if (!(utenteCorrente instanceof Cliente)) {
            return null;
        }

        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return null;
        }

        // Controlla che la proiezione sia nel futuro
        if (LocalDateTime.now().isAfter(proiezione.getDataOraProiezione())) {
            return null;
        }

        // Controlla che ci siano abbastanza posti
        int postiOccupati = calcolaPostiOccupati(idProiezione);
        int postiDisponibili = proiezione.getCapacitaSala() - postiOccupati;

        if (numeroBiglietti > postiDisponibili) {
            return null;
        }

        // Crea e salva la prenotazione
        Prenotazione prenotazione = new Prenotazione((Cliente) utenteCorrente, proiezione, numeroBiglietti);
        prenotazioni.add(prenotazione);
        GestoreDati.salvaPrenotazioni(prenotazioni);
        return prenotazione;
    }

    /**
     * Modifica una prenotazione
     * 
     * @param codicePrenotazione il codice della prenotazione da modificare
     * @param nuovaIdProiezione  l'ID della nuova proiezione
     * @return true se la prenotazione è stata modificata con successo, false
     *         altrimenti
     */
    public boolean modificaPrenotazione(String codicePrenotazione, int nuovaIdProiezione) {
        Prenotazione prenotazione = trovaPrenotazione(codicePrenotazione);
        if (prenotazione == null) {
            return false;
        }

        // Controlla che la proiezione precedente non sia già passata
        if (LocalDateTime.now().isAfter(prenotazione.getProiezione().getDataOraProiezione())) {
            return false;
        }

        Proiezione nuovaProiezione = trovaProiezione(nuovaIdProiezione);
        if (nuovaProiezione == null) {
            return false;
        }

        // Controlla che la nuova proiezione non sia già passata
        if (LocalDateTime.now().isAfter(nuovaProiezione.getDataOraProiezione())) {
            return false;
        }

        // Controlla disponibilità della nuova proiezione
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
     * Elimina una prenotazione
     * 
     * @param codicePrenotazione il codice della prenotazione da eliminare
     * @return true se la prenotazione è stata eliminata con successo, false
     *         altrimenti
     */
    public boolean eliminaPrenotazione(String codicePrenotazione) {
        Prenotazione prenotazione = trovaPrenotazione(codicePrenotazione);
        if (prenotazione == null) {
            return false;
        }

        // Controlla che la proiezione sia nel futuro
        if (LocalDateTime.now().isBefore(prenotazione.getProiezione().getDataOraProiezione())) {
            prenotazioni.remove(prenotazione);
            GestoreDati.salvaPrenotazioni(prenotazioni);
            return true;
        }

        return false;
    }

    // ============ METODI UTILI ============

    /**
     * Calcola quanti posti sono occupati per una proiezione
     * 
     * @param idProiezione l'ID della proiezione
     * @return il numero di posti occupati per la proiezione
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
     * Trova una proiezione per id
     * 
     * @param id l'ID della proiezione da trovare
     * @return la proiezione trovata, null se non esiste
     * 
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
     * Trova una prenotazione per codice
     * 
     * @param codice il codice della prenotazione da trovare
     * @return la prenotazione trovata, null se non esiste
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
     * Ottiene le prenotazioni del cliente loggato
     * 
     * @return una lista di prenotazioni del cliente loggato
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
     * Ottiene le prenotazioni di oggi
     * 
     * @return una lista di prenotazioni effettuate oggi
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
     * Cerca prenotazioni per bigliettaio
     * 
     * @param codice         il codice della prenotazione (può essere null o vuoto)
     * @param nomeCliente    il nome del cliente (può essere null o vuoto)
     * @param cognomeCliente il cognome del cliente (può essere null o vuoto)
     * @param titoloFilm     il titolo del film (può essere null o vuoto)
     * @param dataDa         la data di inizio (può essere null)
     * @param dataA          la data di fine (può essere null)
     * @return una lista di prenotazioni che soddisfano i criteri
     */
    public List<Prenotazione> cercaPrenotazioni(String codice, String nomeCliente,
            String cognomeCliente, String titoloFilm,
            LocalDateTime dataDa, LocalDateTime dataA) {
        List<Prenotazione> risultati = new ArrayList<>();

        for (Prenotazione p : prenotazioni) {
            // Controlla codice
            if (codice != null && !codice.isEmpty()) {
                if (!p.getCodicePrenotazione().contains(codice)) {
                    continue;
                }
            }

            // Controlla nome cliente
            if (nomeCliente != null && !nomeCliente.isEmpty()) {
                if (!p.getCliente().getNome().toLowerCase().contains(nomeCliente.toLowerCase())) {
                    continue;
                }
            }

            // Controlla cognome cliente
            if (cognomeCliente != null && !cognomeCliente.isEmpty()) {
                if (!p.getCliente().getCognome().toLowerCase().contains(cognomeCliente.toLowerCase())) {
                    continue;
                }
            }

            // Controlla titolo film
            if (titoloFilm != null && !titoloFilm.isEmpty()) {
                if (!p.getProiezione().getFilm().getTitolo().toLowerCase().contains(titoloFilm.toLowerCase())) {
                    continue;
                }
            }

            // Controlla data inizio
            if (dataDa != null) {
                if (!p.getProiezione().getDataOraProiezione().isAfter(dataDa) &&
                        !p.getProiezione().getDataOraProiezione().isEqual(dataDa)) {
                    continue;
                }
            }

            // Controlla data fine
            if (dataA != null) {
                if (!p.getProiezione().getDataOraProiezione().isBefore(dataA) &&
                        !p.getProiezione().getDataOraProiezione().isEqual(dataA)) {
                    continue;
                }
            }

            // Se passa tutti i filtri, aggiungilo
            risultati.add(p);
        }

        return risultati;
    }

    // ============ GETTER ============

    /**
     * Ottiene l'utente corrente
     * 
     * @return l'utente corrente
     */
    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    /**
     * Ottiene la lista delle proiezioni
     * 
     * @return la lista delle proiezioni
     */
    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }

    /**
     * Ottiene la lista delle prenotazioni
     * 
     * @return la lista delle prenotazioni
     */
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }
}