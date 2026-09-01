package cinemax;

/**
 * Classe che rappresenta un film nel sistema CineMax.
 * Contiene tutte le informazioni descrittive dell'opera cinematografica.
 * 
 * @author Andrea
 * @version 1.0
 */
public class Film {
    private String titolo;
    private String genere;
    private String regista;
    private int anno;
    private int durata;
    private int etaMinimaPubblico;
    
    /**
     * Costruttore per la creazione di un nuovo film.
     * 
     * @param titolo il titolo del film
     * @param genere il genere cinematografico (es. Azione, Commedia)
     * @param regista il nome del regista del film
     * @param anno l'anno di uscita del film nelle sale
     * @param durata la durata del film espressa in minuti
     * @param etaMinimaPubblico l'età minima consigliata per la visione
     */
    public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinimaPubblico) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinimaPubblico = etaMinimaPubblico;
    }
    
    /**
     * Restituisce il titolo del film.
     * @return il titolo del film
     */
    public String getTitolo() { return titolo; }
    
    /**
     * Imposta un nuovo titolo per il film.
     * @param titolo il nuovo titolo da assegnare
     */
    public void setTitolo(String titolo) { this.titolo = titolo; }
    
    /**
     * Restituisce il genere del film.
     * @return il genere del film
     */
    public String getGenere() { return genere; }
    
    /**
     * Imposta il genere del film.
     * @param genere il genere da assegnare
     */
    public void setGenere(String genere) { this.genere = genere; }
    
    /**
     * Restituisce il regista del film.
     * @return il nome del regista
     */
    public String getRegista() { return regista; }
    
    /**
     * Imposta il regista del film.
     * @param regista il nome del regista da assegnare
     */
    public void setRegista(String regista) { this.regista = regista; }
    
    /**
     * Restituisce l'anno di uscita del film.
     * @return l'anno di uscita
     */
    public int getAnno() { return anno; }
    
    /**
     * Imposta l'anno di uscita del film.
     * @param anno l'anno da assegnare
     */
    public void setAnno(int anno) { this.anno = anno; }
    
    /**
     * Restituisce la durata del film in minuti.
     * @return la durata in minuti
     */
    public int getDurata() { return durata; }
    
    /**
     * Imposta la durata del film in minuti.
     * @param durata la durata in minuti da assegnare
     */
    public void setDurata(int durata) { this.durata = durata; }
    
    /**
     * Restituisce l'età minima per il pubblico adatto alla visione.
     * @return l'età minima
     */
    public int getEtaMinimaPubblico() { return etaMinimaPubblico; }
    
    /**
     * Imposta l'età minima per il pubblico.
     * @param etaMinimaPubblico l'età minima da assegnare
     */
    public void setEtaMinimaPubblico(int etaMinimaPubblico) { this.etaMinimaPubblico = etaMinimaPubblico; }
    
    /**
     * Restituisce una rappresentazione testuale dei dati del film.
     * @return una stringa formattata con i dettagli del film
     */
    @Override
    public String toString() {
        return "Film{" + "titolo='" + titolo + '\'' + ", genere='" + genere + '\'' +
               ", regista='" + regista + '\'' + ", anno=" + anno +
               ", durata=" + durata + " min, etaMinimaPubblico=" + etaMinimaPubblico + '}';
    }
}