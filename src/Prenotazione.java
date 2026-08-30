
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe che rappresenta una prenotazione nel sistema CineMax
 * 
 * @author Andrea
 * @version 1.0
 */
public class Prenotazione {
    

    private String codicePrenotazione;
    private Cliente cliente;
    private Proiezione proiezione;
    private int numeroBiglietti;
    private LocalDateTime dataCreazione;
    private double costoTotale;

    /**
     * Costruttore per la creazione di una prenotazione
     * 
     * @param cliente         il cliente che effettua la prenotazione
     * @param proiezione      la proiezione per cui viene effettuata la prenotazione
     * @param numeroBiglietti il numero di biglietti prenotati
     * 
     */
    public Prenotazione(Cliente cliente, Proiezione proiezione, int numeroBiglietti) {
        // Genera un codice univoco per la prenotazione
        this.codicePrenotazione = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.cliente = cliente;
        this.proiezione = proiezione;
        this.numeroBiglietti = numeroBiglietti;
        this.dataCreazione = LocalDateTime.now();

        // Calcola il costo totale
        this.costoTotale = proiezione.getCostoBiglietto() * numeroBiglietti;
    }

    // ============ GETTER E SETTER ============
    /**
     * Restituisce il codice univoco della prenotazione
     * 
     * @return il codice univoco della prenotazione
     */
    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    /**
     * Restituisce il cliente associato alla prenotazione
     * 
     * @return il cliente associato alla prenotazione
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Imposta il cliente associato alla prenotazione
     * 
     * @param cliente
     */

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Restituisce la proiezione associata alla prenotazione
     * 
     * @return la proiezione associata alla prenotazione
     */

    public Proiezione getProiezione() {
        return proiezione;
    }

    /**
     * Imposta la proiezione associata alla prenotazione
     * 
     * @param proiezione
     */
    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    /**
     * Restituisce il numero di biglietti prenotati
     * 
     * @return il numero di biglietti prenotati
     */
    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }

    /**
     * Imposta il numero di biglietti prenotati e aggiorna il costo totale
     * 
     * @param numeroBiglietti
     */

    public void setNumeroBiglietti(int numeroBiglietti) {
        this.numeroBiglietti = numeroBiglietti;
        // Aggiorna il costo totale
        this.costoTotale = proiezione.getCostoBiglietto() * numeroBiglietti;
    }

    /**
     * Restituisce la data e l'ora di creazione della prenotazione
     * 
     * @return la data e l'ora di creazione della prenotazione
     */

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Restituisce il costo totale della prenotazione
     * 
     * @return il costo totale della prenotazione
     */
    public double getCostoTotale() {
        return costoTotale;
    }

    /**
     * Restituisce una rappresentazione testuale della prenotazione
     * 
     * @return una rappresentazione testuale della prenotazione
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