public class Regista {
    private String nome;
    private String cognome;

    public Regista(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
    
     @Override
    public String toString() {
        return nome + " " + cognome;
    }
}
