package cinemax;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe di utilità che gestisce la persistenza dei dati del sistema CineMax.
 * Fornisce metodi statici per caricare e salvare proiezioni, utenti e prenotazioni 
 * leggendo e scrivendo su file di testo posizionati nella cartella "data".
 * 
 * @author Andrea
 * @version 1.0
 */
public class GestoreDati {

    // Questi sono i percorsi dei file testuali dove salveremo i dati
    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";

    // Questi "formattatori" servono per dire a Java come leggere e scrivere le date
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // =========================================================
    // SEZIONE PROIEZIONI
    // =========================================================

    /**
     * Carica le proiezioni dal file di testo "data/proiezioni.txt" e le restituisce come lista in memoria.
     * Se il file non esiste (es. al primo avvio), restituisce una lista vuota.
     * 
     * @return Lista di oggetti Proiezione caricati e istanziati dal file.
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> proiezioni = new ArrayList<>();
        File file = new File(FILE_PROIEZIONI);

        // Se il file non esiste ancora (es. primo avvio), restituiamo una lista vuota
        if (!file.exists()) {
            return proiezioni;
        }

        // BufferedReader è uno strumento per leggere il file riga per riga in modo efficiente
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea; // Variabile temporanea per leggere ogni riga del file
            int contatoreProiezioni = 1; // Ci serve per dare un ID numerico a ogni proiezione

            // Continua a leggere finché ci sono righe nel file
            while ((linea = reader.readLine()) != null) {
                
                // Ignora le righe vuote o quelle che iniziano con # (che usiamo per i commenti)
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue; 
                }

                // Dividiamo la riga in tanti pezzetti usando il punto e virgola come separatore
                String[] parti = linea.split(";"); //questo ci darà un array di stringhe con tutti i dati della proiezione

                // Se mancano dei dati, saltiamo questa riga per evitare crash
                if (parti.length < 8) {
                    continue; 
                }

                // Estraiamo i dati dall'array "parti"
                String titolo = parti[0];
                String genere = parti[1];
                String regista = parti[2];
                
                // Integer.parseInt trasforma il testo in un numero intero
                int anno = Integer.parseInt(parti[3]);
                int durata = Integer.parseInt(parti[4]);
                int etaMinima = Integer.parseInt(parti[5]);

                // Trasformiamo il testo della data in un vero e proprio oggetto LocalDateTime
                LocalDateTime dataOra = LocalDateTime.parse(parti[6], DATETIME_FORMATTER);
                
                // Double.parseDouble trasforma il testo in un numero con la virgola
                // Sostituiamo la virgola con il punto perché Java vuole il punto per i decimali
                double costo = Double.parseDouble(parti[7].replace(",", "."));

                // Creiamo gli oggetti con i dati appena letti
                Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                Proiezione proiezione = new Proiezione(contatoreProiezioni, film, dataOra, costo);
                
                // Aggiungiamo la proiezione alla lista finale
                proiezioni.add(proiezione);
                contatoreProiezioni++; // Aumentiamo l'ID per la prossima proiezione
            }

        } catch (Exception e) {
            System.out.println("C'è stato un errore nel leggere le proiezioni: " + e.getMessage());
        }

        return proiezioni;
    }

    /**
     * Salva la lista di proiezioni sovrascrivendo l'attuale file di testo.
     * L'intestazione dei campi viene inserita come commento (carattere #) alla prima riga.
     * 
     * @param proiezioni la lista degli oggetti Proiezione attualmente in memoria da salvare su disco.
     */
    public static void salvaProiezioni(List<Proiezione> proiezioni) {
        // BufferedWriter e FileWriter servono per scrivere del testo dentro un file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            
            // Scriviamo un'intestazione per ricordarci l'ordine dei dati
            writer.write("# Formato: titolo;genere;regista;anno;durata;etaMinima;dataOra;costo\n");

            // Per ogni proiezione nella lista, costruiamo la riga da salvare
            for (Proiezione p : proiezioni) {
                Film f = p.getFilm();

                // Trasformiamo la data in un testo semplice leggibile
                String dataFormattata = p.getDataOraProiezione().format(DATETIME_FORMATTER);

                // Concateniamo tutti i dati unendoli con un punto e virgola
                // Il \n alla fine serve per andare a capo!
                String rigaDaSalvare = f.getTitolo() + ";" + 
                                       f.getGenere() + ";" + 
                                       f.getRegista() + ";" + 
                                       f.getAnno() + ";" + 
                                       f.getDurata() + ";" + 
                                       f.getEtaMinimaPubblico() + ";" + 
                                       dataFormattata + ";" + 
                                       p.getCostoBiglietto() + "\n";

                // Scriviamo la riga nel file
                writer.write(rigaDaSalvare);
            }

        } catch (Exception e) {
            System.out.println("Errore nel salvataggio proiezioni: " + e.getMessage());
        }
    }

    // =========================================================
    // SEZIONE UTENTI
    // =========================================================

    /**
     * Carica l'elenco degli utenti registrati dal file di testo.
     * Se il file non esiste, il metodo invoca automaticamente la creazione 
     * degli utenti di default e li salva prima di restituirli.
     * 
     * @return una lista popolata con gli oggetti Utente (Clienti, Proiezionisti, Bigliettai).
     */
    public static List<Utente> caricaUtenti() {
        List<Utente> utenti = new ArrayList<>();
        File file = new File(FILE_UTENTI);

        // Se il file non esiste, creiamo degli utenti base di prova e salviamoli
        if (!file.exists()) {
            creaUtentiDefault(utenti);
            salvaUtenti(utenti);
            return utenti;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] parti = linea.split(";");
                if (parti.length < 7) {
                    continue;
                }

                String nome = parti[0];
                String cognome = parti[1];
                String username = parti[2];
                String password = parti[3];
                String dataStr = parti[4];
                String luogo = parti[5];
                String ruolo = parti[6];

                LocalDate dataNascita = null;
                // Controlliamo se la data non è vuota prima di provare a convertirla
                if (!dataStr.equals("") && !dataStr.equals("null")) {
                    dataNascita = LocalDate.parse(dataStr, DATE_FORMATTER);
                }

                // In base al ruolo scritto nel file, creiamo il tipo di utente corretto
                Utente nuovoUtente = null;
                if (ruolo.equals("cliente")) {
                    nuovoUtente = new Cliente(nome, cognome, username, password, dataNascita, luogo);
                } else if (ruolo.equals("proiezionista")) {
                    nuovoUtente = new Proiezionista(nome, cognome, username, password, dataNascita, luogo);
                } else if (ruolo.equals("bigliettaio")) {
                    nuovoUtente = new Bigliettaio(nome, cognome, username, password, dataNascita, luogo);
                }

                // Se l'abbiamo creato con successo, lo aggiungiamo alla lista
                if (nuovoUtente != null) {
                    utenti.add(nuovoUtente);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura utenti: " + e.getMessage());
        }

        return utenti;
    }

    /**
     * Metodo di supporto (interno) che inietta una lista di utenti "mock" di partenza
     * per permettere al sistema di funzionare al primo avvio.
     * 
     * @param utenti la lista di utenti vuota che verrà popolata dal metodo.
     */
    private static void creaUtentiDefault(List<Utente> utenti) {
        // Aggiungiamo un paio di utenti finti per avere qualcosa nel sistema al primo avvio
        utenti.add(new Bigliettaio("Luca", "Bianchi", "lbianchi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Bigliettaio("Marco", "Rossi", "mrossi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Bigliettaio("Sofia", "Rossi", "srossi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Bigliettaio("Andrea", "Bianchi", "abianchi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Bigliettaio("Giovanni", "Verdi", "gverdi", "password123", LocalDate.of(1990, 3, 10), "Napoli"));
        utenti.add(new Proiezionista("Mario", "Rossi", "mrossi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
        utenti.add(new Proiezionista("Giulia", "Rossi", "grossi", "password123", LocalDate.of(1985, 5, 15), "Milano"));
    }

    /**
     * Salva l'elenco degli utenti (sovrascrivendo i dati vecchi) nel file testuale.
     * 
     * @param utenti la lista contenente tutti gli utenti attuali del sistema da persistere su disco.
     */
    public static void salvaUtenti(List<Utente> utenti) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
            writer.write("# Formato: nome;cognome;username;password;dataNascita;luogo;ruolo\n");

            for (Utente u : utenti) {
                
                // Se l'utente non ha inserito la data di nascita, mettiamo uno spazio vuoto, 
                // altrimenti la formattiamo normalmente
                String dataTesto = "";
                if (u.getDataNascita() != null) {
                    dataTesto = u.getDataNascita().format(DATE_FORMATTER);
                }

                String rigaDaSalvare = u.getNome() + ";" + 
                                       u.getCognome() + ";" + 
                                       u.getUsername() + ";" + 
                                       "password123" + ";" +  // Per semplicità mettiamo sempre la stessa password fissa
                                       dataTesto + ";" + 
                                       u.getLuogoDomicilio() + ";" + 
                                       u.getRuolo() + "\n";

                writer.write(rigaDaSalvare);
            }
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio utenti: " + e.getMessage());
        }
    }

    // =========================================================
    // SEZIONE PRENOTAZIONI
    // =========================================================

    /**
     * Carica tutte le prenotazioni memorizzate nel file di testo, 
     * reidratando i collegamenti logici con le liste di utenti e proiezioni esistenti.
     * 
     * @param utenti l'elenco degli utenti precedentemente caricato dal sistema
     * @param proiezioni l'elenco delle proiezioni precedentemente caricate dal sistema
     * @return una lista popolata con gli oggetti Prenotazione validi e correttamente associati,
     *         vuota se il file è inesistente.
     */
    public static List<Prenotazione> caricaPrenotazioni(List<Utente> utenti, List<Proiezione> proiezioni) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        File file = new File(FILE_PRENOTAZIONI);

        if (!file.exists()) {
            return prenotazioni;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] parti = linea.split(";");
                if (parti.length < 3) {
                    continue;
                }

                String usernameCercato = parti[0];
                int idProiezioneCercata = Integer.parseInt(parti[1]);
                int numeroBiglietti = Integer.parseInt(parti[2]);

                // 1. Dobbiamo trovare l'oggetto Cliente che ha quello specifico username
                Cliente clienteTrovato = null;
                for (Utente u : utenti) {
                    if (u.getRuolo().equals("cliente") && u.getUsername().equals(usernameCercato)) {
                        clienteTrovato = (Cliente) u;
                        break; // Trovato, possiamo fermare il ciclo
                    }
                }

                // 2. Dobbiamo trovare l'oggetto Proiezione che ha quello specifico ID
                Proiezione proiezioneTrovata = null;
                for (Proiezione p : proiezioni) {
                    if (p.getId() == idProiezioneCercata) {
                        proiezioneTrovata = p;
                        break; // Trovato, fermiamo il ciclo
                    }
                }

                // 3. Se abbiamo trovato entrambi, creiamo la prenotazione
                if (clienteTrovato != null && proiezioneTrovata != null) {
                    Prenotazione p = new Prenotazione(clienteTrovato, proiezioneTrovata, numeroBiglietti);
                    prenotazioni.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura prenotazioni: " + e.getMessage());
        }

        return prenotazioni;
    }

    /**
     * Salva l'elenco completo delle prenotazioni attive all'interno del file di testo.
     * Viene scritto l'username del cliente, l'ID della proiezione e la quantità di biglietti acquistati.
     * 
     * @param prenotazioni la lista di oggetti Prenotazione da scrivere sul disco.
     */
    public static void salvaPrenotazioni(List<Prenotazione> prenotazioni) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRENOTAZIONI))) {
            writer.write("# Formato: usernameCliente;idProiezione;numeroBiglietti\n");

            for (Prenotazione p : prenotazioni) {
                String rigaDaSalvare = p.getCliente().getUsername() + ";" + 
                                       p.getProiezione().getId() + ";" + 
                                       p.getNumeroBiglietti() + "\n";

                writer.write(rigaDaSalvare);
            }
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio prenotazioni: " + e.getMessage());
        }
    }
}