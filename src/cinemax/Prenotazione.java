

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe che rappresenta una prenotazione nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Prenotazione implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codicePrenotazione;
    private Cliente cliente;
    private Proiezione proiezione;
    private int numeroBiglietti;
    private LocalDateTime dataCreazione;
    private double costoTotale;
    
    /**
     * Costruttore per la creazione di una prenotazione
     * @param cliente il cliente che effettua la prenotazione
     * @param proiezione la proiezione prenotata
     * @param numeroBiglietti il numero di biglietti
     */
    public Prenotazione(Cliente cliente, Proiezione proiezione, int numeroBiglietti) {
        this.codicePrenotazione = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.cliente = cliente;
        this.proiezione = proiezione;
        this.numeroBiglietti = numeroBiglietti;
        this.dataCreazione = LocalDateTime.now();
        this.costoTotale = proiezione.getCostoBiglietto() * numeroBiglietti;
    }
    
    // Getter e Setter
    /**
     * Ottiene il codice univoco della prenotazione
     * @return il codice prenotazione
     */
    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }
    
    /**
     * Ottiene il cliente che ha effettuato la prenotazione
     * @return il cliente
     */
    public Cliente getCliente() {
        return cliente;
    }
    
    /**
     * Imposta il cliente della prenotazione
     * @param cliente il cliente da impostare
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Ottiene la proiezione prenotata
     * @return la proiezione
     */
    public Proiezione getProiezione() {
        return proiezione;
    }
    
    /**
     * Imposta la proiezione della prenotazione
     * @param proiezione la proiezione da impostare
     */
    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }
    
    /**
     * Ottiene il numero di biglietti prenotati
     * @return il numero di biglietti
     */
    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }
    
    /**
     * Imposta il numero di biglietti e aggiorna il costo totale
     * @param numeroBiglietti il numero di biglietti da impostare
     */
    public void setNumeroBiglietti(int numeroBiglietti) {
        this.numeroBiglietti = numeroBiglietti;
        this.costoTotale = proiezione.getCostoBiglietto() * numeroBiglietti;
    }
    
    /**
     * Ottiene la data e ora di creazione della prenotazione
     * @return la data e ora di creazione
     */
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    
    /**
     * Ottiene il costo totale della prenotazione
     * @return il costo totale in euro
     */
    public double getCostoTotale() {
        return costoTotale;
    }
    
    /**
     * Restituisce la rappresentazione in stringa della prenotazione
     * @return stringa con informazioni della prenotazione
     */
    @Override
    public String toString() {
        return "Prenotazione{" +
                "codicePrenotazione='" + codicePrenotazione + '\'' +
                ", cliente='" + cliente.getNome() + " " + cliente.getCognome() + '\'' +
                ", film='" + proiezione.getFilm().getTitolo() + '\'' +
                ", dataProiezione=" + proiezione.getDataOraProiezione() +
                ", numeroBiglietti=" + numeroBiglietti +
                ", costoTotale=" + costoTotale + "€" +
                '}';
    }
}
