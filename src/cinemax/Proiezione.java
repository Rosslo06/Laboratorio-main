

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
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Film getFilm() {
        return film;
    }
    
    public void setFilm(Film film) {
        this.film = film;
    }
    
    public LocalDateTime getDataOraProiezione() {
        return dataOraProiezione;
    }
    
    public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
        this.dataOraProiezione = dataOraProiezione;
    }
    
    public double getCostoBiglietto() {
        return costoBiglietto;
    }
    
    public void setCostoBiglietto(double costoBiglietto) {
        this.costoBiglietto = costoBiglietto;
    }
    
    public int getCapacitaSala() {
        return CAPACITA_SALA;
    }
    
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
