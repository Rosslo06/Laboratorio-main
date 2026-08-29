import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe che gestisce il salvataggio e il caricamento dei dati
 * Utilizza esclusivamente file TXT con separatore punto e virgola (;)
 */
public class GestoreDati {
    
    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ================= PROIEZIONI =================

    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        File file = new File(FILE_PROIEZIONI);
        
        if (!file.exists()) {
            System.err.println("File proiezioni.txt non trovato.");
            return proiezioni;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int contatoreProiezioni = 1;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                try {
                    String[] parti = line.split(";");
                    if (parti.length < 8) continue;
                    
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
                    System.err.println("Errore nel parsing proiezione: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura proiezioni: " + e.getMessage());
        }
        return proiezioni;
    }

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
            System.err.println("Errore salvataggio proiezioni: " + e.getMessage());
        }
    }

    // ================= UTENTI =================

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
                    String[] parti = line.split(";");
                    if (parti.length < 7) continue;
                    
                    String nome = parti[0].trim();
                    String cognome = parti[1].trim();
                    String username = parti[2].trim();
                    String password = parti[3].trim();
                    String dataNascitaStr = parti[4].trim();
                    String luogo = parti[5].trim();
                    String ruolo = parti[6].trim();
                    
                    LocalDate dataNascita = (!dataNascitaStr.isEmpty() && !dataNascitaStr.equals("null")) 
                        ? LocalDate.parse(dataNascitaStr, DATE_FORMATTER) : null;
                    
                    Utente utente = creaUtenteDalRuolo(nome, cognome, username, password, dataNascita, luogo, ruolo);
                    if (utente != null) utenti.add(utente);
                } catch (Exception e) {
                    System.err.println("Errore parsing utente: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura utenti: " + e.getMessage());
        }
        
        if (utenti.isEmpty()) {
            creaUtentiDefault(utenti);
            salvaUtenti(utenti);
        }
        return utenti;
    }
    
    private static void creaUtentiDefault(List<Utente> utenti) {
        utenti.add(new Proiezionista("Marco", "Rossi", "mrossi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Bigliettaio("Giovanni", "Verdi", "gverdi", "password123", LocalDate.of(1990, 3, 10), "Napoli"));
    }
    
    private static Utente creaUtenteDalRuolo(String nome, String cognome, String username, String password, LocalDate dataNascita, String luogo, String ruolo) {
        if (ruolo.equalsIgnoreCase("cliente")) return new Cliente(nome, cognome, username, "temp", dataNascita, luogo);
        if (ruolo.equalsIgnoreCase("proiezionista")) return new Proiezionista(nome, cognome, username, "temp", dataNascita, luogo);
        if (ruolo.equalsIgnoreCase("bigliettaio")) return new Bigliettaio(nome, cognome, username, "temp", dataNascita, luogo);
        return null;
    }
    
    public static void salvaUtenti(List<Utente> utenti) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
            writer.write("# Formato: nome;cognome;username;password;dataNascita;luogo;ruolo\n");
            for (Utente u : utenti) {
                String dataNascita = u.getDataNascita() != null ? u.getDataNascita().format(DATE_FORMATTER) : "";
                String linea = String.format("%s;%s;%s;%s;%s;%s;%s\n",
                    u.getNome(), u.getCognome(), u.getUsername(), "password123",
                    dataNascita, u.getLuogoDomicilio(), u.getRuolo());
                writer.write(linea);
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio utenti: " + e.getMessage());
        }
    }

    // ================= PRENOTAZIONI =================

    public static List<Prenotazione> caricaPrenotazioni(List<Utente> utenti, List<Proiezione> proiezioni) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        File file = new File(FILE_PRENOTAZIONI);
        
        if (!file.exists()) return prenotazioni;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                try {
                    String[] parti = line.split(";");
                    if (parti.length < 3) continue;
                    
                    String usernameCliente = parti[0].trim();
                    int idProiezione = Integer.parseInt(parti[1].trim());
                    int numeroBiglietti = Integer.parseInt(parti[2].trim());
                    
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
                        prenotazioni.add(new Prenotazione(cliente, proiezione, numeroBiglietti));
                    }
                } catch (Exception e) {
                    System.err.println("Errore parsing prenotazione: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura prenotazioni: " + e.getMessage());
        }
        return prenotazioni;
    }
    
    public static void salvaPrenotazioni(List<Prenotazione> prenotazioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRENOTAZIONI))) {
            writer.write("# Formato: usernameCliente;idProiezione;numeroBiglietti\n");
            for (Prenotazione p : prenotazioni) {
                String linea = String.format("%s;%d;%d\n",
                    p.getCliente().getUsername(), p.getProiezione().getId(), p.getNumeroBiglietti());
                writer.write(linea);
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio prenotazioni: " + e.getMessage());
        }
    }
}