

/**
 * Classe che rappresenta un film nel sistema CineMax
 
 */
public class Film{
    
    
    private String titolo;
    private String genere;
    private String regista;
    private int anno;
    private int durata;
    private int etaMinimaPubblico;
    
    /**
     * Costruttore per la creazione di un film
     */
    public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinimaPubblico) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinimaPubblico = etaMinimaPubblico;
    }
    
    // ============ GETTER E SETTER ============
    /**
     * Restituisce il titolo del film
     * @return il titolo del film
     */
    public String getTitolo() {
        return titolo;
    }
    /**
     * Imposta il titolo del film   
     * @param titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    /**
     * Restituisce il genere del film
     * @return
     */
    public String getGenere() {
        return genere;
    }
    /**
     *  Imposta il genere del film
     * @param genere
     */
    public void setGenere(String genere) {
        this.genere = genere;
    }
    /**
     *     Restituisce il regista del film
     * @return
     */
    
    public String getRegista() {
        return regista;
    }
    /**
     * Imposta il regista del film
     * @param regista
     */
    public void setRegista(String regista) {
        this.regista = regista;
    }
    /**
     * Restituisce l'anno di uscita del film    
     * @return
     */
    public int getAnno() {
        return anno;
    }
    /**
     * Imposta l'anno di uscita del film
     * @param anno
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }
    /**
     * Restituisce la durata del film in minuti
     * @return
     */
    
    public int getDurata() {
        return durata;
    }
    /**
     * Imposta la durata del film in minuti
     * @param durata
     */
    public void setDurata(int durata) {
        this.durata = durata;
    }
    /**
     * Restituisce l'età minima per il pubblico adatto alla visione del film    
     * @return
     */
    public int getEtaMinimaPubblico() {
        return etaMinimaPubblico;
    }
    /**
     * Imposta l'età minima per il pubblico adatto alla visione del film
     * @param etaMinimaPubblico
     */
    public void setEtaMinimaPubblico(int etaMinimaPubblico) {
        this.etaMinimaPubblico = etaMinimaPubblico;
    }
    /**
     * Restituisce una rappresentazione testuale dei dati del film
     * @return una stringa con i dati del film 
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