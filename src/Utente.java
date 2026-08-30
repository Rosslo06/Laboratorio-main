
import java.time.LocalDate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe astratta che rappresenta un utente generico nel sistema CineMax
 * 
 * @author Andrea
 * @version 1.0
 */
public abstract class Utente {
    

    private String nome;
    private String cognome;
    private String username;
    private String passwordHash;
    private LocalDate dataNascita;
    private String luogoDomicilio;
    private String ruolo;

    /**
     * Costruttore per la creazione di un utente
     * 
     * @param nome           il nome dell'utente
     * @param cognome        il cognome dell'utente
     * @param username       l'username dell'utente
     * @param password       la password dell'utente
     * @param dataNascita    la data di nascita dell'utente
     * @param luogoDomicilio il luogo di domicilio dell'utente
     * @param ruolo          il ruolo dell'utente (es. "bigliettaio",
     *                       "proiezionista")
     */
    public Utente(String nome, String cognome, String username, String password,
            LocalDate dataNascita, String luogoDomicilio, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.dataNascita = dataNascita;
        this.luogoDomicilio = luogoDomicilio;
        this.ruolo = ruolo;
    }

    /**
     * Metodo per calcolare l'hash della password
     * 
     * @param password la password in chiaro
     * @return l'hash della password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore nel calcolo dell'hash della password", e);
        }
    }

    /**
     * Verifica se la password fornita è corretta
     * 
     * @param password la password da verificare
     * @return true se la password è corretta, false altrimenti*
     */
    public boolean verificaPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    // ============ GETTER E SETTER ============
    /**
     * serve per ottenere il nome dell'utente
     * 
     * @return il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * serve per impostare il nome dell'utente
     * 
     * @param nome il nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * serve per ottenere il cognome dell'utente
     * 
     * @return il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * serve per impostare il cognome dell'utente
     * 
     * @param cognome il cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * serve per ottenere l'username dell'utente
     * 
     * @return l'username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * serve per impostare l'username dell'utente
     * 
     * @param username l'username dell'utente
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * serve per ottenere la data di nascita dell'utente
     * 
     * @return la data di nascita dell'utente
     */
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    /**
     * serve per impostare la data di nascita dell'utente
     * 
     * @param dataNascita la data di nascita dell'utente
     */
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * serve per ottenere il luogo di domicilio dell'utente
     * 
     * @return il luogo di domicilio dell'utente
     */
    public String getLuogoDomicilio() {
        return luogoDomicilio;
    }

    /**
     * serve per impostare il luogo di domicilio dell'utente
     * 
     * @param luogoDomicilio il luogo di domicilio dell'utente
     */
    public void setLuogoDomicilio(String luogoDomicilio) {
        this.luogoDomicilio = luogoDomicilio;
    }

    /**
     * serve per ottenere il ruolo dell'utente
     * 
     * @return il ruolo dell'utente
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * serve per impostare il ruolo dell'utente
     * 
     * @param ruolo il ruolo dell'utente
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Restituisce una rappresentazione testuale dei dati dell'utente
     * 
     * @return una stringa che rappresenta i dati dell'utente
     */
    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", dataNascita=" + dataNascita +
                ", luogoDomicilio='" + luogoDomicilio + '\'' +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }
}