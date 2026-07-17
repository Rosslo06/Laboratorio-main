

import java.time.LocalDate;

/**
 * Classe che rappresenta un cliente nel sistema CineMax
 * @author Andrea
 * @version 1.0
 */
public class Cliente extends Utente {
    private static final long serialVersionUID = 1L;
    
    /**
     * Costruttore per la creazione di un cliente
     * @param nome il nome del cliente
     * @param cognome il cognome del cliente
     * @param username l'username del cliente
     * @param password la password del cliente
     * @param dataNascita la data di nascita
     * @param luogoDomicilio il luogo del domicilio
     */
    public Cliente(String nome, String cognome, String username, String password,
                   LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "cliente");
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", luogoDomicilio='" + getLuogoDomicilio() + '\'' +
                '}';
    }
}
