

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
    public String getTitolo() {
        return titolo;
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public String getGenere() {
        return genere;
    }
    
    public void setGenere(String genere) {
        this.genere = genere;
    }
    
    public String getRegista() {
        return regista;
    }
    
    public void setRegista(String regista) {
        this.regista = regista;
    }
    
    public int getAnno() {
        return anno;
    }
    
    public void setAnno(int anno) {
        this.anno = anno;
    }
    
    public int getDurata() {
        return durata;
    }
    
    public void setDurata(int durata) {
        this.durata = durata;
    }
    
    public int getEtaMinimaPubblico() {
        return etaMinimaPubblico;
    }
    
    public void setEtaMinimaPubblico(int etaMinimaPubblico) {
        this.etaMinimaPubblico = etaMinimaPubblico;
    }
    
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
