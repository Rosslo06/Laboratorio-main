# 🎬 CineMax - Sistema di Gestione Prenotazioni Cinematografiche

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Architettura](https://img.shields.io/badge/Architettura-MVC_TUI-007396?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completato-28a745?style=for-the-badge)
![Persistenza](https://img.shields.io/badge/Persistenza-CSV-28a745?style=for-the-badge)

CineMax è una piattaforma software interattiva a riga di comando (TUI) sviluppata in **Java**. Il progetto simula la gestione completa di un cinema multisala, coprendo tutte le necessità operative: dalla consultazione pubblica del palinsesto (lato cliente), all'emissione e verifica dei ticket in cassa (lato biglietteria), fino alla programmazione e modifica degli slot orari dei film (lato back-office).

Questo applicativo è stato sviluppato come progetto per il corso di **Laboratorio Interdisciplinare A** (Anno Accademico 2025/2026) presso l'**Università degli Studi dell'Insubria**.

---

## 👥 Team di Sviluppo

| Nominativo | Matricola | Sede |
| :--- | :---: | :---: |
| **Rossetti Andrea** | `765635` | (VA) |
| **Segato Alessandro** | `766931` | (VA) |
| **Bergamo Emma** | `766925` | (VA) |
| **Piccolo Matteo** | `765213` | (VA) |

---

## ✨ Caratteristiche Principali

Il sistema è basato sui principi della programmazione Object-Oriented (OOP) ed è completamente **standalone**, non richiedendo l'ausilio di alcun database relazionale esterno o librerie di terze parti.

*   **Autenticazione basata sui Ruoli (RBAC):** Il sistema espone menu contestuali e flussi logici separati in base al tipo di accesso.
    *   👤 **Guest / Cliente:** Esplorazione del palinsesto tramite filtri incrociati (titolo, genere, date, costo), creazione autonoma di prenotazioni e gestione dei propri ticket.
    *   🎟️ **Bigliettaio:** Interfacce rapide per validare le prenotazioni odierne o ricercare ticket smarriti dai clienti in tempo reale.
    *   📽️ **Proiezionista:** Diritti amministrativi per la pianificazione (Aggiungi/Modifica/Rimuovi) del palinsesto, protetti da logiche anti-conflitto orario e vincoli sui posti già venduti.
*   **Motore di Ricerca Flessibile:** Pipeline di ricerca ottimizzata per permettere l'inserimento di filtri opzionali multipli.
*   **Persistenza Dati Ibrida:** Scrittura in tempo reale su file testuali strutturati (`.txt`) e supporto integrato per l'importazione di palinsesti massivi tramite file `.csv` con parsing a stati finiti.
*   **Gestione Sicura dei Conflitti:** Il sistema calcola *on-the-fly* l'occupazione della singola sala (capacità standard: 200 posti) prevenendo l'overbooking e vietando la rimozione di film se vi sono già clienti prenotati per lo slot.

---

## 📁 Struttura del Progetto

Il progetto impone la seguente alberatura delle cartelle per garantire la corretta compilazione ed esecuzione.

```
Laboratorio-main/
├── bin/                      # Directory di output
│   └── cinemax/
│       └── CineMax.jar       # Eseguibile compilato del progetto
├── data/                     # Livello di persistenza / In-Memory Storage
├── doc/                      # Documentazione del progetto
│   ├── javadoc/              # Documentazione tecnica (JavaDoc)
│   └── Manuali/              # Manuale utente e manuale tecnico
├── src/                      # Codice sorgente Java
│   └── cinemax/              # Package principale
│       ├── CineMax-main/     
│       ├── Bigliettaio.java
│       ├── CineMax.java
│       ├── Cliente.java
│       ├── Film.java
│       ├── GestoreDati.java
│       ├── Prenotazione.java
│       ├── Proiezione.java
│       ├── Proiezionista.java
│       ├── Regista.java
│       ├── Sistema.java
│       └── Utente.java
├── .gitignore                # Regole di esclusione per Git
├── autori.txt                # File anagrafico degli autori
└── README.md                 # Documentazione principale (questo file)
```

---

## 🚀 Setup & Esecuzione Rapida

L'applicazione richiede esclusivamente il **Java Development Kit (JDK) versione 11 o superiore**.

1.  **Preparazione:**
    Clona la repository o crea l'alberatura riportata sopra. Assicurati che i file `.java` si trovino dentro `src/` e il file iniziale `proiezioni.txt` in `data/`.
2.  **Compilazione (dal terminale nella root del progetto):**
    ```bash
    javac -d bin src/*.java
    ```
3.  **Avvio dell'applicazione:**
    ```bash
    java -jar bin/CineMax.jar
    ```

---

## 🔑 Credenziali di Test Pre-Caricate

Per facilitare la valutazione del progetto, il sistema carica automaticamente un database iniziale di dipendenti.

### Proiezionista (Gestione Palinsesto)
- **Username:** `mrossi` (Marco Rossi)
- **Password:** `password123`
- *(Alternativa: `ebianchi`)*

### Bigliettaio (Gestione Cassa)
- **Username:** `gverdi` (Giuseppe Verdi)
- **Password:** `password123`
- *(Alternative: `fneri`, `agalli`, `sferrari`, `lconti`)*

### Cliente
I clienti non sono pre-caricati. È possibile utilizzare la funzione di registrazione "📝 Registrazione" direttamente dal menu principale della TUI per creare un nuovo account Cliente e testare il flusso d'acquisto.