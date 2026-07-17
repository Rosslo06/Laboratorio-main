

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe principale che gestisce la logica dell'applicazione CineMax
 * @author Andrea
 * @version 1.0
 */
public class Sistema {
    private List<Proiezione> proiezioni;
    private List<Utente> utenti;
    private List<Prenotazione> prenotazioni;
    private Utente utenteCorrente;
    
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
    
    /**
     * Effettua il login di un utente
     * @param username l'username
     * @param password la password
     * @return true se login riuscito, false altrimenti
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
     * @param nome nome del cliente
     * @param cognome cognome del cliente
     * @param username username
     * @param password password
     * @param dataNascita data di nascita
     * @param luogoDomicilio luogo domicilio
     * @return true se registrazione riuscita, false altrimenti
     */
    public boolean registraCliente(String nome, String cognome, String username, String password,
                                   LocalDate dataNascita, String luogoDomicilio) {
        for (Utente u : utenti) {
            if (u.getUsername().equals(username)) {
                return false; // Username già esistente
            }
        }
        
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
    
    /**
     * Cerca proiezioni in base ai criteri specificati
     * @param titolo titolo del film (opzionale)
     * @param genere genere del film (opzionale)
     * @param dataDa data inizio (opzionale)
     * @param dataA data fine (opzionale)
     * @param costoMin costo minimo (opzionale, -1 se non specificato)
     * @param costoMax costo massimo (opzionale, -1 se non specificato)
     * @return lista di proiezioni trovate
     */
    public List<Proiezione> cercaProiezioni(String titolo, String genere, 
                                            LocalDateTime dataDa, LocalDateTime dataA,
                                            double costoMin, double costoMax) {
        return proiezioni.stream()
            .filter(p -> titolo == null || titolo.isEmpty() || 
                    p.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase()))
            .filter(p -> genere == null || genere.isEmpty() || 
                    p.getFilm().getGenere().equalsIgnoreCase(genere))
            .filter(p -> dataDa == null || p.getDataOraProiezione().isAfter(dataDa) || 
                    p.getDataOraProiezione().isEqual(dataDa))
            .filter(p -> dataA == null || p.getDataOraProiezione().isBefore(dataA) || 
                    p.getDataOraProiezione().isEqual(dataA))
            .filter(p -> costoMin < 0 || p.getCostoBiglietto() >= costoMin)
            .filter(p -> costoMax < 0 || p.getCostoBiglietto() <= costoMax)
            .collect(Collectors.toList());
    }
    
    /**
     * Aggiunge una nuova proiezione
     * @param film il film della proiezione
     * @param dataOra data e ora della proiezione
     * @param costoBiglietto costo del biglietto
     * @return true se aggiunta riuscita, false altrimenti
     */
    public boolean aggiungiProiezione(Film film, LocalDateTime dataOra, double costoBiglietto) {
        // Verifica sovrapposizione
        for (Proiezione p : proiezioni) {
            if (p.getDataOraProiezione().equals(dataOra)) {
                return false; // Proiezione già esistente
            }
        }
        
        int nuovoId = proiezioni.size();
        Proiezione nuovaProiezione = new Proiezione(nuovoId, film, dataOra, costoBiglietto);
        proiezioni.add(nuovaProiezione);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }
    
    /**
     * Modifica una proiezione
     * @param idProiezione id della proiezione
     * @param nuovaDataOra nuova data e ora
     * @return true se modifica riuscita, false altrimenti
     */
    public boolean modificaProiezione(int idProiezione, LocalDateTime nuovaDataOra) {
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return false;
        }
        
        // Controlla se ci sono prenotazioni
        long prenotazioniCount = prenotazioni.stream()
            .filter(p -> p.getProiezione().getId() == idProiezione)
            .count();
        
        if (prenotazioniCount > 0) {
            return false; // Non puoi modificare se ci sono prenotazioni
        }
        
        proiezione.setDataOraProiezione(nuovaDataOra);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }
    
    /**
     * Elimina una proiezione
     * @param idProiezione id della proiezione
     * @return true se eliminazione riuscita, false altrimenti
     */
    public boolean eliminaProiezione(int idProiezione) {
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return false;
        }
        
        // Controlla se ci sono prenotazioni
        long prenotazioniCount = prenotazioni.stream()
            .filter(p -> p.getProiezione().getId() == idProiezione)
            .count();
        
        if (prenotazioniCount > 0) {
            return false; // Non puoi eliminare se ci sono prenotazioni
        }
        
        proiezioni.remove(proiezione);
        GestoreDati.salvaProiezioni(proiezioni);
        return true;
    }
    
    /**
     * Crea una prenotazione
     * @param idProiezione id della proiezione
     * @param numeroBiglietti numero di biglietti
     * @return la prenotazione creata o null se fallita
     */
    public Prenotazione creaPrenotazione(int idProiezione, int numeroBiglietti) {
        if (!(utenteCorrente instanceof Cliente)) {
            return null; // Solo i clienti possono prenotare
        }
        
        Proiezione proiezione = trovaProiezione(idProiezione);
        if (proiezione == null) {
            return null;
        }
        
        if (LocalDateTime.now().isAfter(proiezione.getDataOraProiezione())) {
            return null; // Proiezione già passata
        }
        
        int postiOccupati = calcolaPostiOccupati1(idProiezione);
        int postiDisponibili = proiezione.getCapacitaSala() - postiOccupati;
        
        if (numeroBiglietti > postiDisponibili) {
            return null; // Non ci sono abbastanza posti
        }
        
        Prenotazione prenotazione = new Prenotazione((Cliente) utenteCorrente, proiezione, numeroBiglietti);
        prenotazioni.add(prenotazione);
        GestoreDati.salvaPrenotazioni(prenotazioni);
        return prenotazione;
    }
    
    /**
     * Modifica una prenotazione
     * @param codicePrenotazione codice della prenotazione
     * @param nuovaIdProiezione nuovo id della proiezione
     * @return true se modifica riuscita, false altrimenti
     */
    public boolean modificaPrenotazione(String codicePrenotazione, int nuovaIdProiezione) {
        Prenotazione prenotazione = trovaPrenotazione(codicePrenotazione);
        if (prenotazione == null) {
            return false;
        }
        
        // Controlla data odierna
        if (LocalDateTime.now().isAfter(prenotazione.getProiezione().getDataOraProiezione())) {
            return false; // Proiezione già passata
        }
        
        Proiezione nuovaProiezione = trovaProiezione(nuovaIdProiezione);
        if (nuovaProiezione == null) {
            return false;
        }
        
        // Controlla disponibilità nuova proiezione
        int postiOccupati = calcolaPostiOccupati1(nuovaIdProiezione);
        int postiDisponibili = nuovaProiezione.getCapacitaSala() - postiOccupati;
        
        if (prenotazione.getNumeroBiglietti() > postiDisponibili) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(nuovaProiezione.getDataOraProiezione())) {
            return false; // Nuova proiezione già passata
        }
        
        prenotazione.setProiezione(nuovaProiezione);
        GestoreDati.salvaPrenotazioni(prenotazioni);
        return true;
    }
    
    /**
     * Elimina una prenotazione
     * @param codicePrenotazione codice della prenotazione
     * @return true se eliminazione riuscita, false altrimenti
     */
    public boolean eliminaPrenotazione(String codicePrenotazione) {
        Prenotazione prenotazione = trovaPrenotazione(codicePrenotazione);
        if (prenotazione == null) {
            return false;
        }
        
        // Controlla che sia una data futura
        if (LocalDateTime.now().isBefore(prenotazione.getProiezione().getDataOraProiezione())) {
            prenotazioni.remove(prenotazione);
            GestoreDati.salvaPrenotazioni(prenotazioni);
            return true;
        }
        
        return false;
    }
    
    /**
     * Calcola il numero di posti occupati per una proiezione
     * @param idProiezione id della proiezione
     * @return numero di posti occupati
     */
    public int calcolaPostiOccupati1(int idProiezione) {
        return (int) prenotazioni.stream()
            .filter(p -> p.getProiezione().getId() == idProiezione)
            .mapToInt(Prenotazione::getNumeroBiglietti)
            .sum();
    }
    
    /**
     * Trova una proiezione per id
     * @param id id della proiezione
     * @return la proiezione o null
     */
    public Proiezione trovaProiezione(int id) {
        return proiezioni.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Trova una prenotazione per codice
     * @param codice codice della prenotazione
     * @return la prenotazione o null
     */
    public Prenotazione trovaPrenotazione(String codice) {
        return prenotazioni.stream()
            .filter(p -> p.getCodicePrenotazione().equals(codice))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Ottiene le prenotazioni di un cliente
     * @return lista di prenotazioni del cliente
     */
    public List<Prenotazione> getPrenotazioniCliente() {
        if (!(utenteCorrente instanceof Cliente)) {
            return new ArrayList<>();
        }
        
        return prenotazioni.stream()
            .filter(p -> p.getCliente().getUsername().equals(utenteCorrente.getUsername()))
            .collect(Collectors.toList());
    }
    
    /**
     * Ottiene le prenotazioni di oggi per bigliettaio
     * @return lista di prenotazioni di oggi
     */
    public List<Prenotazione> getPrenotazioniOggi() {
        LocalDate oggi = LocalDate.now();
        return prenotazioni.stream()
            .filter(p -> p.getProiezione().getDataOraProiezione().toLocalDate().equals(oggi))
            .collect(Collectors.toList());
    }
    
    /**
     * Cerca prenotazioni per bigliettaio
     * @param codice codice prenotazione (opzionale)
     * @param nomeCliente nome cliente (opzionale)
     * @param cognomeCliente cognome cliente (opzionale)
     * @param titoloFilm titolo film (opzionale)
     * @param dataDa data inizio (opzionale)
     * @param dataA data fine (opzionale)
     * @return lista di prenotazioni trovate
     */
    public List<Prenotazione> cercaPrenotazioni(String codice, String nomeCliente,
                                               String cognomeCliente, String titoloFilm,
                                               LocalDateTime dataDa, LocalDateTime dataA) {
        return prenotazioni.stream()
            .filter(p -> codice == null || codice.isEmpty() || 
                    p.getCodicePrenotazione().contains(codice))
            .filter(p -> nomeCliente == null || nomeCliente.isEmpty() || 
                    p.getCliente().getNome().toLowerCase().contains(nomeCliente.toLowerCase()))
            .filter(p -> cognomeCliente == null || cognomeCliente.isEmpty() || 
                    p.getCliente().getCognome().toLowerCase().contains(cognomeCliente.toLowerCase()))
            .filter(p -> titoloFilm == null || titoloFilm.isEmpty() || 
                    p.getProiezione().getFilm().getTitolo().toLowerCase().contains(titoloFilm.toLowerCase()))
            .filter(p -> dataDa == null || p.getProiezione().getDataOraProiezione().isAfter(dataDa) || 
                    p.getProiezione().getDataOraProiezione().isEqual(dataDa))
            .filter(p -> dataA == null || p.getProiezione().getDataOraProiezione().isBefore(dataA) || 
                    p.getProiezione().getDataOraProiezione().isEqual(dataA))
            .collect(Collectors.toList());
    }
    
    // Getter
    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }
    
    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }
    
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public int calcolaPostiOccupati(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcolaPostiOccupati'");
    }
}
