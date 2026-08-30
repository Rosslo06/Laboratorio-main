import java.time.LocalDate;

/**
 * Classe che rappresenta un proiezionista nel sistema CineMax
 * Un proiezionista aggiunge e gestisce le proiezioni
 * 
 * @author Andrea
 * @version 1.0
 */
public class Proiezionista extends Utente {
    

    /**
     * Costruttore per la creazione di un proiezionista
     * 
     * @param nome           il nome del proiezionista
     * @param cognome        il cognome del proiezionista
     * @param username       l'username del proiezionista
     * @param password       la password del proiezionista
     * @param dataNascita    la data di nascita del proiezionista
     * @param luogoDomicilio il luogo di domicilio del proiezionista
     * 
     */
    public Proiezionista(String nome, String cognome, String username, String password,
            LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "proiezionista");
    }

    /**
     * Restituisce una rappresentazione testuale dei dati del proiezionista
     * 
     * @return una stringa che rappresenta i dati del proiezionista
     */

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