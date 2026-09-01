package cinemax;

import java.time.LocalDate;

/**
 * Classe che rappresenta un cliente nel sistema CineMax.
 * Estende la classe Utente e permette di effettuare e gestire prenotazioni.
 * 
 * @author Andrea
 * @version 1.0
 */
public class Cliente extends Utente {
    
    /**
     * Costruttore per la creazione di un account Cliente.
     * 
     * @param nome il nome anagrafico del cliente
     * @param cognome il cognome anagrafico del cliente
     * @param username l'username univoco per l'accesso
     * @param password la password in chiaro (che verrà cifrata dalla superclasse)
     * @param dataNascita la data di nascita del cliente (può essere null)
     * @param luogoDomicilio la città o luogo di domicilio del cliente
     */
    public Cliente(String nome, String cognome, String username, String password,
                   LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "cliente");
    }
    
    /**
     * Restituisce una rappresentazione testuale dei dati del cliente.
     * @return una stringa formattata con i dettagli dell'utente cliente
     */
    @Override
    public String toString() {
        return "Cliente{nome='" + getNome() + "', cognome='" + getCognome() + 
               "', username='" + getUsername() + "', luogoDomicilio='" + getLuogoDomicilio() + "'}";
    }
}