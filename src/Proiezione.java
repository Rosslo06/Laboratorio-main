
import java.time.LocalDateTime;

/**
 * Classe che rappresenta una proiezione nel sistema CineMax
 * 
 * @author Andrea
 * @version 1.0
 */
public class Proiezione{
    

    private int id;
    private Film film;
    private LocalDateTime dataOraProiezione;
    private double costoBiglietto;
    private int capacitaSala = 200;

    /**
     * Costruttore per la creazione di una proiezione
     * 
     * @param id                l'identificativo univoco della proiezione
     * @param film              il film proiettato
     * @param dataOraProiezione la data e l'ora della proiezione
     * @param costoBiglietto    il costo del biglietto per la proiezione
     * 
     * 
     */
    public Proiezione(int id, Film film, LocalDateTime dataOraProiezione, double costoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOraProiezione = dataOraProiezione;
        this.costoBiglietto = costoBiglietto;
    }

    // ============ GETTER E SETTER ============
    /**
     * Restituisce l'identificativo univoco della proiezione
     * 
     * @return l'identificativo univoco della proiezione
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificativo univoco della proiezione
     * 
     * @param id l'identificativo univoco della proiezione
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce il film proiettato
     * 
     * @return il film proiettato
     */

    public Film getFilm() {
        return film;
    }

    /**
     * Imposta il film proiettato
     * 
     * @param film il film proiettato
     */

    public void setFilm(Film film) {
        this.film = film;
    }

    /**
     * Restituisce la data e l'ora della proiezione
     * 
     * @return la data e l'ora della proiezione
     */
    public LocalDateTime getDataOraProiezione() {
        return dataOraProiezione;
    }

    /**
     * Imposta la data e l'ora della proiezione
     * 
     * @param dataOraProiezione la data e l'ora della proiezione
     */
    public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
        this.dataOraProiezione = dataOraProiezione;
    }

    /**
     * Restituisce il costo del biglietto per la proiezione
     * 
     * @return il costo del biglietto per la proiezione
     */
    public double getCostoBiglietto() {
        return costoBiglietto;
    }

    /**
     * Imposta il costo del biglietto per la proiezione
     * 
     * @param costoBiglietto il costo del biglietto per la proiezione
     */
    public void setCostoBiglietto(double costoBiglietto) {
        this.costoBiglietto = costoBiglietto;
    }

    /**
     * Restituisce la capacità della sala per la proiezione
     * 
     * @return la capacità della sala per la proiezione
     */

    public int getCapacitaSala() {
        return capacitaSala;
    }

    /**
     * Restituisce una rappresentazione testuale della proiezione
     * 
     * @return una rappresentazione testuale della proiezione
     */
    @Override
    public String toString() {
        return "Proiezione{" +
                "id=" + id +
                ", film=" + film.getTitolo() +
                ", dataOraProiezione=" + dataOraProiezione +
                ", costoBiglietto=" + costoBiglietto + "€" +
                '}';
    }
}