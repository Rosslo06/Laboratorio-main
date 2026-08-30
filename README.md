# Laboratorio - CineMax Sistema di Prenotazione Cinematografica

## Descrizione Progetto
CineMax è un sistema completo di prenotazione cinematografica sviluppato in Java. Il progetto gestisce film, proiezioni, utenti con diversi ruoli (clienti, bigliettai, proiezionisti) e prenotazioni di biglietti.

## Divisione del Lavoro

Il progetto è stato diviso in quattro moduli principali, affidati ai seguenti membri del team:

### 1. **Rossetti Andrea** - Gestione Film, Proiezioni e Persistenza Dati
**File principali:**
- `Film.java`: Classe che rappresenta i film nel sistema
- `Proiezione.java`: Classe che gestisce le proiezioni cinematografiche
- `GestoreDati.java`: Classe responsabile del caricamento e salvataggio dei dati su file
- `Regista.java`: Classe che rappresenta i registi (supporto a Film)

**Responsabilità:**
- Implementazione della struttura dati per film e proiezioni
- Gestione della persistenza dati (file txt)
- Serializzazione e deserializzazione delle proiezioni
- Gestione dei formati di data/ora

### 2. **Segato Alessandro** - Gestione Utenti e Autenticazione
**File principali:**
- `Utente.java`: Classe astratta base per tutti gli utenti del sistema
- `Cliente.java`: Classe che rappresenta i clienti
- `Bigliettaio.java`: Classe che rappresenta i bigliettai
- `Proiezionista.java`: Classe che rappresenta i proiezionisti
- `Sistema.java`: Gestione della logica di login/registrazione

**Responsabilità:**
- Implementazione del sistema di autenticazione con hashing SHA-256
- Gestione della gerarchia di classi per i diversi ruoli
- Registrazione dei nuovi clienti
- Verifica delle credenziali degli utenti

### 3. **Piccolo Matteo** - Gestione Prenotazioni
**File principali:**
- `Prenotazione.java`: Classe che rappresenta le prenotazioni di biglietti
- `Sistema.java`: Metodi per creazione, modifica ed eliminazione prenotazioni

**Responsabilità:**
- Implementazione della struttura dati per le prenotazioni
- Logica di creazione prenotazioni con verifica disponibilità
- Modifica e cancellazione prenotazioni
- Calcolo dei costi totali e gestione dei codici prenotazione
- Validazioni temporali e spaziali (posti disponibili)

### 4. **Bergamo Emma** - Interfaccia Utente (TUI) e Ricerca
**File principali:**
- `CineMax.java`: Classe principale con l'interfaccia utente terminale
- `Sistema.java`: Metodi di ricerca e visualizzazione

**Responsabilità:**
- Sviluppo del menu principale e dei sottomenu per ogni ruolo
- Interfaccia per ricerca proiezioni con filtri
- Visualizzazione dei dettagli delle proiezioni
- Menu specifico per clienti, bigliettai e proiezionisti
- Gestione dell'input utente
- Formattazione e visualizzazione dell'output

## Architettura del Sistema

### Struttura Classi
```
Utente (astratta)
  ├── Cliente
  ├── Bigliettaio
  └── Proiezionista

Film
  └── correlato a Regista

Proiezione
  └── contiene Film

Prenotazione
  ├── contiene Cliente
  └── contiene Proiezione

Sistema (logica principale)
GestoreDati (persistenza)
CineMax (interfaccia utente)
```

### Ruoli e Funzionalità

**Cliente:**
- Ricerca proiezioni
- Visualizza dettagli proiezioni
- Crea prenotazioni
- Modifica/cancella prenotazioni personali
- Visualizza storico prenotazioni

**Bigliettaio:**
- Ricerca prenotazioni
- Visualizza prenotazioni di oggi
- Accesso ai dati delle proiezioni

**Proiezionista:**
- Aggiunge nuove proiezioni
- Modifica proiezioni (se non hanno prenotazioni)
- Elimina proiezioni
- Visualizza tutte le proiezioni

**Guest:**
- Ricerca proiezioni
- Visualizza dettagli proiezioni
- Nessun accesso alle funzionalità protette

## Tecnologie Utilizzate
- **Linguaggio**: Java
- **Persistenza**: File di testo (CSV-like)
- **Sicurezza**: Hash SHA-256 per password
- **Data/Ora**: Java Time API (LocalDate, LocalDateTime)
- **Interfaccia**: Terminal User Interface (TUI)

## Come Eseguire
1. Compilare: `javac CineMax.java`
2. Eseguire: `java CineMax`
3. Seguire le istruzioni a schermo

## Autori
- **Rossetti Andrea** - Film, Proiezioni, Persistenza Dati
- **Segato Alessandro** - Autenticazione e Gestione Utenti
- **Piccolo Matteo** - Gestione Prenotazioni
- **Bergamo Emma** - Interfaccia Utente e Ricerca
