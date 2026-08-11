

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe che gestisce il salvataggio e il caricamento dei dati
 * @author Andrea
 * @version 1.1
 */
public class GestoreDati {
    private static final String DIR_DATA = "data";
    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";
    private static final String FILE_CSV_PROIEZIONI = "data/proiezioni.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    static {
        File dir = new File(DIR_DATA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Copia il CSV nella cartella data/ se non esiste già
        copiaCSVSeNecessario();
    }
    
    /**
     * Copia il file CSV nella cartella data/ se viene trovato in un percorso alternativo
     */
    private static void copiaCSVSeNecessario() {
        File cartellaData = new File(DIR_DATA);
        if (!cartellaData.exists()) {
            cartellaData.mkdirs();
        }

        File fileDestinazione = new File(FILE_CSV_PROIEZIONI);
        if (!fileDestinazione.exists()) {
            File[] originiPossibili = {
                new File("../data/proiezioni.csv"),
                new File("../../data/proiezioni.csv"),
                new File("proiezioni.csv")
            };

            for (File fileOrigine : originiPossibili) {
                if (fileOrigine.exists()) {
                    try (InputStream in = new FileInputStream(fileOrigine);
                         OutputStream out = new FileOutputStream(fileDestinazione)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                        break;
                    } catch (IOException e) {
                        System.err.println("Errore durante la copia del file CSV: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * Trova il file CSV cercando in diversi percorsi relativi
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
     * Carica tutte le proiezioni dal file CSV (con fallback sul file TXT)
     * @return lista di proiezioni
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        
        File csvFile = trovaFileCSV();
        
        if (csvFile != null) {
            System.out.println("📂 Trovato file CSV in: " + csvFile.getAbsolutePath());
            return caricaProiezioniDaCSV(csvFile);
        }
        
        System.err.println("⚠ File CSV non trovato nei percorsi attesi.");
        System.err.println("   Provo a caricare dal file TXT...");
        
        // Fallback al file TXT
        File file = new File(FILE_PROIEZIONI);
        if (!file.exists()) {
            System.err.println("❌ Nemmeno il file TXT è stato trovato!");
            return proiezioni;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int proiezioneCount = 1;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                try {
                    String[] parts = line.split(";");
                    if (parts.length < 8) continue;
                    
                    String titolo = parts[0].trim();
                    String genere = parts[1].trim();
                    String regista = parts[2].trim();
                    int anno = Integer.parseInt(parts[3].trim());
                    int durata = Integer.parseInt(parts[4].trim());
                    int etaMinima = Integer.parseInt(parts[5].trim());
                    LocalDateTime dataOra = LocalDateTime.parse(parts[6].trim(), DATETIME_FORMATTER);
                    double costoBiglietto = Double.parseDouble(parts[7].trim().replace(",", "."));
                    
                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    Proiezione proiezione = new Proiezione(proiezioneCount++, film, dataOra, costoBiglietto);
                    proiezioni.add(proiezione);
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della proiezione TXT: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura delle proiezioni TXT: " + e.getMessage());
        }
        
        return proiezioni;
    }
    
    /**
     * Carica le proiezioni da file CSV
     * Formato: data_ora_proiezione,titolo_film,genere,regista,anno,durata_minuti,eta_minima,prezzo_biglietto
     * @param csvFile file CSV da leggere
     * @return lista di proiezioni
     */
    private static List<Proiezione> caricaProiezioniDaCSV(File csvFile) {
        List<Proiezione> proiezioni = new ArrayList<>();
        DateTimeFormatter csvDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int proiezioneCount = 1;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean primaLinea = true;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;

                // Gestione header
                if (primaLinea) {
                    primaLinea = false;
                    if (line.toLowerCase().contains("titolo") || line.toLowerCase().contains("data_ora")) {
                        continue;
                    }
                }
                
                try {
                    // Parse CSV gestendo le virgolette
                    String[] parts = parseCSVLine(line);
                    
                    if (parts.length < 8) {
                        System.err.println("Riga CSV con colonne insufficienti: " + line);
                        continue;
                    }
                    
                    // Pulizia di ciascun campo da virgolette ed eventuali spazi
                    String dataStr   = pulisciCampo(parts[0]);
                    String titolo    = pulisciCampo(parts[1]);
                    String genere    = pulisciCampo(parts[2]);
                    String regista   = pulisciCampo(parts[3]);
                    int anno         = Integer.parseInt(pulisciCampo(parts[4]));
                    int durata       = Integer.parseInt(pulisciCampo(parts[5]));
                    int etaMinima    = Integer.parseInt(pulisciCampo(parts[6]));
                    double costoBiglietto = Double.parseDouble(pulisciCampo(parts[7]).replace(",", "."));
                    
                    // Parsing della data
                    LocalDateTime dataOra;
                    if (dataStr.contains("T")) {
                        dataOra = LocalDateTime.parse(dataStr);
                    } else {
                        dataOra = LocalDateTime.parse(dataStr, csvDateFormatter);
                    }
                    
                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    Proiezione proiezione = new Proiezione(proiezioneCount++, film, dataOra, costoBiglietto);
                    proiezioni.add(proiezione);
                    
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della riga CSV: " + line);
                    System.err.println("Dettaglio errore: " + e.getMessage());
                }
            }
            
            System.out.println("✓ Caricate con successo " + proiezioni.size() + " proiezioni dal CSV");
            
        } catch (IOException e) {
            System.err.println("Errore nella lettura del CSV: " + e.getMessage());
        }
        
        return proiezioni;
    }
    
    /**
     * Rimuove virgolette doppie e spazi da un campo estratto dal CSV
     */
    private static String pulisciCampo(String campo) {
        if (campo == null) return "";
        String pulito = campo.trim();
        if (pulito.startsWith("\"") && pulito.endsWith("\"") && pulito.length() >= 2) {
            pulito = pulito.substring(1, pulito.length() - 1).trim();
        }
        return pulito.replace("\"", "").trim();
    }
    
    /**
     * Parse una riga CSV gestendo le virgolette e gli spazi
     * @param line la riga CSV
     * @return array di campi
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        result.add(currentField.toString());
        return result.toArray(new String[0]);
    }
    
    /**
     * Salva le proiezioni nel file
     * @param proiezioni lista di proiezioni da salvare
     */
    public static void salvaProiezioni(List<Proiezione> proiezioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            writer.write("# Formato: titolo;genere;regista;anno;durata;etaMinima;dataOra;costo\n");
            for (Proiezione p : proiezioni) {
                Film f = p.getFilm();
                String line = String.format("%s;%s;%s;%d;%d;%d;%s;%.2f\n",
                    f.getTitolo(), f.getGenere(), f.getRegista(), f.getAnno(),
                    f.getDurata(), f.getEtaMinimaPubblico(),
                    p.getDataOraProiezione().format(DATETIME_FORMATTER),
                    p.getCostoBiglietto());
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio delle proiezioni: " + e.getMessage());
        }
    }
    
    /**
     * Carica tutti gli utenti dal file
     * @return lista di utenti
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
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                try {
                    String[] parts = line.split(";");
                    if (parts.length < 7) continue;
                    
                    String nome = parts[0].trim();
                    String cognome = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim(); // Già hashata
                    String dataNascitaStr = parts[4].trim();
                    String luogo = parts[5].trim();
                    String ruolo = parts[6].trim();
                    
                    LocalDate dataNascita = null;
                    if (!dataNascitaStr.isEmpty() && !dataNascitaStr.equals("null")) {
                        dataNascita = LocalDate.parse(dataNascitaStr, DATE_FORMATTER);
                    }
                    
                    Utente utente = creaUtenteDalRuolo(nome, cognome, username, password, dataNascita, luogo, ruolo);
                    if (utente != null) {
                        utenti.add(utente);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel parsing dell'utente: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura degli utenti: " + e.getMessage());
        }
        
        if (utenti.isEmpty()) {
            creaUtentiDefault(utenti);
            salvaUtenti(utenti);
        }
        
        return utenti;
    }
    
    /**
     * Crea gli utenti di default (2 proiezionisti e 5 bigliettai)
     * @param utenti lista a cui aggiungere gli utenti
     */
    private static void creaUtentiDefault(List<Utente> utenti) {
        utenti.add(new Proiezionista("Marco", "Rossi", "mrossi", "password123", 
            LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Proiezionista("Elena", "Bianchi", "ebianchi", "password123",
            LocalDate.of(1988, 8, 22), "Roma"));
        
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
     * Crea un utente dal ruolo specificato con la password già hashata
     */
    private static Utente creaUtenteDalRuolo(String nome, String cognome, String username, 
                                             String passwordHash, LocalDate dataNascita, 
                                             String luogo, String ruolo) {
        try {
            Utente utente = null;
            switch (ruolo.toLowerCase()) {
                case "cliente":
                    utente = new Cliente(nome, cognome, username, "temp", dataNascita, luogo);
                    break;
                case "proiezionista":
                    utente = new Proiezionista(nome, cognome, username, "temp", dataNascita, luogo);
                    break;
                case "bigliettaio":
                    utente = new Bigliettaio(nome, cognome, username, "temp", dataNascita, luogo);
                    break;
            }
            return utente;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Salva gli utenti nel file
     * @param utenti lista di utenti da salvare
     */
    public static void salvaUtenti(List<Utente> utenti) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
        writer.write("# Formato: nome;cognome;username;passwordHash;dataNascita;luogo;ruolo\n");
        for (Utente u : utenti) {
            String dataNascita = u.getDataNascita() != null ? 
                u.getDataNascita().format(DATE_FORMATTER) : "";
            // Sostituito u.hashCode() con la password corretta
            String line = String.format("%s;%s;%s;%s;%s;%s;%s\n",
                u.getNome(), u.getCognome(), u.getUsername(), "password123",
                dataNascita, u.getLuogoDomicilio(), u.getRuolo());
            writer.write(line);
        }
    } catch (IOException e) {
        System.err.println("Errore nel salvataggio degli utenti: " + e.getMessage());
    }
}
    
    /**
     * Carica tutte le prenotazioni dal file
     * @param utenti lista di utenti caricati
     * @param proiezioni lista di proiezioni caricate
     * @return lista di prenotazioni
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
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                try {
                    String[] parts = line.split(";");
                    if (parts.length < 3) continue;
                    
                    String usernameCliente = parts[0].trim();
                    int idProiezione = Integer.parseInt(parts[1].trim());
                    int numeroBiglietti = Integer.parseInt(parts[2].trim());
                    
                    Cliente cliente = null;
                    for (Utente u : utenti) {
                        if (u instanceof Cliente && u.getUsername().equals(usernameCliente)) {
                            cliente = (Cliente) u;
                            break;
                        }
                    }
                    
                    Proiezione proiezione = null;
                    for (Proiezione p : proiezioni) {
                        if (p.getId() == idProiezione) {
                            proiezione = p;
                            break;
                        }
                    }
                    
                    if (cliente != null && proiezione != null) {
                        Prenotazione pre = new Prenotazione(cliente, proiezione, numeroBiglietti);
                        prenotazioni.add(pre);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della prenotazione: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura delle prenotazioni: " + e.getMessage());
        }
        
        return prenotazioni;
    }
    
    /**
     * Salva le prenotazioni nel file
     * @param prenotazioni lista di prenotazioni da salvare
     */
    public static void salvaPrenotazioni(List<Prenotazione> prenotazioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRENOTAZIONI))) {
            writer.write("# Formato: usernameCliente;idProiezione;numeroBiglietti\n");
            for (Prenotazione p : prenotazioni) {
                String line = String.format("%s;%d;%d\n",
                    p.getCliente().getUsername(),
                    p.getProiezione().getId(),
                    p.getNumeroBiglietti());
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio delle prenotazioni: " + e.getMessage());
        }
    }
}