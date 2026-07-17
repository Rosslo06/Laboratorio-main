

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
    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Proiezione getProiezione() {
        return proiezione;
    }
    
    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }
    
    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }
    
    public void setNumeroBiglietti(int numeroBiglietti) {
        this.numeroBiglietti = numeroBiglietti;
        this.costoTotale = proiezione.getCostoBiglietto() * numeroBiglietti;
    }
    
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    
    public double getCostoTotale() {
        return costoTotale;
    }
    
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
