import java.time.LocalDate;

/**
 * Classe che rappresenta un bigliettaio nel sistema CineMax
 * Un bigliettaio gestisce le prenotazioni
 * @author Andrea
 * @version 1.0
 */
public class Bigliettaio extends Utente {
    
    
    /**
     * Costruttore per la creazione di un bigliettaio
     */
    public Bigliettaio(String nome, String cognome, String username, String password,
                      LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "bigliettaio");
    }
    /**
     * Restituisce una rappresentazione testuale del bigliettaio
     * @return una stringa che rappresenta il bigliettaio
     */
    @Override
    public String toString() {
        return "Bigliettaio{" +
                "nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", luogoDomicilio='" + getLuogoDomicilio() + '\'' +
                '}';
    }
}