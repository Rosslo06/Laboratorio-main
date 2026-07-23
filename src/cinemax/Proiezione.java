

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe che rappresenta una proiezione nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Proiezione implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private Film film;
    private LocalDateTime dataOraProiezione;
    private double costoBiglietto;
    private final int CAPACITA_SALA = 200;
    
    /**
     * Costruttore per la creazione di una proiezione
     * @param id l'identificativo della proiezione
     * @param film il film della proiezione
     * @param dataOraProiezione la data e ora della proiezione
     * @param costoBiglietto il costo del biglietto
     */
    public Proiezione(int id, Film film, LocalDateTime dataOraProiezione, double costoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOraProiezione = dataOraProiezione;
        this.costoBiglietto = costoBiglietto;
    }
    
    // Getter e Setter
    /**
     * Ottiene l'identificativo della proiezione
     * @return l'id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Imposta l'identificativo della proiezione
     * @param id l'id da impostare
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Ottiene il film della proiezione
     * @return il film
     */
    public Film getFilm() {
        return film;
    }
    
    /**
     * Imposta il film della proiezione
     * @param film il film da impostare
     */
    public void setFilm(Film film) {
        this.film = film;
    }
    
    /**
     * Ottiene la data e ora della proiezione
     * @return la data e ora
     */
    public LocalDateTime getDataOraProiezione() {
        return dataOraProiezione;
    }
    
    /**
     * Imposta la data e ora della proiezione
     * @param dataOraProiezione la data e ora da impostare
     */
    public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
        this.dataOraProiezione = dataOraProiezione;
    }
    
    /**
     * Ottiene il costo del biglietto
     * @return il costo in euro
     */
    public double getCostoBiglietto() {
        return costoBiglietto;
    }
    
    /**
     * Imposta il costo del biglietto
     * @param costoBiglietto il costo in euro da impostare
     */
    public void setCostoBiglietto(double costoBiglietto) {
        this.costoBiglietto = costoBiglietto;
    }
    
    /**
     * Ottiene la capacità totale della sala
     * @return la capacità della sala (numero di posti)
     */
    public int getCapacitaSala() {
        return CAPACITA_SALA;
    }
    
    /**
     * Restituisce la rappresentazione in stringa della proiezione
     * @return stringa con informazioni della proiezione
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
