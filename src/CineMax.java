import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale dell'applicazione CineMax
 * Gestisce l'interfaccia utente da terminale
 * @author Andrea
 * @version 1.0
 */
public class CineMax {
    private Sistema sistema;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Costruttore
     */
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
     */
    /**
     * Menu principale prima del login
     */
    private boolean menuPrincipale() {
        System.out.println("\n");
        System.out.println("  ██████╗ ██╗███╗   ██╗███████╗███╗   ███╗███████╗██╗  ██╗");
        System.out.println(" ██╔════╝ ██║████╗  ██║██╔════╝████╗ ████║██╔════╝╚██╗██╔╝");
        System.out.println(" ██║      ██║██╔██╗ ██║█████╗  ██╔████╔██║███████╗ ╚███╔╝ ");
        System.out.println(" ██║      ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ██╔██╗ ");
        System.out.println(" ╚██████╗ ██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║██╔╝ ██╗");
        System.out.println("  ╚═════╝ ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝");
        System.out.println(" ══════════════ SISTEMA DI PRENOTAZIONE ══════════════════\n");
        System.out.println(" ╔═══════════════════════════════════════════════════════╗");
        System.out.println(" ║                                                       ║");
        System.out.println(" ║  [1] 👤 Login                                         ║");
        System.out.println(" ║  [2] 📝 Registrazione                                 ║");
        System.out.println(" ║  [3] 🍿 Continua come Guest                           ║");
        System.out.println(" ║  [4] ❌ Esci                                          ║");
        System.out.println(" ║                                                       ║");
        System.out.println(" ╚═══════════════════════════════════════════════════════╝");
        System.out.print("\n ➤ Scelta: ");
        
        String scelta = scanner.nextLine().trim();
        
        if (scelta.equals("1")) {
            login();
        } else if (scelta.equals("2")) {
            registrazione();
        } else if (scelta.equals("3")) {
            menuGuest();
        } else if (scelta.equals("4")) {
            return false;
        } else {
            System.out.println("\n ⚠️  Scelta non valida! Riprova.");
        }
        
        return true;
    }
    
    /**
     * Menu principale dopo il login
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
     * Menu per i guest
     */
    private void menuGuest() {
        boolean continua = true;
        
        while (continua) {
            System.out.println("\n========== MENU GUEST ==========");
            System.out.println("1. Visualizza film per giorno");
            System.out.println("2. Ricerca avanzata");
            System.out.println("3. Dettagli proiezione");
            System.out.println("4. Torna indietro");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            if (scelta.equals("1")) {
                cercaProiezioniPerGiorno();
            } else if (scelta.equals("2")) {
                cercaProiezioni();
            } else if (scelta.equals("3")) {
                visualizzaDettagliProiezione();
            } else if (scelta.equals("4")) {
                continua = false;
            } else {
                System.out.println("Scelta non valida!");
            }
        }
    }
    
    /**
     * Menu per la gesione dei clienti
     */
    private boolean menuCliente() {
        boolean continua = true;
        
        while (continua) {
            System.out.println("\n========== MENU CLIENTE ==========");
            System.out.println("1. Visualizza film per giorno");
            System.out.println("2. Ricerca avanzata");
            System.out.println("3. Dettagli proiezione");
            System.out.println("4. Le mie prenotazioni");
            System.out.println("5. Crea prenotazione");
            System.out.println("6. Modifica prenotazione");
            System.out.println("7. Cancella prenotazione");
            System.out.println("8. Logout");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            if (scelta.equals("1")) {
                cercaProiezioniPerGiorno();
            } else if (scelta.equals("2")) {
                cercaProiezioni();
            } else if (scelta.equals("3")) {
                visualizzaDettagliProiezione();
            } else if (scelta.equals("4")) {
                visualizzaPrenotazioniCliente();
            } else if (scelta.equals("5")) {
                creaPrenotazione();
            } else if (scelta.equals("6")) {
                modificaPrenotazione();
            } else if (scelta.equals("7")) {
                cancellaPrenotazione();
            } else if (scelta.equals("8")) {
                sistema.logout();
                return true;
            } else {
                System.out.println("Scelta non valida!");
            }
        }
        
        return true;
    }
    
    /**
     * Menu per la gestione dei  proiezionisti
     */
    private boolean menuProiezionista() {
        boolean continua = true;
        
        while (continua) {
            System.out.println("\n========== MENU PROIEZIONISTA ==========");
            System.out.println("1. Visualizza film per giorno");
            System.out.println("2. Aggiungi proiezione");
            System.out.println("3. Modifica proiezione");
            System.out.println("4. Elimina proiezione");
            System.out.println("5. Visualizza tutte le proiezioni");
            System.out.println("6. Logout");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            if (scelta.equals("1")) {
                cercaProiezioniPerGiorno();
            } else if (scelta.equals("2")) {
                aggiungiProiezione();
            } else if (scelta.equals("3")) {
                modificaProiezione();
            } else if (scelta.equals("4")) {
                eliminaProiezione();
            } else if (scelta.equals("5")) {
                visualizzaTutteProiezioni();
            } else if (scelta.equals("6")) {
                sistema.logout();
                return true;
            } else {
                System.out.println("Scelta non valida!");
            }
        }
        
        return true;
    }
    
    /**
     * Menu per la gestione dei bigliettai
     */
    private boolean menuBigliettaio() {
        boolean continua = true;
        
        while (continua) {
            System.out.println("\n========== MENU BIGLIETTAIO ==========");
            System.out.println("1. Visualizza film per giorno");
            System.out.println("2. Prenotazioni di oggi");
            System.out.println("3. Cerca prenotazione");
            System.out.println("4. Logout");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine().trim();
            
            if (scelta.equals("1")) {
                cercaProiezioniPerGiorno();
            } else if (scelta.equals("2")) {
                visualizzaPrenotazioniOggi();
            } else if (scelta.equals("3")) {
                cercaPrenotazione();
            } else if (scelta.equals("4")) {
                sistema.logout();
                return true;
            } else {
                System.out.println("Scelta non valida!");
            }
        }
        
        return true;
    }
    
    // ============ METODI DI LOGIN ============
    
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
     * Registra un nuovo cliente
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
        
        System.out.print("Data di nascita (yyyy-MM-dd) [opzionale]: ");
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
    
    // ============ METODI DI RICERCA ============
    
    /**
     * Cerca proiezioni per giorno
     */
    private void cercaProiezioniPerGiorno() {
        System.out.println("\n========== RICERCA FILM PER GIORNO ==========");
        System.out.print("Inserisci il giorno (yyyy-MM-dd): ");
        String giornoStr = scanner.nextLine().trim();
        
        if (giornoStr.isEmpty()) {
            System.out.println("Giorno non valido!");
            return;
        }
        
        try {
            LocalDate giorno = LocalDate.parse(giornoStr, DATE_FORMATTER);
            LocalDateTime inizio = giorno.atStartOfDay();
            LocalDateTime fine = giorno.atTime(23, 59, 59);
            
            List<Proiezione> risultati = sistema.cercaProiezioni(
                null,
                null,
                inizio,
                fine,
                -1,
                -1
            );
            
            if (risultati.isEmpty()) {
                System.out.println("Nessun film disponibile per il " + giornoStr);
            } else {
                System.out.println("\nFilm disponibili il " + giornoStr + ":\n");
                
                for (Proiezione p : risultati) {
                    String ora = p.getDataOraProiezione().format(DateTimeFormatter.ofPattern("HH:mm"));
                    String titolo = p.getFilm().getTitolo();
                    String genere = p.getFilm().getGenere();
                    double costo = p.getCostoBiglietto();
                    int postiLiberi = p.getCapacitaSala() - sistema.calcolaPostiOccupati(p.getId());
                    
                    System.out.println("[ID: " + p.getId() + "] " + titolo + " (" + genere + ")");
                    System.out.println("  Orario: " + ora + " | Costo: " + costo + "€ | Posti: " + postiLiberi);
                }
            }
        } catch (Exception e) {
            System.out.println("Formato data non valido!");
        }
    }
    
    /**
     * Ricerca avanzata proiezioni
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
        
        List<Proiezione> risultati = sistema.cercaProiezioni(
            titolo.isEmpty() ? null : titolo,
            genere.isEmpty() ? null : genere,
            dataInizio,
            dataFine,
            -1,
            -1
        );
        
        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata!");
        } else {
            System.out.println("\nProiezioni trovate: " + risultati.size() + "\n");
            for (Proiezione p : risultati) {
                System.out.println("[ID: " + p.getId() + "] " + p.getFilm().getTitolo());
                System.out.println("  Data: " + p.getDataOraProiezione().format(DATETIME_FORMATTER));
                System.out.println("  Costo: " + p.getCostoBiglietto() + "€");
                System.out.println("  Posti liberi: " + (p.getCapacitaSala() - sistema.calcolaPostiOccupati(p.getId())));
            }
        }
    }
    
    /**
     * Visualizza i dettagli di una proiezione
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
            System.out.println("Costo biglietto: " + p.getCostoBiglietto() + "€");
            System.out.println("Posti liberi: " + (p.getCapacitaSala() - sistema.calcolaPostiOccupati(p.getId())));
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
            System.out.println("\nID: " + p.getId());
            System.out.println("Film: " + p.getFilm().getTitolo());
            System.out.println("Data/Ora: " + p.getDataOraProiezione().format(DATETIME_FORMATTER));
            System.out.println("Costo: " + p.getCostoBiglietto() + "€");
        }
    }
    
    // ============ METODI PER PRENOTAZIONI ============
    
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
                System.out.println("Costo totale: " + prenotazione.getCostoTotale() + "€");
            } else {
                System.out.println("Prenotazione non riuscita!");
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
            System.out.println("\nCodice: " + p.getCodicePrenotazione());
            System.out.println("Film: " + p.getProiezione().getFilm().getTitolo());
            System.out.println("Data/Ora: " + p.getProiezione().getDataOraProiezione().format(DATETIME_FORMATTER));
            System.out.println("Biglietti: " + p.getNumeroBiglietti());
            System.out.println("Costo totale: " + p.getCostoTotale() + "€");
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
                System.out.println("Modifica non riuscita!");
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
     * Visualizza prenotazioni di oggi
     */
    private void visualizzaPrenotazioniOggi() {
        List<Prenotazione> prenotazioni = sistema.getPrenotazioniOggi();
        
        if (prenotazioni.isEmpty()) {
            System.out.println("Nessuna prenotazione per oggi!");
            return;
        }
        
        System.out.println("\n========== PRENOTAZIONI DI OGGI ==========");
        for (Prenotazione p : prenotazioni) {
            System.out.println("\nCodice: " + p.getCodicePrenotazione());
            System.out.println("Cliente: " + p.getCliente().getNome() + " " + p.getCliente().getCognome());
            System.out.println("Film: " + p.getProiezione().getFilm().getTitolo());
            System.out.println("Biglietti: " + p.getNumeroBiglietti());
            System.out.println("Costo totale: " + p.getCostoTotale() + "€");
        }
    }
    
    /**
     * Cerca una prenotazione
     */
    private void cercaPrenotazione() {
        System.out.println("\n========== RICERCA PRENOTAZIONE ==========");
        
        System.out.print("Codice [opzionale]: ");
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
            System.out.println("\nPrenotazioni trovate: " + risultati.size() + "\n");
            for (Prenotazione p : risultati) {
                System.out.println("Codice: " + p.getCodicePrenotazione());
                System.out.println("Cliente: " + p.getCliente().getNome() + " " + p.getCliente().getCognome());
                System.out.println("Film: " + p.getProiezione().getFilm().getTitolo());
                System.out.println("Data: " + p.getProiezione().getDataOraProiezione().format(DATETIME_FORMATTER));
                System.out.println("Biglietti: " + p.getNumeroBiglietti());
                System.out.println("Costo: " + p.getCostoTotale() + "€\n");
            }
        }
    }
    
    // ============ METODI PER PROIEZIONI (PROIEZIONISTA) ============
    
    /**
     * Aggiunge una nuova proiezione
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
            System.out.println("Proiezione non aggiunta!");
        }
    }
    
    /**
     * Modifica una proiezione
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
     * Elimina una proiezione
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
     * Metodo main
     */
    public static void main(String[] args) {
        CineMax app = new CineMax();
        app.avvia();
    }
}