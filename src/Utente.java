import java.io.Serializable;
import java.time.LocalDate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe astratta che rappresenta un utente generico nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public abstract class Utente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nome;
    private String cognome;
    private String username;
    private String passwordHash;
    private LocalDate dataNascita;
    private String luogoDomicilio;
    private String ruolo;
    
    /**
     * Costruttore per la creazione di un utente
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
     * Metodo per hashare la password
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
     */
    public boolean verificaPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }
    
    // ============ GETTER E SETTER ============
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public LocalDate getDataNascita() {
        return dataNascita;
    }
    
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }
    
    public String getLuogoDomicilio() {
        return luogoDomicilio;
    }
    
    public void setLuogoDomicilio(String luogoDomicilio) {
        this.luogoDomicilio = luogoDomicilio;
    }
    
    public String getRuolo() {
        return ruolo;
    }
    
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
    
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