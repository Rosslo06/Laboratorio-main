import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe che gestisce il salvataggio e il caricamento dei dati
 * Legge e scrive file CSV e TXT per persistenza dei dati
 * @author Andrea
 * @version 1.0
 */
public class GestoreDati {
    private static final String DIR_DATA = "data";
    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";
    private static final String FILE_CSV_PROIEZIONI = "data/proiezioni.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Blocco iniziale - crea la cartella data se non esiste
     */
    static {
        File dir = new File(DIR_DATA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        copiaCSVSeNecessario();
    }
    
    /**
     * Copia il file CSV nella cartella data se necessario
     */
    private static void copiaCSVSeNecessario() {
        File cartellaData = new File(DIR_DATA);
        if (!cartellaData.exists()) {
            cartellaData.mkdirs();
        }

        File fileDestinazione = new File(FILE_CSV_PROIEZIONI);
        if (!fileDestinazione.exists()) {
            String[] percorsiPossibili = {
                "../data/proiezioni.csv",
                "../../data/proiezioni.csv",
                "proiezioni.csv"
            };

            for (String percorso : percorsiPossibili) {
                File fileOrigine = new File(percorso);
                if (fileOrigine.exists()) {
                    try (InputStream in = new FileInputStream(fileOrigine);
                         OutputStream out = new FileOutputStream(fileDestinazione)) {
                        byte[] buffer = new byte[1024];
                        int lunghezza;
                        while ((lunghezza = in.read(buffer)) > 0) {
                            out.write(buffer, 0, lunghezza);
                        }
                        break;
                    } catch (IOException e) {
                        System.err.println("Errore durante la copia del CSV: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * Trova il file CSV cercando in vari percorsi
     */
    private static File trovaFileCSV() {
        String[] percorsi = {
            FILE_CSV_PROIEZIONI,
            "../data/proiezioni.csv",
            "../../data/proiezioni.csv",
            "src/data/proiezioni.csv",
            "proiezioni.csv"
        };
        
        for (String p : percorsi) {
            File f = new File(p);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    /**
     * Carica le proiezioni dal file CSV (con fallback su TXT)
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        
        File csvFile = trovaFileCSV();
        
        if (csvFile != null) {
            System.out.println("Trovato file CSV: " + csvFile.getAbsolutePath());
            return caricaProiezioniDaCSV(csvFile);
        }
        
        System.err.println("File CSV non trovato. Provo a caricare dal file TXT...");
        
        // Fallback al file TXT
        File file = new File(FILE_PROIEZIONI);
        if (!file.exists()) {
            System.err.println("Nemmeno il file TXT è stato trovato!");
            return proiezioni;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int contatoreProiezioni = 1;
            
            while ((line = reader.readLine()) != null) {
                // Salta le linee vuote e i commenti
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                try {
                    String[] parti = line.split(";");
                    if (parti.length < 8) {
                        continue;
                    }
                    
                    String titolo = parti[0].trim();
                    String genere = parti[1].trim();
                    String regista = parti[2].trim();
                    int anno = Integer.parseInt(parti[3].trim());
                    int durata = Integer.parseInt(parti[4].trim());
                    int etaMinima = Integer.parseInt(parti[5].trim());
                    LocalDateTime dataOra = LocalDateTime.parse(parti[6].trim(), DATETIME_FORMATTER);
                    double costoBiglietto = Double.parseDouble(parti[7].trim().replace(",", "."));
                    
                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    Proiezione proiezione = new Proiezione(contatoreProiezioni++, film, dataOra, costoBiglietto);
                    proiezioni.add(proiezione);
                } catch (Exception e) {
                    System.err.println("Errore nel parsing: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura: " + e.getMessage());
        }
        
        return proiezioni;
    }
    
    /**
     * Carica le proiezioni da file CSV
     */
    private static List<Proiezione> caricaProiezioniDaCSV(File csvFile) {
        List<Proiezione> proiezioni = new ArrayList<>();
        DateTimeFormatter csvDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int contatoreProiezioni = 1;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean primaLinea = true;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Salta l'header
                if (primaLinea) {
                    primaLinea = false;
                    if (line.toLowerCase().contains("titolo") || line.toLowerCase().contains("data_ora")) {
                        continue;
                    }
                }
                
                try {
                    String[] parti = parseCSVLine(line);
                    
                    if (parti.length < 8) {
                        continue;
                    }
                    
                    String dataStr = pulisciCampo(parti[0]);
                    String titolo = pulisciCampo(parti[1]);
                    String genere = pulisciCampo(parti[2]);
                    String regista = pulisciCampo(parti[3]);
                    int anno = Integer.parseInt(pulisciCampo(parti[4]));
                    int durata = Integer.parseInt(pulisciCampo(parti[5]));
                    int etaMinima = Integer.parseInt(pulisciCampo(parti[6]));
                    double costoBiglietto = Double.parseDouble(pulisciCampo(parti[7]).replace(",", "."));
                    
                    // Parsing della data
                    LocalDateTime dataOra;
                    if (dataStr.contains("T")) {
                        dataOra = LocalDateTime.parse(dataStr);
                    } else {
                        dataOra = LocalDateTime.parse(dataStr, csvDateFormatter);
                    }
                    
                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    Proiezione proiezione = new Proiezione(contatoreProiezioni++, film, dataOra, costoBiglietto);
                    proiezioni.add(proiezione);
                    
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della riga: " + line);
                }
            }
            
            System.out.println("Caricate " + proiezioni.size() + " proiezioni dal CSV");
            
        } catch (IOException e) {
            System.err.println("Errore nella lettura del CSV: " + e.getMessage());
        }
        
        return proiezioni;
    }
    
    /**
     * Rimuove virgolette e spazi da un campo CSV
     */
    private static String pulisciCampo(String campo) {
        if (campo == null) {
            return "";
        }
        
        String pulito = campo.trim();
        
        // Rimuove le virgolette
        if (pulito.startsWith("\"") && pulito.endsWith("\"") && pulito.length() >= 2) {
            pulito = pulito.substring(1, pulito.length() - 1).trim();
        }
        
        return pulito.replace("\"", "").trim();
    }
    
    /**
     * Splitta una riga CSV gestendo le virgolette
     */
    private static String[] parseCSVLine(String line) {
        List<String> risultato = new ArrayList<>();
        StringBuilder campoCorrente = new StringBuilder();
        boolean dentroVirgolette = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                dentroVirgolette = !dentroVirgolette;
            } else if (c == ',' && !dentroVirgolette) {
                risultato.add(campoCorrente.toString());
                campoCorrente = new StringBuilder();
            } else {
                campoCorrente.append(c);
            }
        }
        
        risultato.add(campoCorrente.toString());
        return risultato.toArray(new String[0]);
    }
    
    /**
     * Salva le proiezioni nel file TXT
     */
    public static void salvaProiezioni(List<Proiezione> proiezioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            writer.write("# Formato: titolo;genere;regista;anno;durata;etaMinima;dataOra;costo\n");
            
            for (Proiezione p : proiezioni) {
                Film f = p.getFilm();
                String linea = String.format("%s;%s;%s;%d;%d;%d;%s;%.2f\n",
                    f.getTitolo(), f.getGenere(), f.getRegista(), f.getAnno(),
                    f.getDurata(), f.getEtaMinimaPubblico(),
                    p.getDataOraProiezione().format(DATETIME_FORMATTER),
                    p.getCostoBiglietto());
                writer.write(linea);
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio: " + e.getMessage());
        }
    }
    
    /**
     * Carica tutti gli utenti dal file
     */
    public static List<Utente> caricaUtenti() {
        List<Utente> utenti = new ArrayList<>();
        File file = new File(FILE_UTENTI);
        
        if (!file.exists()) {
            creaUtentiDefault(utenti);
            salvaUtenti(utenti);
            return utenti;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                try {
                    String[] parti = line.split(";");
                    if (parti.length < 7) {
                        continue;
                    }
                    
                    String nome = parti[0].trim();
                    String cognome = parti[1].trim();
                    String username = parti[2].trim();
                    String password = parti[3].trim();
                    String dataNascitaStr = parti[4].trim();
                    String luogo = parti[5].trim();
                    String ruolo = parti[6].trim();
                    
                    LocalDate dataNascita = null;
                    if (!dataNascitaStr.isEmpty() && !dataNascitaStr.equals("null")) {
                        dataNascita = LocalDate.parse(dataNascitaStr, DATE_FORMATTER);
                    }
                    
                    Utente utente = creaUtenteDalRuolo(nome, cognome, username, password, 
                                                       dataNascita, luogo, ruolo);
                    if (utente != null) {
                        utenti.add(utente);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel parsing dell'utente: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura: " + e.getMessage());
        }
        
        if (utenti.isEmpty()) {
            creaUtentiDefault(utenti);
            salvaUtenti(utenti);
        }
        
        return utenti;
    }
    
    /**
     * Crea gli utenti di default
     */
    private static void creaUtentiDefault(List<Utente> utenti) {
        // Proiezionisti
        utenti.add(new Proiezionista("Marco", "Rossi", "mrossi", "password123", 
            LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Proiezionista("Elena", "Bianchi", "ebianchi", "password123",
            LocalDate.of(1988, 8, 22), "Roma"));
        
        // Bigliettai
        utenti.add(new Bigliettaio("Giovanni", "Verdi", "gverdi", "password123",
            LocalDate.of(1990, 3, 10), "Napoli"));
        utenti.add(new Bigliettaio("Francesca", "Neri", "fneri", "password123",
            LocalDate.of(1992, 7, 18), "Torino"));
        utenti.add(new Bigliettaio("Andrea", "Galli", "agalli", "password123",
            LocalDate.of(1995, 2, 25), "Firenze"));
        utenti.add(new Bigliettaio("Sara", "Ferrari", "sferrari", "password123",
            LocalDate.of(1993, 11, 30), "Bologna"));
        utenti.add(new Bigliettaio("Luca", "Conti", "lconti", "password123",
            LocalDate.of(1991, 6, 12), "Genova"));
    }
    
    /**
     * Crea un utente dal ruolo specificato
     */
    private static Utente creaUtenteDalRuolo(String nome, String cognome, String username, 
                                             String password, LocalDate dataNascita, 
                                             String luogo, String ruolo) {
        try {
            if (ruolo.equalsIgnoreCase("cliente")) {
                return new Cliente(nome, cognome, username, "temp", dataNascita, luogo);
            }
            
            if (ruolo.equalsIgnoreCase("proiezionista")) {
                return new Proiezionista(nome, cognome, username, "temp", dataNascita, luogo);
            }
            
            if (ruolo.equalsIgnoreCase("bigliettaio")) {
                return new Bigliettaio(nome, cognome, username, "temp", dataNascita, luogo);
            }
        } catch (Exception e) {
            return null;
        }
        
        return null;
    }
    
    /**
     * Salva gli utenti nel file
     */
    public static void salvaUtenti(List<Utente> utenti) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
            writer.write("# Formato: nome;cognome;username;password;dataNascita;luogo;ruolo\n");
            
            for (Utente u : utenti) {
                String dataNascita = u.getDataNascita() != null ? 
                    u.getDataNascita().format(DATE_FORMATTER) : "";
                    
                String linea = String.format("%s;%s;%s;%s;%s;%s;%s\n",
                    u.getNome(), u.getCognome(), u.getUsername(), "password123",
                    dataNascita, u.getLuogoDomicilio(), u.getRuolo());
                writer.write(linea);
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio: " + e.getMessage());
        }
    }
    
    /**
     * Carica le prenotazioni dal file
     */
    public static List<Prenotazione> caricaPrenotazioni(List<Utente> utenti, List<Proiezione> proiezioni) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        File file = new File(FILE_PRENOTAZIONI);
        
        if (!file.exists()) {
            return prenotazioni;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                try {
                    String[] parti = line.split(";");
                    if (parti.length < 3) {
                        continue;
                    }
                    
                    String usernameCliente = parti[0].trim();
                    int idProiezione = Integer.parseInt(parti[1].trim());
                    int numeroBiglietti = Integer.parseInt(parti[2].trim());
                    
                    // Cerca il cliente
                    Cliente cliente = null;
                    for (Utente u : utenti) {
                        if (u instanceof Cliente && u.getUsername().equals(usernameCliente)) {
                            cliente = (Cliente) u;
                            break;
                        }
                    }
                    
                    // Cerca la proiezione
                    Proiezione proiezione = null;
                    for (Proiezione p : proiezioni) {
                        if (p.getId() == idProiezione) {
                            proiezione = p;
                            break;
                        }
                    }
                    
                    // Se entrambi esistono, crea la prenotazione
                    if (cliente != null && proiezione != null) {
                        Prenotazione pre = new Prenotazione(cliente, proiezione, numeroBiglietti);
                        prenotazioni.add(pre);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel parsing: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura: " + e.getMessage());
        }
        
        return prenotazioni;
    }
    
    /**
     * Salva le prenotazioni nel file
     */
    public static void salvaPrenotazioni(List<Prenotazione> prenotazioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRENOTAZIONI))) {
            writer.write("# Formato: usernameCliente;idProiezione;numeroBiglietti\n");
            
            for (Prenotazione p : prenotazioni) {
                String linea = String.format("%s;%d;%d\n",
                    p.getCliente().getUsername(),
                    p.getProiezione().getId(),
                    p.getNumeroBiglietti());
                writer.write(linea);
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio: " + e.getMessage());
        }
    }
}