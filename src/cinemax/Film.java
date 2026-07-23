

import java.io.Serializable;

/**
 * Classe che rappresenta un film nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Film implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String titolo;
    private String genere;
    private String regista;
    private int anno;
    private int durata; // in minuti
    private int etaMinimaPubblico;
    
    /**
     * Costruttore per la creazione di un film
     * @param titolo il titolo del film
     * @param genere il genere del film
     * @param regista il regista del film
     * @param anno l'anno di uscita
     * @param durata la durata in minuti
     * @param etaMinimaPubblico l'età minima consigliata
     */
    public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinimaPubblico) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinimaPubblico = etaMinimaPubblico;
    }
    
    // Getter e Setter
    /**
     * Ottiene il titolo del film
     * @return il titolo
     */
    public String getTitolo() {
        return titolo;
    }
    
    /**
     * Imposta il titolo del film
     * @param titolo il titolo da impostare
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    /**
     * Ottiene il genere del film
     * @return il genere
     */
    public String getGenere() {
        return genere;
    }
    
    /**
     * Imposta il genere del film
     * @param genere il genere da impostare
     */
    public void setGenere(String genere) {
        this.genere = genere;
    }
    
    /**
     * Ottiene il nome del regista
     * @return il nome del regista
     */
    public String getRegista() {
        return regista;
    }
    
    /**
     * Imposta il nome del regista
     * @param regista il nome del regista da impostare
     */
    public void setRegista(String regista) {
        this.regista = regista;
    }
    
    /**
     * Ottiene l'anno di uscita del film
     * @return l'anno
     */
    public int getAnno() {
        return anno;
    }
    
    /**
     * Imposta l'anno di uscita del film
     * @param anno l'anno da impostare
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }
    
    /**
     * Ottiene la durata del film in minuti
     * @return la durata in minuti
     */
    public int getDurata() {
        return durata;
    }
    
    /**
     * Imposta la durata del film in minuti
     * @param durata la durata in minuti da impostare
     */
    public void setDurata(int durata) {
        this.durata = durata;
    }
    
    /**
     * Ottiene l'età minima consigliata per il pubblico
     * @return l'età minima
     */
    public int getEtaMinimaPubblico() {
        return etaMinimaPubblico;
    }
    
    /**
     * Imposta l'età minima consigliata per il pubblico
     * @param etaMinimaPubblico l'età minima da impostare
     */
    public void setEtaMinimaPubblico(int etaMinimaPubblico) {
        this.etaMinimaPubblico = etaMinimaPubblico;
    }
    
    /**
     * Restituisce la rappresentazione in stringa del film
     * @return stringa con tutte le informazioni del film
     */
    @Override
    public String toString() {
        return "Film{" +
                "titolo='" + titolo + '\'' +
                ", genere='" + genere + '\'' +
                ", regista='" + regista + '\'' +
                ", anno=" + anno +
                ", durata=" + durata + " min" +
                ", etaMinimaPubblico=" + etaMinimaPubblico +
                '}';
    }
}
