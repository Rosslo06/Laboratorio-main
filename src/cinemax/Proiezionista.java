

import java.time.LocalDate;

/**
 * Classe che rappresenta un proiezionista nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Proiezionista extends Utente {
    private static final long serialVersionUID = 1L;
    
    /**
     * Costruttore per la creazione di un proiezionista
     * @param nome il nome del proiezionista
     * @param cognome il cognome del proiezionista
     * @param username l'username del proiezionista
     * @param password la password del proiezionista
     * @param dataNascita la data di nascita
     * @param luogoDomicilio il luogo del domicilio
     */
    public Proiezionista(String nome, String cognome, String username, String password,
                        LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "proiezionista");
    }
    
    @Override
    public String toString() {
        return "Proiezionista{" +
                "nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", luogoDomicilio='" + getLuogoDomicilio() + '\'' +
                '}';
    }
}
