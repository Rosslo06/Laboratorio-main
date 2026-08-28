/**
 * Classe che rappresenta un regista nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Regista {
    private String nome;
    private String cognome;

    /**
     * Costruttore per la creazione di un regista
     */
    public Regista(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Ottiene il nome del regista
     */
    public String getNome() {
        return nome;
    }

    /**
     * Ottiene il cognome del regista
     */
    public String getCognome() {
        return cognome;
    }
    
    @Override
    public String toString() {
        return nome + " " + cognome;
    }
}