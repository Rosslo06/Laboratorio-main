

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
    private String ruolo; // cliente, proiezionista, bigliettaio
    
    /**
     * Costruttore per la creazione di un utente
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param username l'username dell'utente
     * @param password la password in chiaro (verrà hashata)
     * @param dataNascita la data di nascita
     * @param luogoDomicilio il luogo del domicilio
     * @param ruolo il ruolo dell'utente
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
     * @param password la password da hashare
     * @return la password hashata
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
     * @param password la password da verificare
     * @return true se la password è corretta, false altrimenti
     */
    public boolean verificaPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }
    
    /**
     * Ottiene il nome dell'utente
     * @return il nome
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Imposta il nome dell'utente
     * @param nome il nome da impostare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Ottiene il cognome dell'utente
     * @return il cognome
     */
    public String getCognome() {
        return cognome;
    }
    
    /**
     * Imposta il cognome dell'utente
     * @param cognome il cognome da impostare
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    /**
     * Ottiene lo username dell'utente
     * @return lo username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Imposta lo username dell'utente
     * @param username lo username da impostare
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Ottiene la data di nascita dell'utente
     * @return la data di nascita
     */
    public LocalDate getDataNascita() {
        return dataNascita;
    }
    
    /**
     * Imposta la data di nascita dell'utente
     * @param dataNascita la data di nascita da impostare
     */
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }
    
    /**
     * Ottiene il luogo di domicilio dell'utente
     * @return il luogo di domicilio
     */
    public String getLuogoDomicilio() {
        return luogoDomicilio;
    }
    
    /**
     * Imposta il luogo di domicilio dell'utente
     * @param luogoDomicilio il luogo di domicilio da impostare
     */
    public void setLuogoDomicilio(String luogoDomicilio) {
        this.luogoDomicilio = luogoDomicilio;
    }
    
    /**
     * Ottiene il ruolo dell'utente (cliente, proiezionista, bigliettaio)
     * @return il ruolo
     */
    public String getRuolo() {
        return ruolo;
    }
    
    /**
     * Imposta il ruolo dell'utente
     * @param ruolo il ruolo da impostare
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
    
    /**
     * Restituisce la rappresentazione in stringa dell'utente
     * @return stringa con informazioni dell'utente
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
