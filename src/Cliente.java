import java.time.LocalDate;

/**
 * Classe che rappresenta un cliente nel sistema CineMax
 * Un cliente può prenotare film
 * @author Andrea
 * @version 1.0
 */
public class Cliente extends Utente {
    private static final long serialVersionUID = 1L;
    
    /**
     * Costruttore per la creazione di un cliente
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