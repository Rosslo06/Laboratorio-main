

import java.io.*;
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
    private static final String DIR_DATA = "data";
    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    static {
        File dir = new File(DIR_DATA);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    
    /**
     * Carica tutte le proiezioni dal file
     * @return lista di proiezioni
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        File file = new File(FILE_PROIEZIONI);
        
        if (!file.exists()) {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
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
