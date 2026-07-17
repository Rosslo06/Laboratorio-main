

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale dell'applicazione CineMax
 * Gestisce l'interfaccia utente terminale (TUI)
 * @author Andrea
 * @version 1.0
 */
public class CineMax {
    private Sistema sistema;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public CineMax() {
        this.sistema = new Sistema();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Avvia l'applicazione
     */
    public void avvia() {
        boolean esecuzione = true;
        
        while (esecuzione) {
            if (sistema.getUtenteCorrente() == null) {
                esecuzione = menuPrincipale();
            } else {
                esecuzione = menuPrincipalePosLogin();
            }
        }
        
        scanner.close();
        System.out.println("\nGrazie per aver utilizzato CineMax!");
    }
    
    /**
     * Menu principale prima del login
     * @return false per uscire
     */
    private boolean menuPrincipale() {
        System.out.println("\n========== CINEMAX - SISTEMA DI PRENOTAZIONE ==========");
        System.out.println("1. Login");
        System.out.println("2. Registrazione");
        System.out.println("3. Continua come Guest");
        System.out.println("4. Esci");
        System.out.print("Scelta: ");
        
        String scelta = scanner.nextLine().trim();
        
        switch (scelta) {
            case "1":
                login();
                break;
            case "2":
                registrazione();
                break;
            case "3":
                menuGuest();
                break;
            case "4":
                return false;
            default:
                System.out.println("Scelta non valida!");
        }
        
        return true;
    }
    
    /**
     * Menu principale dopo il login
     * @return false per uscire
     */
    private boolean menuPrincipalePosLogin() {
        String ruolo = sistema.getUtenteCorrente().getRuolo();
        
        System.out.println("\n========== MENU PRINCIPALE ==========");
        System.out.println("Benvenuto " + sistema.getUtenteCorrente().getNome() + "!");
        
        if (ruolo.equals("cliente")) {
            return menuCliente();
        } else if (ruolo.equals("proiezionista")) {
            return menuProiezionista();
        } else if (ruolo.equals("bigliettaio")) {
            return menuBigliettaio();
        }
        
        return true;
    }
    
    /**
     * Menu per i guest (accesso senza login)
     */
    private void menuGuest() {
        boolean continuaGuest = true;
        
        while (continuaGuest) {
            System.out.println("\n========== MENU GUEST ==========");
            System.out.println("1. Cerca proiezioni");
            System.out.println("2. Visualizza dettagli proiezione");
            System.out.println("3. Torna al menu principale");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            switch (scelta) {
                case "1":
                    cercaProiezioni();
                    break;
                case "2":
                    visualizzaDettagliProiezione();
                    break;
                case "3":
                    continuaGuest = false;
                    break;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }
    
    /**
     * Menu per i clienti
     * @return false per logout
     */
    private boolean menuCliente() {
        boolean continuaCliente = true;
        
        while (continuaCliente) {
            System.out.println("\n========== MENU CLIENTE ==========");
            System.out.println("1. Cerca proiezioni");
            System.out.println("2. Visualizza dettagli proiezione");
            System.out.println("3. Le mie prenotazioni");
            System.out.println("4. Crea nuova prenotazione");
            System.out.println("5. Modifica prenotazione");
            System.out.println("6. Cancella prenotazione");
            System.out.println("7. Logout");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            switch (scelta) {
                case "1":
                    cercaProiezioni();
                    break;
                case "2":
                    visualizzaDettagliProiezione();
                    break;
                case "3":
                    visualizzaPrenotazioniCliente();
                    break;
                case "4":
                    creaPrenotazione();
                    break;
                case "5":
                    modificaPrenotazione();
                    break;
                case "6":
                    cancellaPrenotazione();
                    break;
                case "7":
                    sistema.logout();
                    return true;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
        
        return true;
    }
    
    /**
     * Menu per i proiezionisti
     * @return false per logout
     */
    private boolean menuProiezionista() {
        boolean continua = true;
        
        while (continua) {
            System.out.println("\n========== MENU PROIEZIONISTA ==========");
            System.out.println("1. Aggiungi proiezione");
            System.out.println("2. Modifica proiezione");
            System.out.println("3. Elimina proiezione");
            System.out.println("4. Visualizza tutte le proiezioni");
            System.out.println("5. Logout");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            switch (scelta) {
                case "1":
                    aggiungiProiezione();
                    break;
                case "2":
                    modificaProiezione();
                    break;
                case "3":
                    eliminaProiezione();
                    break;
                case "4":
                    visualizzaTutteProiezioni();
                    break;
                case "5":
                    sistema.logout();
                    return true;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
        
        return true;
    }
    
    /**
     * Menu per i bigliettai
     * @return false per logout
     */
    private boolean menuBigliettaio() {
        boolean continua = true;
        
        while (continua) {
            System.out.println("\n========== MENU BIGLIETTAIO ==========");
            System.out.println("1. Visualizza prenotazioni di oggi");
            System.out.println("2. Cerca prenotazione");
            System.out.println("3. Logout");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            switch (scelta) {
                case "1":
                    visualizzaPrenotazioniOggi();
                    break;
                case "2":
                    cercaPrenotazione();
                    break;
                case "3":
                    sistema.logout();
                    return true;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
        
        return true;
    }
    
    /**
     * Effettua il login
     */
    private void login() {
        System.out.println("\n========== LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        if (sistema.login(username, password)) {
            System.out.println("Login effettuato con successo!");
        } else {
            System.out.println("Username o password non corretti!");
        }
    }
    
    /**
     * Effettua la registrazione
     */
    private void registrazione() {
        System.out.println("\n========== REGISTRAZIONE ==========");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine().trim();
        
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Data di nascita (yyyy-MM-dd) [opzionale, premi Invio]: ");
        String dataStr = scanner.nextLine().trim();
        LocalDate dataNascita = null;
        if (!dataStr.isEmpty()) {
            try {
                dataNascita = LocalDate.parse(dataStr, DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println("Formato data non valido!");
            }
        }
        
        System.out.print("Luogo di domicilio: ");
        String luogo = scanner.nextLine().trim();
        
        if (sistema.registraCliente(nome, cognome, username, password, dataNascita, luogo)) {
            System.out.println("Registrazione effettuata con successo!");
        } else {
            System.out.println("Username già esistente!");
        }
    }
    
    /**
     * Cerca proiezioni
     */
    private void cercaProiezioni() {
        System.out.println("\n========== RICERCA PROIEZIONI ==========");
        System.out.print("Titolo film [opzionale]: ");
        String titolo = scanner.nextLine().trim();
        
        System.out.print("Genere [opzionale]: ");
        String genere = scanner.nextLine().trim();
        
        System.out.print("Data inizio (yyyy-MM-dd) [opzionale]: ");
        String dataInizioStr = scanner.nextLine().trim();
        LocalDateTime dataInizio = null;
        if (!dataInizioStr.isEmpty()) {
            try {
                dataInizio = LocalDate.parse(dataInizioStr, DATE_FORMATTER).atStartOfDay();
            } catch (Exception e) {
                System.out.println("Formato data non valido!");
            }
        }
        
        System.out.print("Data fine (yyyy-MM-dd) [opzionale]: ");
        String dataFineStr = scanner.nextLine().trim();
        LocalDateTime dataFine = null;
        if (!dataFineStr.isEmpty()) {
            try {
                dataFine = LocalDate.parse(dataFineStr, DATE_FORMATTER).atStartOfDay();
            } catch (Exception e) {
                System.out.println("Formato data non valido!");
            }
        }
        
        System.out.print("Costo minimo € [opzionale]: ");
        double costoMin = -1;
        try {
            String costoMinStr = scanner.nextLine().trim();
            if (!costoMinStr.isEmpty()) {
                costoMin = Double.parseDouble(costoMinStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato non valido!");
        }
        
        System.out.print("Costo massimo € [opzionale]: ");
        double costoMax = -1;
        try {
            String costoMaxStr = scanner.nextLine().trim();
            if (!costoMaxStr.isEmpty()) {
                costoMax = Double.parseDouble(costoMaxStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato non valido!");
        }
        
        List<Proiezione> risultati = sistema.cercaProiezioni(
            titolo.isEmpty() ? null : titolo,
            genere.isEmpty() ? null : genere,
            dataInizio,
            dataFine,
            costoMin,
            costoMax
        );
        
        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata!");
        } else {
            System.out.println("\nProiezioni trovate:");
            for (int i = 0; i < risultati.size(); i++) {
                Proiezione p = risultati.get(i);
                System.out.printf("\n[%d] ID: %d\n", i, p.getId());
                System.out.println("    Film: " + p.getFilm().getTitolo());
                System.out.println("    Data/Ora: " + p.getDataOraProiezione().format(DATETIME_FORMATTER));
                System.out.printf("    Costo: %.2f€\n", p.getCostoBiglietto());
                System.out.println("    Posti disponibili: " + (p.getCapacitaSala() - sistema.calcolaPostiOccupati(p.getId())));
            }
        }
    }
    
    /**
     * Visualizza dettagli di una proiezione
     */
    private void visualizzaDettagliProiezione() {
        System.out.print("ID Proiezione: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Proiezione p = sistema.trovaProiezione(id);
            
            if (p == null) {
                System.out.println("Proiezione non trovata!");
                return;
            }
            
            System.out.println("\n========== DETTAGLI PROIEZIONE ==========");
            System.out.println("Titolo: " + p.getFilm().getTitolo());
            System.out.println("Genere: " + p.getFilm().getGenere());
            System.out.println("Regista: " + p.getFilm().getRegista());
            System.out.println("Anno: " + p.getFilm().getAnno());
            System.out.println("Durata: " + p.getFilm().getDurata() + " minuti");
            System.out.println("Età minima: " + p.getFilm().getEtaMinimaPubblico() + " anni");
            System.out.println("Data/Ora: " + p.getDataOraProiezione().format(DATETIME_FORMATTER));
            System.out.printf("Costo biglietto: %.2f€\n", p.getCostoBiglietto());
            System.out.println("Posti liberi: " + (p.getCapacitaSala() - sistema.calcolaPostiOccupati(p.getId())) + "/" + p.getCapacitaSala());
        } catch (NumberFormatException e) {
            System.out.println("ID non valido!");
        }
    }
    
    /**
     * Visualizza tutte le proiezioni
     */
    private void visualizzaTutteProiezioni() {
        List<Proiezione> proiezioni = sistema.getProiezioni();
        
        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione disponibile!");
            return;
        }
        
        System.out.println("\n========== TUTTE LE PROIEZIONI ==========");
        for (Proiezione p : proiezioni) {
            System.out.printf("\nID: %d\n", p.getId());
            System.out.println("Film: " + p.getFilm().getTitolo());
            System.out.println("Data/Ora: " + p.getDataOraProiezione().format(DATETIME_FORMATTER));
            System.out.printf("Costo: %.2f€\n", p.getCostoBiglietto());
        }
    }
    
    /**
     * Crea una nuova prenotazione
     */
    private void creaPrenotazione() {
        System.out.print("ID Proiezione: ");
        try {
            int idProiezione = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Numero biglietti: ");
            int numeroBiglietti = Integer.parseInt(scanner.nextLine().trim());
            
            Prenotazione prenotazione = sistema.creaPrenotazione(idProiezione, numeroBiglietti);
            
            if (prenotazione != null) {
                System.out.println("\nPrenotazione effettuata con successo!");
                System.out.println("Codice prenotazione: " + prenotazione.getCodicePrenotazione());
                System.out.printf("Costo totale: %.2f€\n", prenotazione.getCostoTotale());
            } else {
                System.out.println("Prenotazione non riuscita! Controlla i dati inseriti.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Dati non validi!");
        }
    }
    
    /**
     * Visualizza le prenotazioni del cliente
     */
    private void visualizzaPrenotazioniCliente() {
        List<Prenotazione> prenotazioni = sistema.getPrenotazioniCliente();
        
        if (prenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni!");
            return;
        }
        
        System.out.println("\n========== LE MIE PRENOTAZIONI ==========");
        for (Prenotazione p : prenotazioni) {
            System.out.printf("\nCodice: %s\n", p.getCodicePrenotazione());
            System.out.println("Film: " + p.getProiezione().getFilm().getTitolo());
            System.out.println("Data/Ora: " + p.getProiezione().getDataOraProiezione().format(DATETIME_FORMATTER));
            System.out.println("Biglietti: " + p.getNumeroBiglietti());
            System.out.printf("Costo totale: %.2f€\n", p.getCostoTotale());
        }
    }
    
    /**
     * Modifica una prenotazione
     */
    private void modificaPrenotazione() {
        System.out.print("Codice prenotazione: ");
        String codice = scanner.nextLine().trim();
        
        System.out.print("Nuovo ID proiezione: ");
        try {
            int nuovoId = Integer.parseInt(scanner.nextLine().trim());
            
            if (sistema.modificaPrenotazione(codice, nuovoId)) {
                System.out.println("Prenotazione modificata con successo!");
            } else {
                System.out.println("Modifica non riuscita! Controlla i dati.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID non valido!");
        }
    }
    
    /**
     * Cancella una prenotazione
     */
    private void cancellaPrenotazione() {
        System.out.print("Codice prenotazione: ");
        String codice = scanner.nextLine().trim();
        
        if (sistema.eliminaPrenotazione(codice)) {
            System.out.println("Prenotazione cancellata con successo!");
        } else {
            System.out.println("Cancellazione non riuscita!");
        }
    }
    
    /**
     * Aggiunge una nuova proiezione (proiezionista)
     */
    private void aggiungiProiezione() {
        System.out.println("\n========== AGGIUNGI PROIEZIONE ==========");
        
        System.out.print("Titolo film: ");
        String titolo = scanner.nextLine().trim();
        
        System.out.print("Genere: ");
        String genere = scanner.nextLine().trim();
        
        System.out.print("Regista: ");
        String regista = scanner.nextLine().trim();
        
        System.out.print("Anno: ");
        int anno = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Durata (minuti): ");
        int durata = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Età minima pubblica: ");
        int etaMinima = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Data e ora (yyyy-MM-dd HH:mm): ");
        LocalDateTime dataOra = LocalDateTime.parse(scanner.nextLine().trim(), DATETIME_FORMATTER);
        
        System.out.print("Costo biglietto (€): ");
        double costo = Double.parseDouble(scanner.nextLine().trim());
        
        Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
        
        if (sistema.aggiungiProiezione(film, dataOra, costo)) {
            System.out.println("Proiezione aggiunta con successo!");
        } else {
            System.out.println("Proiezione non aggiunta! Verifica i dati.");
        }
    }
    
    /**
     * Modifica una proiezione (proiezionista)
     */
    private void modificaProiezione() {
        System.out.print("ID Proiezione: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Nuova data e ora (yyyy-MM-dd HH:mm): ");
        LocalDateTime nuovaDataOra = LocalDateTime.parse(scanner.nextLine().trim(), DATETIME_FORMATTER);
        
        if (sistema.modificaProiezione(id, nuovaDataOra)) {
            System.out.println("Proiezione modificata con successo!");
        } else {
            System.out.println("Modifica non riuscita!");
        }
    }
    
    /**
     * Elimina una proiezione (proiezionista)
     */
    private void eliminaProiezione() {
        System.out.print("ID Proiezione: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        
        if (sistema.eliminaProiezione(id)) {
            System.out.println("Proiezione eliminata con successo!");
        } else {
            System.out.println("Eliminazione non riuscita!");
        }
    }
    
    /**
     * Visualizza prenotazioni di oggi (bigliettaio)
     */
    private void visualizzaPrenotazioniOggi() {
        List<Prenotazione> prenotazioni = sistema.getPrenotazioniOggi();
        
        if (prenotazioni.isEmpty()) {
            System.out.println("Nessuna prenotazione per oggi!");
            return;
        }
        
        System.out.println("\n========== PRENOTAZIONI DI OGGI ==========");
        for (Prenotazione p : prenotazioni) {
            System.out.printf("\nCodice: %s\n", p.getCodicePrenotazione());
            System.out.println("Cliente: " + p.getCliente().getNome() + " " + p.getCliente().getCognome());
            System.out.println("Film: " + p.getProiezione().getFilm().getTitolo());
            System.out.println("Biglietti: " + p.getNumeroBiglietti());
            System.out.printf("Costo totale: %.2f€\n", p.getCostoTotale());
        }
    }
    
    /**
     * Cerca una prenotazione (bigliettaio)
     */
    private void cercaPrenotazione() {
        System.out.println("\n========== RICERCA PRENOTAZIONE ==========");
        
        System.out.print("Codice prenotazione [opzionale]: ");
        String codice = scanner.nextLine().trim();
        
        System.out.print("Nome cliente [opzionale]: ");
        String nome = scanner.nextLine().trim();
        
        System.out.print("Cognome cliente [opzionale]: ");
        String cognome = scanner.nextLine().trim();
        
        System.out.print("Titolo film [opzionale]: ");
        String titolo = scanner.nextLine().trim();
        
        List<Prenotazione> risultati = sistema.cercaPrenotazioni(
            codice.isEmpty() ? null : codice,
            nome.isEmpty() ? null : nome,
            cognome.isEmpty() ? null : cognome,
            titolo.isEmpty() ? null : titolo,
            null,
            null
        );
        
        if (risultati.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata!");
        } else {
            System.out.println("\nPrenotazioni trovate:");
            for (Prenotazione p : risultati) {
                System.out.printf("\nCodice: %s\n", p.getCodicePrenotazione());
                System.out.println("Cliente: " + p.getCliente().getNome() + " " + p.getCliente().getCognome());
                System.out.println("Film: " + p.getProiezione().getFilm().getTitolo());
                System.out.println("Data/Ora: " + p.getProiezione().getDataOraProiezione().format(DATETIME_FORMATTER));
                System.out.println("Biglietti: " + p.getNumeroBiglietti());
                System.out.printf("Costo: %.2f€\n", p.getCostoTotale());
            }
        }
    }
    
    /**
     * Metodo main
     */
    public static void main(String[] args) {
        CineMax app = new CineMax();
        app.avvia();
    }
}
