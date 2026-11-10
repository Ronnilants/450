# M450 – Testlevels

Quelle: [UEBUNGEN.md](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/test-levels/UEBUNGEN.md)

---

## Aufgabe 1 – Was wird wie bei SIX Group AG getestet?

SIX betreibt die Finanzmarkt-Infrastruktur der Schweiz: die Börse (SIX Swiss Exchange), den Zahlungsverkehr zwischen
den Banken (SIC, im Auftrag der SNB), Finanzinformationen und Banking Services. Ein Fehler betrifft direkt Banken und
den ganzen Finanzplatz. Deshalb wird sehr gründlich und auf allen Levels getestet, und SIX untersteht der Aufsicht
von FINMA/SNB.

> ✏️ Die markierten Stellen beschreiben ein typisches Team bei SIX. Bitte an das eigene Team anpassen.

### Mit welchen Test Levels hatten wir bereits zu tun?

| Test Level | Wer | Beispiel bei SIX |
|---|---|---|
| **Unit Test** | Entwickler | JUnit-Tests z.B. für die Berechnung von Gebühren oder das Validieren einer Zahlungsnachricht |
| **Component Test** | Entwickler | Service mit mehreren Klassen; Datenbank und Schnittstellen zu anderen Systemen werden gemockt |
| **Integration Test** | Entwickler / Test-Team | Zusammenspiel mit Datenbank, Message Queues und Schnittstellen zu anderen SIX-Systemen bzw. Banken |
| **System Test** | Test-Team / QA | Ganze Abläufe in einer produktionsnahen Testumgebung, inkl. **Performance-/Lasttests** (viele Börsenaufträge oder Zahlungen gleichzeitig) und **Security-Tests** |
| **Acceptance Test (UAT)** | Fachbereich / Kunden (Banken) | Fachbereich nimmt neue Funktionen ab; bei Änderungen an Schnittstellen testen teilnehmende Banken in einer Testumgebung mit |

### Wann werden Tests ausgeführt?

* **Lokal beim Entwickeln:** Unit Tests in der IDE vor jedem Commit.
* **Bei jedem Pull/Merge Request:** CI-Pipeline (✏️ z.B. Jenkins / GitLab CI) führt Build, Unit-, Component- und
  Integration-Tests aus, dazu Code-Analyse (✏️ z.B. SonarQube). Schlägt ein Test fehl, wird nicht gemergt.
* **Nach dem Deployment auf die Testumgebungen** (✏️ z.B. DEV → TEST → Abnahme): System-, Regressions-,
  Performance- und Security-Tests.
* **Vor dem Release:** Abnahme durch den Fachbereich. Das Release läuft über einen geregelten
  **Change-Management-Prozess** (Freigabe nötig, oft in fixen Wartungsfenstern, z.B. am Wochenende).
* **Nach dem Release:** Smoke Tests und Monitoring der Produktion.

### Haben wir dedizierte Testing- oder QA-Teams?

**Ja.** Als grosse Firma mit kritischer Infrastruktur hat SIX neben den Entwicklern auch Tester bzw. QA-Spezialisten
und eigene Testumgebungen. ✏️ Je nach Team sind die Tester direkt im agilen Team (Scrum) oder in einem separaten Test-Team.

* **Entwickler:** Unit-, Component- und Integration-Tests, Code Reviews (4-Augen-Prinzip)
* **Tester/QA:** System-, Regressions- und Performance-Tests, oft automatisiert
* **Security-Team:** Security- und Penetrationstests
* **Fachbereich/Kunden:** Abnahmetests

### Wie sieht unser Testing Life Cycle aus?

```
Anforderung (User Story + Akzeptanzkriterien, ggf. regulatorische Vorgaben)
        │
        ▼
Testplanung ── Welche Tests auf welchem Level? Risiko-Einschätzung
        │
        ▼
Entwicklung + Unit/Component Tests (Entwickler)
        │
        ▼
Pull Request ── Code Review + CI-Pipeline (automatische Tests, Code-Analyse)
        │
        ▼
Testumgebungen ── Integration-, System-, Regressions-, Performance-, Security-Tests (QA)
        │
        ▼
Abnahmetest (UAT) durch Fachbereich / Kunden
        │
        ▼
Change-Freigabe ──► Release Produktion (Wartungsfenster) ── Smoke Test, Monitoring
        │
        ▼
Gefundene Bugs ──► Ticket ──► zurück in die Entwicklung (neuer Test, der den Bug abdeckt)
```

Das entspricht dem klassischen **Software Testing Life Cycle (STLC)**:
Anforderungsanalyse → Testplanung → Testfall-Erstellung → Testumgebung bereitstellen → Testdurchführung → Testabschluss.
Bei SIX wird zusätzlich alles **dokumentiert** (Testprotokolle, Freigaben), damit es bei Audits nachweisbar ist.

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
