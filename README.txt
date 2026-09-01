
========================================
       CineMax - Progetto di Laboratorio
========================================

Autori / Gruppo:
- Rossetti Andrea
- Segato Alessandro
- Piccolo Matteo
- Bergamo Emma

Descrizione:
CineMax è un sistema software in Java per la gestione di un cinema monosala da 200 posti,
con supporto per la gestione del palinsesto (proiezioni), prenotazioni, ruoli utente
(Clienti, Proiezionisti, Bigliettai) e interfaccia a riga di comando (TUI).

Struttura del Repository:
- bin/         -> Contiene l'archivio eseguibile CineMax.jar
- doc/         -> Contiene il Manuale Utente (.pdf), Manuale Tecnico (.pdf) e la JavaDoc
- src/cinemax         -> Codice sorgente Java nel package cinemax
- data/        -> File di persistenza dei dati (utenti.csv, proiezioni.csv, prenotazioni.txt)
- autori.txt   -> Informazioni sui membri del team

Istruzioni per la Compilazione e l'Esecuzione:

1. Compilazione dei sorgenti:
   javac -d bin -sourcepath src src/cinemax/*.java

2. Generazione dell'archivio JAR eseguibile:
   jar cfe bin/CineMax.jar cinemax.CineMax -C bin .

3. Esecuzione dell'applicazione:
   java -jar bin/CineMax.jar
