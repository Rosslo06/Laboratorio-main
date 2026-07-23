/**
 * Classe che rappresenta un regista nel sistema CineMax
 * Contiene informazioni di base su nome e cognome del regista
 * @author Matteo Piccolo
 * @version 1.0
 */
public class Regista {
    private String nome;
    private String cognome;

    /**
     * Costruttore per la creazione di un regista
     * @param nome il nome del regista
     * @param cognome il cognome del regista
     */
    public Regista(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Ottiene il nome del regista
     * @return il nome del regista
     */
    public String getNome() {
        return nome;
    }

    /**
     * Ottiene il cognome del regista
     * @return il cognome del regista
     */
    public String getCognome() {
        return cognome;
    }
    
    /**
     * Restituisce la rappresentazione in stringa del regista
     * @return stringa nel formato "nome cognome"
     */
    @Override
    public String toString() {
        return nome + " " + cognome;
    }
}
