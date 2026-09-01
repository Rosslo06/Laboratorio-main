package cinemax;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una proiezione nel sistema CineMax.
 * Associa un film a una data, un orario, un costo e alla capienza della sala.
 * 
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
     * Costruttore per la creazione di una nuova proiezione.
     * 
     * @param id l'identificativo univoco della proiezione
     * @param film l'oggetto Film che verrà proiettato
     * @param dataOraProiezione la data e l'ora esatta della proiezione
     * @param costoBiglietto il costo del biglietto in euro
     */
    public Proiezione(int id, Film film, LocalDateTime dataOraProiezione, double costoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOraProiezione = dataOraProiezione;
        this.costoBiglietto = costoBiglietto;
    }

    /**
     * Restituisce l'identificativo univoco della proiezione.
     * @return l'ID della proiezione
     */
    public int getId() { return id; }

    /**
     * Imposta l'identificativo univoco della proiezione.
     * @param id il nuovo ID da assegnare
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce il film associato alla proiezione.
     * @return l'oggetto Film proiettato
     */
    public Film getFilm() { return film; }

    /**
     * Imposta o modifica il film della proiezione.
     * @param film il nuovo oggetto Film da proiettare
     */
    public void setFilm(Film film) { this.film = film; }

    /**
     * Restituisce la data e l'ora della proiezione.
     * @return la data e l'ora della proiezione
     */
    public LocalDateTime getDataOraProiezione() { return dataOraProiezione; }

    /**
     * Imposta una nuova data e ora per la proiezione.
     * @param dataOraProiezione la nuova data e ora da assegnare
     */
    public void setDataOraProiezione(LocalDateTime dataOraProiezione) { this.dataOraProiezione = dataOraProiezione; }

    /**
     * Restituisce il costo del biglietto per la proiezione.
     * @return il costo del biglietto in euro
     */
    public double getCostoBiglietto() { return costoBiglietto; }

    /**
     * Imposta il costo del biglietto per la proiezione.
     * @param costoBiglietto il nuovo costo del biglietto
     */
    public void setCostoBiglietto(double costoBiglietto) { this.costoBiglietto = costoBiglietto; }

    /**
     * Restituisce la capacità massima della sala.
     * @return il numero di posti totali (fissato a 200)
     */
    public int getCapacitaSala() { return capacitaSala; }

    /**
     * Restituisce una rappresentazione testuale della proiezione.
     * @return una stringa con i dettagli della proiezione
     */
    @Override
    public String toString() {
        return "Proiezione{id=" + id + ", film=" + film.getTitolo() +
               ", dataOraProiezione=" + dataOraProiezione + ", costoBiglietto=" + costoBiglietto + "€}";
    }
}