package cinemax;

import java.time.LocalDate;

/**
 * Classe che rappresenta un bigliettaio nel sistema CineMax.
 * Estende la classe Utente e ha i permessi per gestire e visualizzare le prenotazioni in cassa.
 * 
 * @author Andrea
 * @version 1.0
 */
public class Bigliettaio extends Utente {
    
    /**
     * Costruttore per la creazione di un account Bigliettaio.
     * 
     * @param nome il nome anagrafico del bigliettaio
     * @param cognome il cognome anagrafico del bigliettaio
     * @param username l'username univoco per l'accesso
     * @param password la password in chiaro
     * @param dataNascita la data di nascita del bigliettaio
     * @param luogoDomicilio la città o luogo di domicilio
     */
    public Bigliettaio(String nome, String cognome, String username, String password,
                      LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "bigliettaio");
    }
    
    /**
     * Restituisce una rappresentazione testuale dei dati del bigliettaio.
     * @return una stringa formattata con i dettagli dell'utente bigliettaio
     */
    @Override
    public String toString() {
        return "Bigliettaio{nome='" + getNome() + "', cognome='" + getCognome() + 
               "', username='" + getUsername() + "', luogoDomicilio='" + getLuogoDomicilio() + "'}";
    }
}