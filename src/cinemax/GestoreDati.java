

import java.io.*;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe che gestisce il salvataggio e il caricamento dei dati
 * @author Andrea
 * @version 1.0
 */
public class GestoreDati {
    private static final String DATA_DIR = "src/cinemax/data";
    private static final String LEGACY_DATA_DIR = "data";
    private static final String FILE_PROIEZIONI = "proiezioni.txt";
    private static final String FILE_PROIEZIONI_CSV = "proiezioni (1).csv";
    private static final String FILE_UTENTI = "utenti.txt";
    private static final String FILE_PRENOTAZIONI = "prenotazioni.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter CSV_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        File dir = getDataDirectory();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static File getDataDirectory() {
        File preferred = resolveExistingDataDirectory();
        if (preferred != null) {
            preferred.mkdirs();
            return preferred;
        }

        File fallback = new File(getProjectRoot(), DATA_DIR);
        fallback.mkdirs();
        return fallback;
    }

    private static File resolveExistingDataDirectory() {
        List<File> candidates = new ArrayList<>();
        candidates.add(new File(System.getProperty("user.dir"), DATA_DIR).getAbsoluteFile());
        candidates.add(new File(System.getProperty("user.dir"), LEGACY_DATA_DIR).getAbsoluteFile());
        candidates.add(new File(getProjectRoot(), DATA_DIR).getAbsoluteFile());
        candidates.add(new File(getProjectRoot(), LEGACY_DATA_DIR).getAbsoluteFile());

        for (File candidate : candidates) {
            if (candidate.exists() && candidate.isDirectory()) {
                return candidate;
            }
        }

        return null;
    }

    private static File getProjectRoot() {
        List<File> candidates = new ArrayList<>();
        candidates.add(new File(System.getProperty("user.dir")).getAbsoluteFile());

        try {
            CodeSource codeSource = GestoreDati.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                File location = new File(codeSource.getLocation().toURI()).getAbsoluteFile();
                candidates.add(location);
                if (location.getName().equals("bin")) {
                    candidates.add(location.getParentFile());
                }
                File parent = location.getParentFile();
                if (parent != null) {
                    candidates.add(parent);
                }
            }
        } catch (URISyntaxException e) {
            // ignore and continue with fallbacks
        }

        for (File candidate : candidates) {
            if (candidate == null) {
                continue;
            }
            File dataDir = new File(candidate, DATA_DIR);
            if (dataDir.exists() && dataDir.isDirectory()) {
                return candidate;
            }
            File legacyDir = new File(candidate, LEGACY_DATA_DIR);
            if (legacyDir.exists() && legacyDir.isDirectory()) {
                return candidate;
            }
            File srcDir = new File(candidate, "src");
            if (srcDir.exists() && srcDir.isDirectory()) {
                return candidate;
            }
        }

        return new File(System.getProperty("user.dir")).getAbsoluteFile();
    }

    private static File resolveDataFile(String fileName) {
        List<File> candidates = new ArrayList<>();
        candidates.add(new File(getDataDirectory(), fileName).getAbsoluteFile());
        candidates.add(new File(getProjectRoot(), DATA_DIR + File.separator + fileName).getAbsoluteFile());
        candidates.add(new File(getProjectRoot(), LEGACY_DATA_DIR + File.separator + fileName).getAbsoluteFile());
        candidates.add(new File(System.getProperty("user.dir"), DATA_DIR + File.separator + fileName).getAbsoluteFile());
        candidates.add(new File(System.getProperty("user.dir"), LEGACY_DATA_DIR + File.separator + fileName).getAbsoluteFile());

        for (File candidate : candidates) {
            if (candidate.exists()) {
                return candidate;
            }
        }

        return new File(getDataDirectory(), fileName).getAbsoluteFile();
    }

    private static List<Proiezione> caricaProiezioniDaCsv(File file) {
        List<Proiezione> proiezioni = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int proiezioneCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                if (line.startsWith("data_ora_proiezione")) continue;

                try {
                    String[] parts = parseCsvLine(line);
                    if (parts.length < 8) continue;

                    String dataOraStr = parts[0].trim();
                    String titolo = parts[1].trim();
                    String genere = parts[2].trim();
                    String regista = parts[3].trim();
                    int anno = Integer.parseInt(parts[4].trim());
                    int durata = Integer.parseInt(parts[5].trim());
                    int etaMinima = Integer.parseInt(parts[6].trim());
                    double costoBiglietto = Double.parseDouble(parts[7].trim());

                    LocalDateTime dataOra = LocalDateTime.parse(dataOraStr, CSV_DATETIME_FORMATTER);
                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    proiezioni.add(new Proiezione(proiezioneCount++, film, dataOra, costoBiglietto));
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della proiezione CSV: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura delle proiezioni CSV: " + e.getMessage());
        }

        return proiezioni;
    }

    private static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }
    
    /**
     * Carica tutte le proiezioni dal file
     * @return lista di proiezioni
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        File file = resolveDataFile(FILE_PROIEZIONI);
        
        if (!file.exists()) {
            File csvFile = resolveDataFile(FILE_PROIEZIONI_CSV);
            if (csvFile.exists()) {
                return caricaProiezioniDaCsv(csvFile);
            }
            return proiezioni;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int proiezioneCount = 0;
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
                    double costoBiglietto = Double.parseDouble(parts[7].trim());
                    
                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    Proiezione proiezione = new Proiezione(proiezioneCount++, film, dataOra, costoBiglietto);
                    proiezioni.add(proiezione);
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della proiezione: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura delle proiezioni: " + e.getMessage());
        }
        
        return proiezioni;
    }
    
    /**
     * Salva le proiezioni nel file
     * @param proiezioni lista di proiezioni da salvare
     */
    public static void salvaProiezioni(List<Proiezione> proiezioni) {
        File file = resolveDataFile(FILE_PROIEZIONI);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
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
        File file = resolveDataFile(FILE_UTENTI);
        
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
                    if (parts.length < 8) continue;
                    
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
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param username username
     * @param passwordHash password hashata
     * @param dataNascita data di nascita
     * @param luogo luogo domicilio
     * @param ruolo ruolo dell'utente
     * @return utente creato o null
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
        File file = resolveDataFile(FILE_UTENTI);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# Formato: nome;cognome;username;passwordHash;dataNascita;luogo;ruolo\n");
            for (Utente u : utenti) {
                String dataNascita = u.getDataNascita() != null ? 
                    u.getDataNascita().format(DATE_FORMATTER) : "";
                String line = String.format("%s;%s;%s;%s;%s;%s;%s\n",
                    u.getNome(), u.getCognome(), u.getUsername(), u.hashCode(),
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
        File file = resolveDataFile(FILE_PRENOTAZIONI);
        
        if (!file.exists()) {
            return prenotazioni;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                try {
                    String[] parts = line.split(";");
                    if (parts.length < 4) continue;
                    
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
        File file = resolveDataFile(FILE_PRENOTAZIONI);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
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
