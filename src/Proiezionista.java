import java.time.LocalDate;

/**
 * Classe che rappresenta un proiezionista nel sistema CineMax
 * Un proiezionista aggiunge e gestisce le proiezioni
 * @author Andrea
 * @version 1.0
 */
public class Proiezionista extends Utente {
    private static final long serialVersionUID = 1L;
    
    /**
     * Costruttore per la creazione di un proiezionista
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