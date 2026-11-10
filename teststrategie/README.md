# M450 – Teststrategie

Quelle: [UEBUNGEN.md](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/teststrategie/UEBUNGEN.md)

---

## Übung 1 – Rabattregeln Autohaus

> *Bei einem Kaufpreis von weniger als 15'000 CHF soll kein Rabatt gewährt werden. Bei einem Preis bis zu 20'000 CHF
> sind 5% Rabatt angemessen. Liegt der Kaufpreis unter 25'000 CHF sind 7% Rabatt möglich, darüber sind 8,5 % Rabatt zu gewähren.*

**Annahmen zu den Grenzen** (die Beschreibung ist nicht ganz eindeutig):

* "weniger als 15'000" → `< 15000` → 15'000 selbst gehört bereits zur 5 %-Klasse.
* "bis zu 20'000" → `<= 20000` (inklusive).
* "unter 25'000" → `< 25000`.
* "darüber" → `>= 25000`. Genau 25'000 ist in der Beschreibung eigentlich nicht geregelt ("unter 25'000" vs. "darüber");
  da es nicht "unter" 25'000 ist, wird es der 8.5 %-Klasse zugeordnet. → **Rückfrage beim Auftraggeber nötig.**
* Ein Kaufpreis `<= 0` ist ungültig und muss abgelehnt werden (nicht spezifiziert → ebenfalls klären).

### Abstrakte Testfälle

| ID | Eingabe (Kaufpreis *x* in CHF) | Erwarteter Rabatt |
|---:|---|---|
| A1 | 0 < x < 15000 | 0 % |
| A2 | 15000 <= x <= 20000 | 5 % |
| A3 | 20000 < x < 25000 | 7 % |
| A4 | x >= 25000 | 8.5 % |
| A5 | x <= 0 (ungültig) | Fehlermeldung, keine Berechnung |

### Konkrete Testfälle

Pro Äquivalenzklasse ein Repräsentant und zusätzlich die **Grenzwerte** (jeweils knapp unter / auf / knapp über der Grenze, 5 Rappen Abstand).

| ID | Abstrakt | Kaufpreis (CHF) | Erwarteter Rabatt | Erwarteter Endpreis (CHF) |
|---:|---|---:|---:|---:|
| K1 | A1 | 10'000.00 | 0 % | 10'000.00 |
| K2 | A1 (Grenze) | 14'999.95 | 0 % | 14'999.95 |
| K3 | A2 (Grenze) | 15'000.00 | 5 % | 14'250.00 |
| K4 | A2 | 17'500.00 | 5 % | 16'625.00 |
| K5 | A2 (Grenze) | 20'000.00 | 5 % | 19'000.00 |
| K6 | A3 (Grenze) | 20'000.05 | 7 % | 18'600.05 |
| K7 | A3 | 22'500.00 | 7 % | 20'925.00 |
| K8 | A3 (Grenze) | 24'999.95 | 7 % | 23'249.95 |
| K9 | A4 (Grenze) | 25'000.00 | 8.5 % | 22'875.00 |
| K10 | A4 | 30'000.00 | 8.5 % | 27'450.00 |
| K11 | A5 (Grenze) | 0.00 | – | Fehlermeldung "ungültiger Preis" |
| K12 | A5 | -5'000.00 | – | Fehlermeldung "ungültiger Preis" |

**Beobachtung:** Die Testfälle zeigen eine Schwäche der Anforderung selbst. Ein Auto für 15'000 CHF kostet nach Rabatt
14'250 CHF und ist damit günstiger als eines für 14'999.95 CHF (K2 vs. K3). Dasselbe passiert bei 20'000 / 20'000.05
und 24'999.95 / 25'000. Das sollte mit dem Autohaus besprochen werden.

---

## Übung 2 – Black-Box-Tests Autovermietung

Getestete Webseite: **[sixt.ch](https://www.sixt.ch)** (andere Plattformen wie europcar.ch oder hertz.ch funktionieren analog).

Die 5 wichtigsten funktionalen Testfälle decken den Kernprozess *Fahrzeug finden → buchen → verwalten* ab:

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat | Status | Mögliche Ursache |
|---:|---|---|---|---|---|
| 1 | **Fahrzeugsuche mit gültigen Daten:** Abholort "Zürich Flughafen", Abholung morgen 10:00, Rückgabe in 3 Tagen 10:00 → "Fahrzeuge anzeigen" | Liste verfügbarer Fahrzeuge mit Preis für 3 Tage wird angezeigt; Ort und Daten sind korrekt übernommen. | | | |
| 2 | **Ungültiger Zeitraum:** Rückgabedatum liegt vor dem Abholdatum (bzw. Abholung in der Vergangenheit) | Suche wird nicht ausgeführt; verständliche Fehlermeldung oder Datum wird automatisch korrigiert. | | | |
| 3 | **Filter & Preis:** Suchresultat nach Fahrzeugkategorie (z.B. "SUV") und Getriebe "Automatik" filtern, ein Fahrzeug wählen | Nur passende Fahrzeuge werden angezeigt; der Gesamtpreis auf der Detailseite stimmt mit dem Preis in der Liste überein (inkl. gewählter Extras wie Kindersitz). | | | |
| 4 | **Buchung abschliessen** (Testumgebung/Testkreditkarte): Fahrzeug wählen, Fahrerdaten erfassen, bezahlen | Buchungsbestätigung mit Buchungsnummer wird angezeigt und per E-Mail versendet; Buchung erscheint im Kundenkonto. | | | |
| 5 | **Pflichtfeld-Validierung im Buchungsformular:** E-Mail ohne "@" eingeben, Pflichtfeld "Nachname" leer lassen, Fahrer unter Mindestalter angeben | Buchung kann nicht abgeschickt werden; fehlerhafte Felder werden markiert und erklärt. | | | |

Weitere sinnvolle Testfälle (nicht in den Top 5): Buchung stornieren/ändern, Login/Registrierung, Einwegmiete
(Rückgabe an anderem Ort → Zuschlag), Sprache/Währung wechseln.

> Die Spalten *Effektives Resultat*, *Status* und *Mögliche Ursache* werden beim manuellen Durchführen der Tests im Browser
> ausgefüllt. Testfall 4 darf auf der produktiven Seite nur bis vor die Zahlung durchgeführt werden.

---

## Übung 3 – Bank-Software

Code: [`bank-software/`](bank-software)

### Inbetriebnahme

Maven-Projekt (Java 20+, Abhängigkeiten `okhttp`, `gson`):

```bash
cd teststrategie/bank-software
mvn compile exec:java -Dexec.mainClass=ch.tbz.bank.software.Main
```

Beim Start werden 5 Konten angelegt:

| Nr | Name | Währung | Startsaldo |
|---:|---|---|---:|
| 1 | Rockefeller | USD | 1'500.00 |
| 2 | Gates | EUR | 2'000.00 |
| 3 | Musk | CHF | 23'500.00 |
| 4 | Bezos | EUR | 100.50 |
| 5 | Branson | USD | 1'500'000.00 |

### Black-Box-Testfälle

Alle Testfälle wurden mit Java 21 tatsächlich ausgeführt (Eingaben per Konsole). Jeder Testfall startet mit einem frischen Programm.

| ID | Beschreibung | Eingabe | Erwartetes Resultat | Effektives Resultat | Status |
|---:|---|---|---|---|---|
| 1 | Alle Konten anzeigen | `a` | Liste der 5 Konten mit Nr., Name, Währung | wie erwartet | ✅ OK |
| 2 | Konto wählen & Kontostand abfragen | `3`, `k` | Details Konto 3, Kontostand 23'500.00 CHF | wie erwartet | ✅ OK |
| 3 | Einzahlen | `1`, `e`, `100` | Kontostand 1'600.00 USD | 1600.00 USD | ✅ OK |
| 4 | Einzahlen mit ungültigem Betrag | `1`, `e`, `abc`, `50` | Fehlermeldung, erneute Eingabe, danach 1'550.00 USD | wie erwartet | ✅ OK |
| 5 | Abheben mehr als Saldo | `4`, `a`, `200`, `100` | "Kontostand zu niedrig", erneute Eingabe, danach 0.50 EUR | wie erwartet | ✅ OK |
| 6 | **Negativen Betrag einzahlen** | `4`, `e`, `-1000` | Fehlermeldung "Betrag muss positiv sein" | Kontostand **-899.50 EUR** | ❌ Fehler |
| 7 | **Negativen Betrag abheben** | `4`, `a`, `-5000` | Fehlermeldung | Kontostand **5'100.50 EUR** (Geld "erzeugt") | ❌ Fehler |
| 8 | Nicht existierendes Konto wählen | `9` | "Ein Konto mit dieser Nummer ist nicht vorhanden!" | wie erwartet | ✅ OK |
| 9 | Ungültige Menüeingabe | `xyz` | Fehlermeldung mit den gültigen Optionen | Meldung nennt `"u"` statt `"w"` als Option | ⚠️ Mangel |
| 10 | Konto erstellen | `e`, `Meier`, `CHF` | Neues Konto Nr. 6, Meier, 0.00 CHF | wie erwartet | ✅ OK |
| 11 | Konto mit unbekannter Währung erstellen | `e`, `Meier`, `GBP` | Fehlermeldung, erneute Eingabe | Hinweis und Konto wird ungefragt in **USD** erstellt | ⚠️ Mangel |
| 12 | **Leere Eingabe im Kontomenü** | `1`, *(Enter)* | Fehlermeldung, erneute Eingabe | **Absturz:** `StringIndexOutOfBoundsException` | ❌ Fehler |
| 13 | Überweisung USD → CHF | `1`, `ü`, `3`, `100` | Konto 1: 1'400 USD; Konto 3: 23'500 + umgerechneter Betrag | 1'400 USD / 23'611 CHF (fixer Kurs 1.11) | ✅ OK |
| 14 | **Überweisung EUR → CHF** | `2`, `ü`, `3`, `100` | 100 EUR werden in CHF umgerechnet | "Es wurde keine Umrechnung vorgenommen", es werden **100 CHF** gutgeschrieben | ❌ Fehler |
| 15 | Überweisung auf eigenes Konto | `1`, `ü`, `1` | Fehlermeldung | "Bitte ein anderes Konto … auswählen!" | ✅ OK |
| 16 | Konto löschen & erneut wählen | `2`, `l`, `j`, `2` | "Konto 2 wurde gelöscht", danach "nicht vorhanden" | wie erwartet | ✅ OK |
| 17 | **Negativen Betrag überweisen** | `4`, `ü`, `5`, `-1000` | Fehlermeldung | Konto 4: **+1'000**, Konto 5: **-1'000** (Geld vom Empfänger "gestohlen") | ❌ Fehler |
| 18 | Eingabe mit Zahl und Text | `12abc` | Fehlermeldung | **Keine Meldung**, Menü erscheint einfach erneut | ⚠️ Mangel |
| 19 | Eingabe "exit" im Hauptmenü | `exit` | Fehlermeldung (bzw. Beenden) | Startet **Konto erstellen**, weil nur der 1. Buchstabe ausgewertet wird | ⚠️ Mangel |
| 20 | Betrag "NaN" einzahlen | `1`, `e`, `NaN` | Fehlermeldung | Kontostand **NaN USD**, Konto unbrauchbar | ❌ Fehler |
| 21 | Konto ohne Namen erstellen | `e`, *(Enter)*, `CHF` | Fehlermeldung "Name erforderlich" | Konto ohne Namen wird erstellt (Code-Analyse) | ⚠️ Mangel |
| 22 | Wechselkurs abfragen | `w`, `CHF USD` | "1 CHF = x USD" (Live-Kurs) | nicht ausgeführt: externer Dienst mit fest eingebautem API-Key | ⏸️ offen |
| 23 | Programm beenden | `q` | "Auf Wiedersehen!", Programmende | wie erwartet | ✅ OK |

**Zusammenfassung:** 23 Testfälle, davon 11 OK, 6 Fehler, 5 Mängel, 1 offen.

### White-Box-Testfälle (Methoden)

| Klasse | Methode | Was wird getestet (Pfade / Zweige) |
|---|---|---|
| `Account` | `Account(...)` (Konstruktor) | IDs werden fortlaufend vergeben (`counter`); Startsaldo, Name und Währung korrekt gesetzt |
| `Account` | `deposit(double)` | Saldo erhöht sich; Grenzfälle 0, negativ, `NaN`, sehr grosse Werte |
| `Account` | `withdraw(double)` | Zweig `amount > balance` → `false`, Saldo unverändert; Zweig `else` → `true`, Saldo reduziert; Grenzwert `amount == balance` |
| `Bank` | `createAccount(...)` | Konto wird erstellt **und** in der Liste gespeichert (`getNumberOfAccounts()` +1) |
| `Bank` | `getAccount(int)` | Konto gefunden → richtiges Objekt; nicht gefunden → `null`; gelöschtes Konto → `null` |
| `Bank` | `deleteAccount(Account)` | Konto ist danach nicht mehr in der Liste; Anzahl -1 |
| `Bank` | `printAccountDetails(Account)` | Zweig "Konto existiert nicht mehr" vs. Ausgabe der Details |
| `Bank` | `printOtherAccounts(Account)` | Das übergebene Konto erscheint nicht in der Ausgabe |
| `Counter` | `convertCurrency(...)` (private) | Alle 3 umgerechneten Paare (USD→CHF, USD→EUR, CHF→USD) und der Default-Zweig (keine Umrechnung) – zeigt, dass EUR→CHF, EUR→USD, CHF→EUR fehlen |
| `Counter` | `transferAmount(Account, Account)` | Gleiche Währung / unterschiedliche Währung; zu wenig Guthaben (Schleife); ungültiger Betrag |
| `Counter` | `chooseAccount()` | Zweige `q`, `a`, `e`, `w`, Zahl gefunden / nicht gefunden, ungültige Eingabe |
| `Counter` | `editAccount(int)` | Alle `switch`-Zweige, Rückgabewert `true`/`false`, leere Eingabe |
| `Counter` | `createAccount()` | Gültige Währung, unbekannte Währung (→ USD), ungültiges Format |
| `ExchangeRateOkhttp` | `getExchangeRate(String, String)` | Erfolgreiche Antwort wird geparst; Netzwerkfehler → `0.0`. Für den Test muss der HTTP-Client gemockt werden (z.B. `MockWebServer`) |

Gut als Unit-Tests geeignet sind vor allem `Account` und `Bank`, weil sie keine Konsoleneingaben brauchen.
`Counter` liest direkt von `System.in` und ist deshalb schwer testbar (siehe Verbesserungen).

### Verbesserungen / Best Practices

**Fehler beheben**

* **Beträge validieren:** `deposit`, `withdraw` und `transferAmount` müssen `amount <= 0`, `NaN` und `Infinity` ablehnen
  (Testfälle 6, 7, 17, 20). Die Prüfung gehört in die Klasse `Account`, nicht nur ins UI.
* **Leere Eingaben abfangen:** `input.substring(0,1)` in `editAccount` und `getConfirmation` stürzt bei leerer Eingabe ab (Testfall 12).
* **Währungsumrechnung vervollständigen:** Es fehlen EUR→CHF, EUR→USD und CHF→EUR (Testfall 14). Besser eine Kurs-Tabelle
  (`Map<Currency, Double>` gegenüber einer Basiswährung) statt einzelner `if`-Abfragen. Die fixen Kurse (USD→CHF 1.11) sind zudem unrealistisch.
* **Menü-Eingabe exakt prüfen:** `matcher.find()` mit `\\d|a|e|w|q` findet das Muster *irgendwo* im String. Besser
  `matches()` mit `^(\\d+|[aewq])$` verwenden (Testfälle 18, 19). Fehlermeldung "u" → "w" korrigieren (Testfall 9).
* Unbekannte Währung nicht stillschweigend auf USD setzen, sondern erneut fragen; leeren Namen ablehnen (Testfälle 11, 21).
* Die Anforderung "Abheben innerhalb eines Limits" ist nicht umgesetzt. Es gibt kein Tageslimit/Bezugslimit.

**Code-Qualität / Best Practices**

* **Kein `double` für Geldbeträge:** Rundungsfehler (`0.1 + 0.2 != 0.3`). `BigDecimal` oder Rappen als `long` verwenden.
* **API-Key nicht im Quellcode:** Der Key in `ExchangeRateOkhttp` ist im Repository öffentlich sichtbar. Er gehört in eine
  Umgebungsvariable/Konfigurationsdatei und sollte erneuert werden.
* **Spezifische Exceptions fangen** statt `catch (Exception e)` + `instanceof`. Fehler nicht stillschweigend verschlucken
  (in `chooseAccount` wird die `NumberFormatException` ignoriert). Tippfehler `AccountExeption` → `AccountException`.
* **UI und Logik trennen:** `Counter` mischt Ein-/Ausgabe mit Geschäftslogik (Überweisen, Umrechnen). Logik in eine eigene
  Service-Klasse auslagern und den `Scanner` per Konstruktor übergeben (Dependency Injection) → testbar ohne Konsole.
* **Überweisung atomar machen:** Wenn nach `withdraw` etwas schiefgeht, ist das Geld beim Absender weg, aber nie beim Empfänger angekommen.
* `Account.counter` ist `static` und nicht `private`; `Currency`-Enum in eigene Datei verschieben; Rückgabewert `0` von
  `chooseAccount` als "Beenden" kollidiert mit einer möglichen Kontonummer 0.
* `ExchangeRateOkhttp`: HTTP-Statuscode prüfen und `Response` schliessen (try-with-resources); `OkHttpClient` wiederverwenden.
* `getAccount` mit Streams: `accounts.stream().filter(a -> a.getId() == nr).findFirst()` (beantwortet das TODO im Code).
* Der Kommentar in `Bank.deleteAccount` ist falsch: Java übergibt die *Referenz* per Wert, `a.pseudoDeleteAccount()` würde funktionieren.
* **Automatisierte Unit-Tests (JUnit 5)** ergänzen und im Build (`mvn test`) laufen lassen.
