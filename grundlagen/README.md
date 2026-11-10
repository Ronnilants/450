# M450 – Grundlagen: Lösungen der Übungen

Quelle der Aufgaben: [UEBUNGEN.md](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/grundlagen/UEBUNGEN.md)

---

## Aufgabe 1 – Formen von Tests

| Testform | Was wird geprüft? | Wie wird er durchgeführt? |
|---|---|---|
| **Komponenten-/Unit-Test** | Eine einzelne Methode oder Klasse erfüllt ihre Spezifikation. | Der Entwickler schreibt Testcode (z.B. JUnit, Jest, pytest), der die Methode mit definierten Eingaben aufruft und das Resultat mit dem erwarteten Wert vergleicht. Läuft automatisiert, z.B. bei jedem Build in der CI-Pipeline. |
| **Integrationstest** | Mehrere Komponenten arbeiten korrekt zusammen (z.B. Backend ↔ Datenbank, Service ↔ REST-API). | Teile des Systems werden zusammen gestartet (oft mit Testdatenbank oder Docker-Containern). Tests rufen z.B. einen REST-Endpoint auf und prüfen, ob die Daten korrekt in der DB landen. Werkzeuge: Postman, Spring Boot Test, Testcontainers. |
| **Systemtest / End-to-End-Test** | Das ganze System erfüllt die Anforderungen aus Sicht des Benutzers. | In einer produktionsnahen Testumgebung werden ganze Abläufe durchgespielt (Login → Bestellung → Rechnung). Manuell nach Testprotokoll oder automatisiert mit Selenium, Playwright oder Cypress. |
| **Abnahmetest (UAT)** | Der Kunde akzeptiert das System als korrekt. | Der Kunde/Fachbereich testet anhand der vereinbarten Abnahmekriterien bzw. User Stories und unterschreibt ein Abnahmeprotokoll. |
| **Regressionstest** | Bereits funktionierende Funktionen sind nach einer Änderung nicht kaputt gegangen. | Bestehende (meist automatisierte) Testsuite wird nach jeder Änderung/jedem Inkrement erneut ausgeführt. |
| **Last-/Performancetest** | Das System hält die geforderte Last aus (Antwortzeiten, viele gleichzeitige Benutzer). | Mit Tools wie JMeter, k6 oder Gatling werden viele parallele Anfragen simuliert und Antwortzeiten gemessen. |
| **Usability-Test** | Ist die Anwendung verständlich und angenehm bedienbar? | Testpersonen lösen Aufgaben, während sie beobachtet werden ("Thinking aloud"); Probleme werden protokolliert. |
| **Sicherheitstest / Penetrationstest** | Ist das System gegen Angriffe geschützt? | Scanner (z.B. OWASP ZAP) und manuelle Angriffsversuche (SQL-Injection, XSS, ...) in einer Testumgebung. |

Zusätzlich unterscheidet man nach **Methode**:
* **Black-Box-Test**: Getestet wird nur gegen die Spezifikation, ohne den Code zu kennen.
* **White-Box-Test**: Testfälle werden anhand des Codes erstellt (z.B. jede Verzweigung abdecken → Codeabdeckung).
* **Manuell vs. automatisiert**.

---

## Aufgabe 2 – SW-Fehler, SW-Mangel, hoher Schaden

**Beispiel SW-Fehler** (IST ≠ SOLL, Anforderung nicht erfüllt):
Ein Webshop soll ab einem Bestellwert von CHF 100 versandkostenfrei liefern. Die Software verrechnet aber bei genau
CHF 100.00 trotzdem Versandkosten, weil im Code `> 100` statt `>= 100` steht.
(Auch im Code aus Aufgabe 3: Ab 5 Zusatzausstattungen sollten 15 % Rabatt gewährt werden, es werden aber nur 10 % gewährt.)

**Beispiel SW-Mangel** (Anforderung/berechtigte Erwartung nicht *angemessen* erfüllt):
Die Rechnung wird korrekt berechnet, aber im GUI wird der Betrag als `1234.5` statt `CHF 1'234.50` angezeigt,
oder die Seite braucht 20 Sekunden zum Laden. Das Resultat ist richtig, aber die Erwartung des Benutzers wird nicht angemessen erfüllt.

**Beispiel für hohen Schaden durch einen SW-Fehler:**
* **Ariane 5 (1996):** Bei der Umwandlung einer 64-Bit-Gleitkommazahl in eine 16-Bit-Ganzzahl kam es zu einem
  Überlauf. Die Software stammte von der Ariane 4 und wurde nicht mit den neuen Flugdaten getestet. Die Rakete
  zerstörte sich 37 Sekunden nach dem Start selbst – Schaden ca. 370 Mio. USD.
* Weitere Beispiele: **Therac-25** (Strahlentherapiegerät, Race Condition → tödliche Überdosen bei Patienten),
  **Mars Climate Orbiter** (1999, Verwechslung metrischer/imperialer Einheiten → Sonde verloren),
  **CrowdStrike-Update** (2024, fehlerhaftes Update legte weltweit ca. 8.5 Mio. Windows-Rechner lahm, u.a. Flughäfen und Spitäler).

---

## Aufgabe 3 – Testtreiber für die Preisberechnung

Code: [`src/ch/tbz/m450/grundlagen`](src/ch/tbz/m450/grundlagen)

* `PriceCalculator.java` – enthält den Originalcode (`calculatePriceOriginal`) und die korrigierte Version (`calculatePrice`).
* `PriceCalculatorTestDriver.java` – der Testtreiber (`test_calculate_price`). Er ruft die Methode mit definierten
  Eingaben auf, vergleicht IST mit SOLL und gibt das Ergebnis aus. **Kein** Unit-Test-Framework.

### Ausführen

```bash
cd grundlagen
javac -d out src/ch/tbz/m450/grundlagen/*.java
java -cp out ch.tbz.m450.grundlagen.PriceCalculatorTestDriver
```

### Testfälle

Die Testfälle sind so gewählt, dass sie die **Grenzwerte** (2/3 und 4/5 Extras) und die Regel
"Händlerrabatt gilt nur für den Grundpreis" abdecken.

| Nr | baseprice | specialprice | extraprice | extras | discount | SOLL | IST Original |
|---:|---:|---:|---:|---:|---:|---:|---:|
| 1 | 20000 | 0 | 0 | 0 | 0 | 20000 | 20000 ✅ |
| 2 | 20000 | 0 | 0 | 0 | 10 | 18000 | 18000 ✅ |
| 3 | 20000 | 1000 | 2000 | 2 | 0 | 23000 | 23000 ✅ |
| 4 | 20000 | 1000 | 2000 | 3 | 0 | 22800 | 22800 ✅ |
| 5 | 20000 | 1000 | 2000 | 4 | 0 | 22800 | 22800 ✅ |
| 6 | 20000 | 1000 | 2000 | 5 | 0 | 22700 | 22800 ❌ |
| 7 | 20000 | 1000 | 2000 | 6 | 0 | 22700 | 22800 ❌ |
| 8 | 20000 | 0 | 2000 | 0 | 20 | 18000 | 17600 ❌ |
| 9 | 20000 | 0 | 2000 | 3 | 5 | 20800 | 20800 ✅ |
| 10 | 20000 | 500 | 3000 | 6 | 10 | 21050 | 21200 ❌ |
| 11 | 0 | 0 | 0 | 0 | 0 | 0 | 0 ✅ |

Mit der korrigierten Version sind alle 11 Tests OK.

---

## Aufgabe 3 – Bonus: Fehler im Code

**Fehler 1 – falsche Reihenfolge der Bedingungen:**

```java
if (extras >= 3)
    addon_discount = 10;
else if (extras >= 5)   // wird nie erreicht!
    addon_discount = 15;
```

Jede Zahl `>= 5` ist auch `>= 3`, deshalb greift immer schon die erste Bedingung. Der 15 %-Rabatt wird nie gewährt
(Testfälle 6, 7, 10). **Korrektur:** zuerst `extras >= 5` prüfen, dann `extras >= 3`.

**Fehler 2 – Händlerrabatt wird auf das Zubehör angewendet:**

```java
if (discount > addon_discount)
    addon_discount = discount;
```

Laut Spezifikation bezieht sich der Händlerrabatt *nur* auf den Grundpreis und der Zubehörrabatt *nur* auf die
Zubehörteile. Ist der Händlerrabatt grösser als der Zubehörrabatt, wird er hier fälschlicherweise auch auf die
Extras angewendet (Testfälle 8, 10). **Korrektur:** diese zwei Zeilen ersatzlos streichen.

**Korrigierter Code:**

```java
double calculatePrice(double baseprice, double specialprice, double extraprice, int extras, double discount) {
    double addonDiscount;

    if (extras >= 5)
        addonDiscount = 15;
    else if (extras >= 3)
        addonDiscount = 10;
    else
        addonDiscount = 0;

    return baseprice / 100.0 * (100 - discount)
            + specialprice
            + extraprice / 100.0 * (100 - addonDiscount);
}
```

Hinweis zur **Fehlermaskierung**: In Testfall 9 (5 % Händlerrabatt, 3 Extras) ist Fehler 2 nicht sichtbar, weil
der Zubehörrabatt (10 %) grösser ist. Erst mit einem Händlerrabatt > Zubehörrabatt (Testfall 8) tritt er auf.
Darum sind gezielt gewählte Testfälle (Grenzwerte, Kombinationen) wichtig.
