package cinemax;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe di utilità che gestisce la persistenza dei dati del sistema CineMax.
 * 
 * @author Andrea
 * @version 1.3
 */
public class GestoreDati {

    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.csv";
    private static final String FILE_CSV_PROIEZIONI = "data/proiezioni.csv";
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    

    /**
     * Carica le proiezioni da file di testo o dal CSV iniziale.
     * 
     * @return la lista delle proiezioni caricate
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        File fileTxt = new File(FILE_PROIEZIONI);

        // 1. Prova a caricare dal file strutturato TXT (comportamento standard)
        if (fileTxt.exists()) {
            return caricaProiezioniDaTxt(fileTxt);
        }

        // 2. Se il TXT non esiste, importa i dati dal CSV e crea il TXT per i futuri avvii
        File csvFile = new File(FILE_CSV_PROIEZIONI);
        if (csvFile.exists()) {
            proiezioni = caricaProiezioniDaCSV(csvFile);
            salvaProiezioni(proiezioni);
            return proiezioni;
        }

        return proiezioni;
    }

    /**
     * Carica le proiezioni dal file di testo strutturato.
     * 
     * @param file il file di testo delle proiezioni
     * @return la lista delle proiezioni
     */
    private static List<Proiezione> caricaProiezioniDaTxt(File file) {
        List<Proiezione> proiezioni = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea; 
            while ((linea = reader.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) { continue; }

                String[] parti = linea.split(";"); 
                if (parti.length < 9) { continue; }

                int id = Integer.parseInt(parti[0].trim());
                String titolo = parti[1].trim();
                String genere = parti[2].trim();
                String regista = parti[3].trim();
                int anno = Integer.parseInt(parti[4].trim());
                int durata = Integer.parseInt(parti[5].trim());
                int etaMinima = Integer.parseInt(parti[6].trim());
                
                //questo serve per gestire sia il formato con T che senza T nella data, ossia "2024-06-15T20:30" o "2024-06-15 20:30"
                LocalDateTime dataOra;
                String dataStr = parti[7].trim();
                if (dataStr.contains("T")) {
                    dataOra = LocalDateTime.parse(dataStr); // Formato ISO standard
                } else {
                    dataOra = LocalDateTime.parse(dataStr, DATETIME_FORMATTER); // Formato personalizzato
                }
                
                double costo = Double.parseDouble(parti[8].trim().replace(",", "."));

                Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                Proiezione proiezione = new Proiezione(id, film, dataOra, costo);
                proiezioni.add(proiezione);
            }
        } catch (Exception e) {
            System.out.println("Errore nel leggere le proiezioni TXT: " + e.getMessage());
        }
        return proiezioni;
    }

    
    
    
    
    
    /**
     * Esegue il parsing del file CSV delle proiezioni.
     * 
     * @param csvFile il file CSV
     * @return la lista delle proiezioni estratte
     */
    private static List<Proiezione> caricaProiezioniDaCSV(File csvFile) {
        List<Proiezione> proiezioni = new ArrayList<>();
        DateTimeFormatter csvDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int contatoreProiezioni = 1;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean primaLinea = true;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) { continue; }

                if (primaLinea) {
                    primaLinea = false;
                    if (line.toLowerCase().contains("titolo") || line.toLowerCase().contains("data_ora")) {
                        continue;
                    }
                }
                
                try {
                    String[] parti = parseCSVLine(line);
                    if (parti.length < 8) { continue; }
                    
                    String dataStr = pulisciCampo(parti[0]);
                    String titolo = pulisciCampo(parti[1]);
                    String genere = pulisciCampo(parti[2]);
                    String regista = pulisciCampo(parti[3]);
                    int anno = Integer.parseInt(pulisciCampo(parti[4]));
                    int durata = Integer.parseInt(pulisciCampo(parti[5]));
                    int etaMinima = Integer.parseInt(pulisciCampo(parti[6]));
                    double costoBiglietto = Double.parseDouble(pulisciCampo(parti[7]).replace(",", "."));
                    
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
        } catch (IOException e) {
            System.err.println("Errore nella lettura del CSV: " + e.getMessage());
        }
        return proiezioni;
    }
    
    /**
     * Rimuove apici e spazi superflui da un campo CSV.
     * 
     * @param campo la stringa del campo
     * @return la stringa ripulita
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
     * Effettua il parsing di una singola riga CSV gestendo le virgolette.
     * 
     * @param line la riga di testo
     * @return un array di stringhe con i campi separati
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
     * Salva la lista delle proiezioni su file di testo.
     * 
     * @param proiezioni la lista delle proiezioni da salvare
     */
    public static void salvaProiezioni(List<Proiezione> proiezioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            writer.write("# Formato: id;titolo;genere;regista;anno;durata;etaMinima;dataOra;costo\n");
            for (Proiezione p : proiezioni) {
                Film f = p.getFilm();
                String dataFormattata = p.getDataOraProiezione().format(DATETIME_FORMATTER);
                String rigaDaSalvare = p.getId() + ";" + 
                                       f.getTitolo() + ";" + f.getGenere() + ";" + 
                                       f.getRegista() + ";" + f.getAnno() + ";" + 
                                       f.getDurata() + ";" + f.getEtaMinimaPubblico() + ";" + 
                                       dataFormattata + ";" + p.getCostoBiglietto() + "\n";
                writer.write(rigaDaSalvare);
            }
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio proiezioni: " + e.getMessage());
        }
    }

    /**
     * Carica gli utenti da file, o li inizializza con i valori di default se il file non esiste.
     * 
     * @return la lista degli utenti caricati
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
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) { continue; }

                String[] parti = linea.split(";");
                if (parti.length < 7) { continue; }

                String nome = parti[0];
                String cognome = parti[1];
                String username = parti[2];
                String passwordHash = parti[3];
                String dataStr = parti[4];
                String luogo = parti[5];
                String ruolo = parti[6];

                LocalDate dataNascita = null;
                if (!dataStr.equals("") && !dataStr.equals("null")) {
                    dataNascita = LocalDate.parse(dataStr, DATE_FORMATTER);
                }

                Utente nuovoUtente = null;
                if (ruolo.equals("cliente")) {
                    nuovoUtente = new Cliente(nome, cognome, username, "", dataNascita, luogo);
                } else if (ruolo.equals("proiezionista")) {
                    nuovoUtente = new Proiezionista(nome, cognome, username, "", dataNascita, luogo);
                } else if (ruolo.equals("bigliettaio")) {
                    nuovoUtente = new Bigliettaio(nome, cognome, username, "", dataNascita, luogo);
                }

                if (nuovoUtente != null) {
                    nuovoUtente.setPasswordHashForced(passwordHash);
                    utenti.add(nuovoUtente);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura utenti: " + e.getMessage());
        }

        return utenti;
    }

    /**
     * Inietta gli utenti di default (5 bigliettai e 2 proiezionisti) nella lista.
     * 
     * @param utenti la lista di utenti da popolare
     */
    private static void creaUtentiDefault(List<Utente> utenti) {
        utenti.add(new Bigliettaio("Luca", "Bianchi", "lbianchi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Bigliettaio("Marco", "Rossi", "mrossi", "password123", LocalDate.of(1988, 8, 22), "Roma"));
        utenti.add(new Bigliettaio("Sofia", "Verdi", "sverdi", "password123", LocalDate.of(1992, 1, 10), "Napoli"));
        utenti.add(new Bigliettaio("Andrea", "Galli", "agalli", "password123", LocalDate.of(1995, 11, 5), "Firenze"));
        utenti.add(new Bigliettaio("Giovanni", "Neri", "gneri", "password123", LocalDate.of(1990, 3, 10), "Torino"));
        
        utenti.add(new Proiezionista("Mario", "Draghi", "mdraghi", "password123", LocalDate.of(1975, 4, 20), "Bologna"));
        utenti.add(new Proiezionista("Giulia", "Conte", "gconte", "password123", LocalDate.of(1982, 7, 14), "Genova"));
    }

    /**
     * Salva la lista degli utenti su file di testo cifrando o preservando gli hash password.
     * 
     * @param utenti la lista degli utenti da salvare
     */
    public static void salvaUtenti(List<Utente> utenti) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
            writer.write("# Formato: nome;cognome;username;passwordHash;dataNascita;luogo;ruolo\n");

            for (Utente u : utenti) {
                String dataTesto = "";
                if (u.getDataNascita() != null) {
                    dataTesto = u.getDataNascita().format(DATE_FORMATTER);
                }
                
                String rigaDaSalvare = u.getNome() + ";" + 
                                       u.getCognome() + ";" + 
                                       u.getUsername() + ";" + 
                                       u.getPasswordHash() + ";" + 
                                       dataTesto + ";" + 
                                       u.getLuogoDomicilio() + ";" + 
                                       u.getRuolo() + "\n";

                writer.write(rigaDaSalvare);
            }
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio utenti: " + e.getMessage());
        }
    }

    /**
     * Carica le prenotazioni dal file CSV formattato.
     * 
     * @param utenti la lista degli utenti registrati (per associare il cliente)
     * @param proiezioni la lista delle proiezioni (per associare lo spettacolo)
     * @return la lista delle prenotazioni caricate
     */
    public static List<Prenotazione> caricaPrenotazioni(List<Utente> utenti, List<Proiezione> proiezioni) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        File file = new File(FILE_PRENOTAZIONI);

        if (!file.exists()) { return prenotazioni; }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) { continue; }

                String[] parti = linea.split(",");
                if (parti.length < 3) { continue; }

                String usernameCercato = parti[0].trim();
                int idProiezioneCercata = Integer.parseInt(parti[1].trim());
                int numeroBiglietti = Integer.parseInt(parti[2].trim());

                Cliente clienteTrovato = null;
                for (Utente u : utenti) {
                    if (u.getRuolo().equals("cliente") && u.getUsername().equals(usernameCercato)) {
                        clienteTrovato = (Cliente) u;
                        break; 
                    }
                }

                Proiezione proiezioneTrovata = null;
                for (Proiezione p : proiezioni) {
                    if (p.getId() == idProiezioneCercata) {
                        proiezioneTrovata = p;
                        break; 
                    }
                }

                if (clienteTrovato != null && proiezioneTrovata != null) {
                    Prenotazione p = new Prenotazione(clienteTrovato, proiezioneTrovata, numeroBiglietti);
                    prenotazioni.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura del CSV prenotazioni: " + e.getMessage());
        }
        return prenotazioni;
    }

    /**
     * Salva la lista delle prenotazioni sul file CSV dedicato.
     * 
     * @param prenotazioni la lista delle prenotazioni da salvare
     */
    public static void salvaPrenotazioni(List<Prenotazione> prenotazioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRENOTAZIONI))) {
            writer.write("# Formato CSV: usernameCliente,idProiezione,numeroBiglietti\n");
            
            for (Prenotazione p : prenotazioni) {
                String rigaDaSalvare = p.getCliente().getUsername() + "," + 
                                       p.getProiezione().getId() + "," + 
                                       p.getNumeroBiglietti() + "\n";
                writer.write(rigaDaSalvare);
            }
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio del CSV prenotazioni: " + e.getMessage());
        }
    }
}