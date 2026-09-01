package cinemax;

import java.time.LocalDate;

/**
 * Classe che rappresenta un proiezionista nel sistema CineMax.
 * Estende la classe Utente e ha i permessi per aggiungere e gestire le proiezioni.
 * 
 * @author Andrea
 * @version 1.0
 */
public class Proiezionista extends Utente {

    /**
     * Costruttore per la creazione di un account Proiezionista.
     * 
     * @param nome il nome anagrafico del proiezionista
     * @param cognome il cognome anagrafico del proiezionista
     * @param username l'username univoco per l'accesso
     * @param password la password in chiaro
     * @param dataNascita la data di nascita del proiezionista
     * @param luogoDomicilio la città o luogo di domicilio
     */
    public Proiezionista(String nome, String cognome, String username, String password,
            LocalDate dataNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataNascita, luogoDomicilio, "proiezionista");
    }

    /**
     * Restituisce una rappresentazione testuale dei dati del proiezionista.
     * @return una stringa formattata con i dettagli dell'utente proiezionista
     */
    @Override
    public String toString() {
        return "Proiezionista{nome='" + getNome() + "', cognome='" + getCognome() + 
               "', username='" + getUsername() + "', luogoDomicilio='" + getLuogoDomicilio() + "'}";
    }
}