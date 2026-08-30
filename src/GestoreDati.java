import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GestoreDati {

    private static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    private static final String FILE_UTENTI = "data/utenti.txt";
    private static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";
    private static final String FILE_CSV_PROIEZIONI = "data/proiezioni.csv";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    // =========================
    // PROIEZIONI
    // =========================

    public static List<Proiezione> caricaProiezioni() {

        List<Proiezione> proiezioni = new ArrayList<>();

        File csvFile = new File(FILE_CSV_PROIEZIONI);

        if (csvFile.exists()) {
            return caricaProiezioniDaCSV(csvFile);
        }

        File file = new File(FILE_PROIEZIONI);

        if (!file.exists()) {
            return proiezioni;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;
            int contatoreProiezioni = 1;

            while ((line = reader.readLine()) != null) {

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

                    LocalDateTime dataOra =
                            LocalDateTime.parse(
                                    parti[6].trim(),
                                    DATETIME_FORMATTER
                            );

                    double costoBiglietto =
                            Double.parseDouble(
                                    parti[7].trim().replace(",", ".")
                            );

                    Film film = new Film(
                            titolo,
                            genere,
                            regista,
                            anno,
                            durata,
                            etaMinima
                    );

                    Proiezione proiezione = new Proiezione(
                            contatoreProiezioni++,
                            film,
                            dataOra,
                            costoBiglietto
                    );

                    proiezioni.add(proiezione);

                } catch (Exception e) {
                    System.err.println(
                            "Errore nel parsing della riga: " + line
                    );
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Errore nella lettura: " + e.getMessage()
            );
        }

        return proiezioni;
    }


    private static List<Proiezione> caricaProiezioniDaCSV(File csvFile) {

        List<Proiezione> proiezioni = new ArrayList<>();

        DateTimeFormatter csvDateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int contatoreProiezioni = 1;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(csvFile))) {

            String line;
            boolean primaLinea = true;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Salta l'intestazione del CSV
                if (primaLinea) {
                    primaLinea = false;

                    if (line.toLowerCase().contains("titolo")
                            || line.toLowerCase().contains("data_ora")) {
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

                    int anno =
                            Integer.parseInt(pulisciCampo(parti[4]));

                    int durata =
                            Integer.parseInt(pulisciCampo(parti[5]));

                    int etaMinima =
                            Integer.parseInt(pulisciCampo(parti[6]));

                    double costoBiglietto =
                            Double.parseDouble(
                                    pulisciCampo(parti[7])
                                            .replace(",", ".")
                            );

                    LocalDateTime dataOra;

                    if (dataStr.contains("T")) {
                        dataOra = LocalDateTime.parse(dataStr);
                    } else {
                        dataOra = LocalDateTime.parse(
                                dataStr,
                                csvDateFormatter
                        );
                    }

                    Film film = new Film(
                            titolo,
                            genere,
                            regista,
                            anno,
                            durata,
                            etaMinima
                    );

                    Proiezione proiezione = new Proiezione(
                            contatoreProiezioni++,
                            film,
                            dataOra,
                            costoBiglietto
                    );

                    proiezioni.add(proiezione);

                } catch (Exception e) {
                    System.err.println(
                            "Errore nel parsing della riga: " + line
                    );
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Errore nella lettura del CSV: " + e.getMessage()
            );
        }

        return proiezioni;
    }


    private static String pulisciCampo(String campo) {

        if (campo == null) {
            return "";
        }

        String pulito = campo.trim();

        if (pulito.startsWith("\"")
                && pulito.endsWith("\"")
                && pulito.length() >= 2) {

            pulito = pulito.substring(
                    1,
                    pulito.length() - 1
            ).trim();
        }

        return pulito.replace("\"", "").trim();
    }


    private static String[] parseCSVLine(String line) {

        List<String> risultato = new ArrayList<>();

        StringBuilder campoCorrente =
                new StringBuilder();

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


    public static void salvaProiezioni(
            List<Proiezione> proiezioni) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FILE_PROIEZIONI))) {

            writer.write(
                    "# Formato: titolo;genere;regista;anno;" +
                    "durata;etaMinima;dataOra;costo\n"
            );

            for (Proiezione p : proiezioni) {

                Film f = p.getFilm();

                String linea = String.format(
                        "%s;%s;%s;%d;%d;%d;%s;%.2f\n",

                        f.getTitolo(),
                        f.getGenere(),
                        f.getRegista(),
                        f.getAnno(),
                        f.getDurata(),
                        f.getEtaMinimaPubblico(),

                        p.getDataOraProiezione()
                                .format(DATETIME_FORMATTER),

                        p.getCostoBiglietto()
                );

                writer.write(linea);
            }

        } catch (IOException e) {
            System.err.println(
                    "Errore nel salvataggio: " + e.getMessage()
            );
        }
    }


    // =========================
    // UTENTI
    // =========================

    public static List<Utente> caricaUtenti() {

        List<Utente> utenti = new ArrayList<>();

        File file = new File(FILE_UTENTI);

        if (!file.exists()) {

            creaUtentiDefault(utenti);
            salvaUtenti(utenti);

            return utenti;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()
                        || line.startsWith("#")) {
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

                    if (!dataNascitaStr.isEmpty()
                            && !dataNascitaStr.equals("null")) {

                        dataNascita = LocalDate.parse(
                                dataNascitaStr,
                                DATE_FORMATTER
                        );
                    }

                    Utente utente = creaUtenteDalRuolo(
                            nome,
                            cognome,
                            username,
                            password,
                            dataNascita,
                            luogo,
                            ruolo
                    );

                    if (utente != null) {
                        utenti.add(utente);
                    }

                } catch (Exception e) {

                    System.err.println(
                            "Errore nel parsing dell'utente: "
                            + line
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Errore nella lettura: "
                    + e.getMessage()
            );
        }

        if (utenti.isEmpty()) {

            creaUtentiDefault(utenti);
            salvaUtenti(utenti);
        }

        return utenti;
    }


    private static void creaUtentiDefault(
            List<Utente> utenti) {

        utenti.add(
                new Proiezionista(
                        "Marco",
                        "Rossi",
                        "mrossi",
                        "password123",
                        LocalDate.of(1985, 5, 15),
                        "Milano"
                )
        );

        utenti.add(
                new Proiezionista(
                        "Elena",
                        "Bianchi",
                        "ebianchi",
                        "password123",
                        LocalDate.of(1988, 8, 22),
                        "Roma"
                )
        );

        utenti.add(
                new Bigliettaio(
                        "Giovanni",
                        "Verdi",
                        "gverdi",
                        "password123",
                        LocalDate.of(1990, 3, 10),
                        "Napoli"
                )
        );

        utenti.add(
                new Bigliettaio(
                        "Francesca",
                        "Neri",
                        "fneri",
                        "password123",
                        LocalDate.of(1992, 7, 18),
                        "Torino"
                )
        );

        utenti.add(
                new Bigliettaio(
                        "Andrea",
                        "Galli",
                        "agalli",
                        "password123",
                        LocalDate.of(1995, 2, 25),
                        "Firenze"
                )
        );

        utenti.add(
                new Bigliettaio(
                        "Sara",
                        "Ferrari",
                        "sferrari",
                        "password123",
                        LocalDate.of(1993, 11, 30),
                        "Bologna"
                )
        );

        utenti.add(
                new Bigliettaio(
                        "Luca",
                        "Conti",
                        "lconti",
                        "password123",
                        LocalDate.of(1991, 6, 12),
                        "Genova"
                )
        );
    }


    private static Utente creaUtenteDalRuolo(
            String nome,
            String cognome,
            String username,
            String password,
            LocalDate dataNascita,
            String luogo,
            String ruolo) {

        try {

            if (ruolo.equalsIgnoreCase("cliente")) {

                return new Cliente(
                        nome,
                        cognome,
                        username,
                        password,
                        dataNascita,
                        luogo
                );
            }

            if (ruolo.equalsIgnoreCase("proiezionista")) {

                return new Proiezionista(
                        nome,
                        cognome,
                        username,
                        password,
                        dataNascita,
                        luogo
                );
            }

            if (ruolo.equalsIgnoreCase("bigliettaio")) {

                return new Bigliettaio(
                        nome,
                        cognome,
                        username,
                        password,
                        dataNascita,
                        luogo
                );
            }

        } catch (Exception e) {

            return null;
        }

        return null;
    }


    public static void salvaUtenti(
            List<Utente> utenti) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FILE_UTENTI))) {

            writer.write(
                    "# Formato: nome;cognome;username;" +
                    "password;dataNascita;luogo;ruolo\n"
            );

            for (Utente u : utenti) {

                String dataNascita =
                        u.getDataNascita() != null
                                ? u.getDataNascita()
                                        .format(DATE_FORMATTER)
                                : "";

                String linea = String.format(
                        "%s;%s;%s;%s;%s;%s;%s\n",

                        u.getNome(),
                        u.getCognome(),
                        u.getUsername(),
                        "password123",
                        dataNascita,
                        u.getLuogoDomicilio(),
                        u.getRuolo()
                );

                writer.write(linea);
            }

        } catch (IOException e) {

            System.err.println(
                    "Errore nel salvataggio: "
                    + e.getMessage()
            );
        }
    }


    // =========================
    // PRENOTAZIONI
    // =========================

    public static List<Prenotazione> caricaPrenotazioni(
            List<Utente> utenti,
            List<Proiezione> proiezioni) {

        List<Prenotazione> prenotazioni =
                new ArrayList<>();

        File file = new File(FILE_PRENOTAZIONI);

        if (!file.exists()) {
            return prenotazioni;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()
                        || line.startsWith("#")) {
                    continue;
                }

                try {

                    String[] parti = line.split(";");

                    if (parti.length < 3) {
                        continue;
                    }

                    String usernameCliente =
                            parti[0].trim();

                    int idProiezione =
                            Integer.parseInt(parti[1].trim());

                    int numeroBiglietti =
                            Integer.parseInt(parti[2].trim());


                    Cliente cliente = null;

                    for (Utente u : utenti) {

                        if (u instanceof Cliente
                                && u.getUsername()
                                .equals(usernameCliente)) {

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


                    if (cliente != null
                            && proiezione != null) {

                        Prenotazione prenotazione =
                                new Prenotazione(
                                        cliente,
                                        proiezione,
                                        numeroBiglietti
                                );

                        prenotazioni.add(prenotazione);
                    }

                } catch (Exception e) {

                    System.err.println(
                            "Errore nel parsing: " + line
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Errore nella lettura: "
                    + e.getMessage()
            );
        }

        return prenotazioni;
    }


    public static void salvaPrenotazioni(
            List<Prenotazione> prenotazioni) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     FILE_PRENOTAZIONI))) {

            writer.write(
                    "# Formato: usernameCliente;" +
                    "idProiezione;numeroBiglietti\n"
            );

            for (Prenotazione p : prenotazioni) {

                String linea = String.format(
                        "%s;%d;%d\n",

                        p.getCliente().getUsername(),
                        p.getProiezione().getId(),
                        p.getNumeroBiglietti()
                );

                writer.write(linea);
            }

        } catch (IOException e) {

            System.err.println(
                    "Errore nel salvataggio: "
                    + e.getMessage()
            );
        }
    }
}