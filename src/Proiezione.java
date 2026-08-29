
import java.time.LocalDateTime;

/**
 * Classe che rappresenta una proiezione nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Proiezione {
   
    
    private int id;
    private Film film;
    private LocalDateTime dataOraProiezione;
    private double costoBiglietto;
    private int capacitaSala = 200;
    
    /**
     * Costruttore per la creazione di una proiezione
     */
    public Proiezione(int id, Film film, LocalDateTime dataOraProiezione, double costoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOraProiezione = dataOraProiezione;
        this.costoBiglietto = costoBiglietto;
    }
    
    // ============ GETTER E SETTER ============
    
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
        return capacitaSala;
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