

import java.time.LocalDate;

/**
 * Classe che rappresenta un bigliettaio nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Bigliettaio extends Utente {
    private static final long serialVersionUID = 1L;
    
    /**
     * Costruttore per la creazione di un bigliettaio
     * @param nome il nome del bigliettaio
     * @param cognome il cognome del bigliettaio
     * @param username l'username del bigliettaio
     * @param password la password del bigliettaio
     * @param dataNascita la data di nascita
     * @param luogoDomicilio il luogo del domicilio
     */
    public Bigliettaio(String nome, String cognome, String username, String password,
                      LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "bigliettaio");
    }
    
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
