package cinemax;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale dell'applicazione CineMax.
 * Gestisce il loop principale e l'interfaccia utente interattiva da terminale (TUI).
 * 
 * @author Andrea
 * @version 1.0
 */
public class CineMax {
    private Sistema sistema;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Costruttore della classe CineMax.
     * Si occupa di istanziare il cuore logico dell'applicativo (Sistema) e 
     * lo scanner di sistema per leggere gli input testuali dell'utente.
     */
    public CineMax() {
        this.sistema = new Sistema();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Avvia il ciclo infinito dell'applicazione.
     * Controlla costantemente lo stato di autenticazione dell'utente 
     * smistandolo tra il menu pre-login o il menu post-login corrispondente al suo ruolo.
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
     * Visualizza il menu principale di partenza (per utenti non loggati) e smista l'azione scelta.
     * 
     * @return true se l'utente desidera proseguire, false se ha selezionato l'uscita ("Esci").
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
     * Metodo ponte che si occupa di intercettare il ruolo (Cliente, Proiezionista, Bigliettaio) 
     * dell'utente appena loggato e invocarne il rispettivo menu contestuale.
     * 
     * @return true, poiché i sub-menu restituiscono l'esito della sessione
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
     * Stampa e gestisce la vista e l'input delle scelte limitate per l'Utente Guest.
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
     * Stampa e gestisce il ciclo del menu specifico per i permessi del Cliente 
     * (ricerca palinsesto, gestione delle proprie prenotazioni).
     * 
     * @return true se il cliente effettua un logout pulito (rimanendo nel programma)
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
     * Stampa e gestisce il ciclo del menu specifico per i permessi del Proiezionista 
     * (controllo e inserimento proiezioni a palinsesto).
     * 
     * @return true se il proiezionista effettua un logout pulito (rimanendo nel programma)
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
     * Stampa e gestisce il ciclo del menu specifico per i permessi del Bigliettaio 
     * (ricerca e validazione delle prenotazioni in cassa).
     * 
     * @return true se il bigliettaio effettua un logout pulito (rimanendo nel programma)
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
     * Richiede le credenziali in input (username e password) e invoca il metodo logico di Login.
     * Gestisce la stampa dell'esito dell'operazione.
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
     * Richiede tutti i dati anagrafici necessari per la creazione di un nuovo utente (Cliente).
     * Intercetta la formattazione testuale della Data e invoca il metodo logico di registrazione.
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
     * Interfaccia che richiede una singola giornata specifica e invoca il motore 
     * di ricerca sottostante per stampare a schermo l'elenco dei film disponibili per tale data 
     * corredato dai calcoli dei posti liberi della sala.
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
     * Interfaccia flessibile (i campi sono tutti opzionali) per consentire all'utente 
     * di immettere molteplici filtri contestuali che limitano i risultati delle proiezioni restituite.
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
     * Interfaccia che richiede l'inserimento dell'ID intero relativo ad una specifica 
     * proiezione e ne stampa a schermo in dettaglio tutti i valori interni collegati.
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
     * Metodo di utility (tipicamente usato dai Proiezionisti) che stampa a schermo 
     * la lista scarna e completa di tutti gli identificativi delle proiezioni presenti.
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
     * Interfaccia in uso al Cliente. Richiede in ingresso ID e Quantità per validare, stampare 
     * il riassunto dell'acquisto ed emettere il nuovo biglietto/codice prenotazione.
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
     * Stampa in sequenza tutte e sole le prenotazioni (e i relativi biglietti) connesse
     * in maniera univoca al Cliente al momento autenticato.
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
     * Permette ad un Cliente di variare, conoscendone il codice (alfanumerico), 
     * lo slot orario/proiezione a cui è legata una prenotazione già esistente.
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
     * Permette ad un Cliente (o al sistema in via autonoma) di distruggere l'oggetto Prenotazione, 
     * liberando i posti che occupava in sala.
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
     * Interfaccia rapida in uso esclusivamente al Bigliettaio. Isola le query filtrando 
     * sul `LocalDate.now()` al fine di restituire la lista d'appello della clientela odierna.
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
     * Strumento di query per i Bigliettai in caso di clienti che hanno smarrito il biglietto.
     * Permette cross-ricerche per codice, oppure nominativo o frammento di esso.
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
     * Procedura ad alto livello di permessi (Proiezionista). Genera in sequenza gli oggetti 
     * Film e Proiezione necessari riempiendo le rispettive istanze dai parametri a terminale.
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
     * Metodo per i Proiezionisti volto a slittare la riga oraria della proiezione. 
     * Può fallire se all'ID in questione sono collegate Prenotazioni correnti.
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
     * Metodo per i Proiezionisti che rimuove forzatamente il palinsesto del film, 
     * a patto che non vi siano già clienti paganti iscritti allo slot indicato dall'ID.
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
     * Entry point assoluto del software. Permette l'avvio della JVM chiamando `CineMax.avvia()`.
     * 
     * @param args gli eventuali parametri stringa passati nel comando di esecuzione. Non elaborati.
     */
    public static void main(String[] args) {
        CineMax app = new CineMax();
        app.avvia();
    }
}