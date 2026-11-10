# M450 – Testlevels

Quelle: [UEBUNGEN.md](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/test-levels/UEBUNGEN.md)

---

## Aufgabe 1 – Was wird wie in unserer Firma getestet?

> ✏️ **Hinweis:** Die Antworten beschreiben ein typisches Software-Team in einem Lehrbetrieb.
> Die mit ✏️ markierten Stellen an die eigene Firma anpassen.

### Mit welchen Test Levels hatten wir bereits zu tun?

| Test Level | Bei uns? | Wer | Beispiel |
|---|---|---|---|
| **Unit Test** | ✅ ja | Entwickler | JUnit-Tests für Service-Klassen, z.B. Berechnung von Preisen/Gebühren |
| **Component Test** | ✅ ja | Entwickler | Mehrere Klassen zusammen, Datenbank wird mit Mockito gemockt |
| **Integration Test** | ✅ teilweise | Entwickler | Spring-Boot-Tests mit echter Testdatenbank (Docker), REST-Aufrufe mit Postman |
| **System Test** | ✅ ja | Tester / Fachbereich | Ganze Abläufe in der Testumgebung (TEST/INT) nach Testprotokoll |
| **Acceptance Test (UAT)** | ✅ ja | Kunde / Product Owner | Abnahme der User Stories am Ende des Sprints bzw. vor dem Release |

### Wann werden Tests ausgeführt?

* **Lokal beim Entwickeln:** Unit Tests in der IDE vor jedem Commit.
* **Bei jedem Push / Merge Request:** CI-Pipeline (z.B. GitLab CI / GitHub Actions / Jenkins ✏️) führt Build,
  Unit-, Component- und Integration-Tests aus. Schlägt ein Test fehl, kann nicht gemergt werden.
* **Nach dem Deployment auf die Testumgebung:** Manuelle System- und Regressionstests.
* **Vor dem Release:** Abnahmetest durch den Kunden/Product Owner, danach Deployment auf Produktion.
* **Nach dem Release:** Smoke Test auf Produktion (Login, wichtigste Funktion).

### Haben wir dedizierte Testing- oder QA-Teams?

✏️ **Nein, kein eigenes QA-Team.** Die Entwickler sind für Unit-, Component- und Integration-Tests selbst verantwortlich.
System- und Abnahmetests macht der Fachbereich bzw. der Product Owner. Zusätzlich gibt es **Code Reviews**
(4-Augen-Prinzip) bei jedem Merge Request.

*Vergleich:* Grössere Firmen (Banken, Versicherungen) haben oft ein eigenes QA-Team, das System- und Integrationstests
übernimmt und Testfälle in einem Tool wie Jira/Xray verwaltet.

### Wie sieht unser Testing Life Cycle aus?

```
Anforderung (User Story + Akzeptanzkriterien)
        │
        ▼
Testplanung ── Welche Tests auf welchem Level? (in der Sprint-Planung)
        │
        ▼
Entwicklung + Unit/Component Tests (Entwickler)
        │
        ▼
Merge Request ── Code Review + CI-Pipeline (automatische Tests)
        │
        ▼
Deployment Testumgebung ── System- und Regressionstests
        │
        ▼
Abnahmetest (UAT) durch Kunde / PO
        │
        ▼
Release Produktion ── Smoke Test, Monitoring
        │
        ▼
Gefundene Bugs ──► Ticket ──► zurück in die Entwicklung (neuer Test, der den Bug abdeckt)
```

Das entspricht dem klassischen **Software Testing Life Cycle (STLC)**:
Anforderungsanalyse → Testplanung → Testfall-Erstellung → Testumgebung bereitstellen → Testdurchführung → Testabschluss.

---

## Aufgabe 2 – Begriffe einordnen

### Die Begriffe

| Begriff | Frage, die er beantwortet | Beispiele |
|---|---|---|
| **Testing Approach** (Testansatz) | *Wie* gehen wir grundsätzlich vor? Woher kommen die Testfälle? | Black-Box, White-Box, Grey-Box; manuell vs. automatisiert; statisch (Review) vs. dynamisch (Ausführen); Test-First (TDD) |
| **Testing Levels** (Teststufen) | *Wann/auf welcher Ebene* wird getestet? Was ist das Testobjekt? | Unit → Component → Integration → System → Acceptance |
| **Testing Types** (Testarten) | *Was* wird geprüft (welche Eigenschaft)? | Funktional; nicht-funktional (Performance, Last, Stress, Usability, Security); Regression; Smoke |
| **Testing Techniques** (Testtechniken) | *Mit welcher Methode* leite ich konkrete Testfälle ab? | Äquivalenzklassen, Grenzwertanalyse, Entscheidungstabellen, Zustandsübergänge (Black-Box); Anweisungs-/Zweigabdeckung (White-Box) |
| **Testing Tactics** (Testtaktiken) | *Wie setzen wir es konkret um* (Werkzeuge, Organisation, Priorität)? | Mocks/Stubs, Testdaten, risikobasiertes Testen, Testautomatisierung in der CI-Pipeline, explorative Tests |

### Abhängigkeiten

```
Testing Approach  (Black-Box / White-Box, manuell / automatisiert)
      │ bestimmt, welche
      ▼
Testing Techniques  (Grenzwerte, Äquivalenzklassen, Zweigabdeckung …)
      │ werden angewendet auf
      ▼
Testing Levels  (Unit → Component → Integration → System → Acceptance)
      │ auf jedem Level prüft man verschiedene
      ▼
Testing Types  (funktional, Performance, Security, Regression …)
      │ umgesetzt durch
      ▼
Testing Tactics  (Mocks, Testdaten, CI-Pipeline, Priorisierung nach Risiko)
```

* **Approach → Technique:** Der Ansatz legt fest, welche Techniken möglich sind. Grenzwertanalyse ist eine Black-Box-Technik
  (nur Spezifikation nötig), Zweigabdeckung eine White-Box-Technik (Code nötig).
* **Level → Approach:** Je höher das Level, desto mehr Black-Box. Unit- und Component-Tests sind White-Box,
  System- und Abnahmetests Black-Box, Integrationstests beides.
* **Level → Type:** Nicht jede Testart passt auf jedes Level. Funktionale Tests gibt es auf allen Levels,
  Performance-, Last- und Usability-Tests erst auf System-Level (braucht das ganze System in einer produktionsnahen Umgebung).
* **Level → Tactic:** Auf Unit/Component-Level werden Abhängigkeiten (DB, externe API) **gemockt**,
  ab Integration-Level werden sie echt verwendet.
* **Regressionstest** ist kein eigenes Level, sondern eine Testart, die auf jedem Level wiederholt wird (meist automatisiert).

### Übersicht: Wer macht was auf welchem Level?

| Level | Wer | Approach | Typische Types | Tactics | Geschwindigkeit |
|---|---|---|---|---|---|
| Unit | Entwickler | White-Box, automatisiert | funktional, Regression | isoliert, alles gemockt | sehr schnell (ms) |
| Component | Entwickler | White-Box, automatisiert | funktional | DB/APIs gemockt | schnell |
| Integration | Entwickler / Tester | White- & Black-Box | funktional, Schnittstellen | echte DB / Testcontainer | mittel (s) |
| System | Tester / QA | Black-Box, manuell + automatisiert | funktional + nicht-funktional (Performance, Security, Usability) | produktionsnahe Umgebung | langsam (min) |
| Acceptance | Kunde / Business | Black-Box, meist manuell | funktional (Akzeptanzkriterien) | User Stories, Abnahmeprotokoll | am langsamsten |

### Beispiel: Bank-Software aus der [Teststrategie](../teststrategie/README.md)

| Level | Konkreter Test |
|---|---|
| Unit | `Account.withdraw(200)` bei Saldo 100 gibt `false` zurück ([`BankTestDriver`](../teststrategie/bank-software/src/test/java/ch/tbz/bank/software/BankTestDriver.java)) |
| Component | `Counter.transferAmount()` mit `Bank` und zwei `Account`s, Eingabe simuliert |
| Integration | `ExchangeRateOkhttp.getExchangeRate("CHF","USD")` ruft die echte Wechselkurs-API auf |
| System | Programm starten, Konto wählen, Geld überweisen (Black-Box-Tests aus der Teststrategie) |
| Acceptance | Bankmitarbeiter prüft: "Ich kann ein Konto eröffnen, Geld einzahlen und auf ein anderes Konto überweisen." |
