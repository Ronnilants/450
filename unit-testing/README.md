# M450 – Unit Testing

Quelle: [UEBUNGEN.md](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/unit-testing/UEBUNGEN.md)

| Aufgabe | Lösung |
|---|---|
| 1 – Simpler Rechner | [`calculator/`](calculator) |
| 2 – JUnit Zusammenfassung | [unten](#aufgabe-2--junit-zusammenfassung) |
| 3 – Banken Simulation verstehen | [unten](#aufgabe-3--banken-simulation) |
| 4 – Unit-Tests Banken Simulation | [`bank/src/test/java`](bank/src/test/java/ch/schule/bank/junit5) |

---

## Aufgabe 1 – Simpler Rechner

* Klasse: [`Calculator.java`](calculator/src/main/java/ch/tbz/m450/calculator/Calculator.java) im **main**-Package
  mit `add`, `subtract`, `multiply`, `divide`. Division durch 0 wirft eine `ArithmeticException`.
* Tests: [`CalculatorTest.java`](calculator/src/test/java/ch/tbz/m450/calculator/CalculatorTest.java) im **test**-Package
  mit JUnit 5 (`@Test`, `@BeforeEach`, `@DisplayName`, `@ParameterizedTest`, `assertThrows`).
* Getestete Fälle: positive und negative Zahlen, 0, Kommazahlen (`0.1 + 0.2`), Division durch 0.

### 1) In der Entwicklungsumgebung (IntelliJ)

`unit-testing/calculator/pom.xml` öffnen → Rechtsklick auf `CalculatorTest` → **Run 'CalculatorTest'**.

### 2) Mit Maven auf der Kommandozeile

```bash
cd unit-testing/calculator
mvn test            # oder ohne installiertes Maven: ./mvnw test  (Windows: mvnw.cmd test)
```

Resultat:

```
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Aufgabe 2 – JUnit Zusammenfassung

**JUnit 5** ist das Standard-Framework für Unit-Tests in Java. Es besteht aus der *Jupiter*-API (Annotationen, Assertions)
und der *Platform*, welche die Tests ausführt (IDE, Maven, Gradle).

### Annotationen

| Annotation | Bedeutung | Anwendungsfall |
|---|---|---|
| `@Test` | Markiert eine Testmethode | Jede Methode, die etwas prüft |
| `@BeforeEach` / `@AfterEach` | Läuft vor / nach **jedem** Test | Neues Objekt erstellen, `System.out` zurücksetzen |
| `@BeforeAll` / `@AfterAll` | Läuft einmal vor / nach **allen** Tests (`static`) | Teure Ressourcen, z.B. DB-Verbindung |
| `@DisplayName("…")` | Lesbarer Name im Testreport | `"Division durch 0 wirft Exception"` |
| `@Disabled("Grund")` | Test wird übersprungen | Test für noch nicht fertige Funktion |
| `@ParameterizedTest` | Gleicher Test mit mehreren Eingaben | Zusammen mit `@ValueSource`, `@CsvSource`, `@MethodSource` |
| `@RepeatedTest(n)` | Test n-mal ausführen | Zufallswerte, Stabilität |
| `@Nested` | Innere Testklasse zum Gruppieren | Alle Tests zu `withdraw` zusammenfassen |
| `@Tag("slow")` | Tests markieren und filtern | Nur schnelle Tests im Build ausführen |
| `@Timeout(1)` | Test schlägt fehl, wenn er zu lange dauert | Performance-Grenze |

### Assertions (`org.junit.jupiter.api.Assertions`)

| Methode | Prüft | Beispiel |
|---|---|---|
| `assertEquals(expected, actual)` | Gleichheit | `assertEquals(5, calc.add(2, 3))` |
| `assertEquals(exp, act, delta)` | Gleichheit mit Toleranz (`double`) | `assertEquals(0.3, calc.add(0.1, 0.2), 0.0001)` |
| `assertTrue` / `assertFalse` | Bedingung | `assertFalse(account.withdraw(1, -5))` |
| `assertNull` / `assertNotNull` | `null` | `assertNull(bank.createSalaryAccount(1000))` |
| `assertSame` | Gleiches Objekt (Referenz) | `assertSame(booking, account.getBooking())` |
| `assertThrows(Ex.class, () -> …)` | Exception wird geworfen | `assertThrows(ArithmeticException.class, () -> calc.divide(1, 0))` |
| `assertAll(…)` | Mehrere Assertions, alle werden ausgewertet | Mehrere Felder eines Objekts prüfen |
| `assertTimeout(…)` | Code ist schnell genug | `assertTimeout(Duration.ofMillis(100), () -> …)` |
| `fail("…")` | Test absichtlich fehlschlagen lassen | Platzhalter "toDo" |

### Beispiel

```java
class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() { calculator = new Calculator(); }

    @Test
    @DisplayName("Division durch 0 wirft eine ArithmeticException")
    void divideByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(5, 0));
    }

    @ParameterizedTest
    @CsvSource({"1, 1, 2", "-1, 1, 0", "2.5, 2.5, 5"})
    void add(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), 0.0001);
    }
}
```

### Gute Praxis

* **AAA-Muster:** *Arrange* (vorbereiten) → *Act* (ausführen) → *Assert* (prüfen).
* Ein Test prüft **ein** Verhalten, Tests sind unabhängig voneinander (Reihenfolge egal).
* Grenzwerte und Fehlerfälle testen, nicht nur den "Happy Path".
* Testklassen heissen wie die Klasse + `Test` und liegen im gleichen Package unter `src/test/java`.

**Referenz:** [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/) · gut verständliches Tutorial: [Baeldung – A Guide to JUnit 5](https://www.baeldung.com/junit-5)

---

## Aufgabe 3 – Banken Simulation

Projekt: [`bank/`](bank) (Maven, Klassendiagramm in [`bank/Design`](bank/Design/bank6_klassendiagramm.png))

### Wie funktioniert die Software?

* **`Bank`** verwaltet alle Konten in einer `TreeMap<String, Account>` (Schlüssel = Kontonummer).
  * Erstellt Konten mit fortlaufender Nummer ab 1000 und Präfix: `S-` Sparkonto, `Y-` Jugendsparkonto, `P-` Lohnkonto.
  * `deposit` / `withdraw` / `getBalance(id)` suchen das Konto und leiten den Aufruf weiter. Unbekanntes Konto → `false` bzw. `0`.
  * `getBalance()` = Saldo der Bank = **minus** Summe aller Kundenguthaben (Guthaben der Kunden sind Schulden der Bank).
  * `printTop5` / `printBottom5` sortieren mit den Comparatoren und geben die 5 höchsten / tiefsten Konten aus.
* **`Account`** (abstrakt) hat Kontonummer, Saldo und eine Liste von `Booking`s.
  * `deposit` / `withdraw`: negativer Betrag → `false`. Jede Transaktion erzeugt eine Buchung (Abhebung mit negativem Betrag).
  * `canTransact(date)`: Eine Buchung darf nicht vor der letzten Buchung liegen (keine Rückdatierung).
  * `print()` / `print(year, month)`: Kontoauszug, gesamt oder für einen Monat.
* **Unterklassen von `Account`** (Vererbung):
  * **`SavingsAccount`** (Sparkonto): darf nicht unter 0 fallen.
  * **`PromoYouthSavingsAccount`** (Jugendsparkonto) erbt von `SavingsAccount`: **+1 % Bonus** auf jede Einzahlung.
  * **`SalaryAccount`** (Lohnkonto): darf bis zur **Kreditlimite** (negative Zahl) ins Minus.
* **`Booking`**: eine Buchung mit Datum und Betrag, kann sich als Zeile ausdrucken.
* **`BankUtils`**: Hilfsfunktionen zum Formatieren von Datum und Betrag.
* **`AccountBalanceComparator`** (absteigend) / **`AccountInverseBalanceComparator`** (aufsteigend): sortieren Konten nach Saldo.

### Wichtige Details

* **Beträge** sind `long` in **Millirappen** (100'000 = 1 CHF). Dadurch gibt es keine Rundungsfehler wie bei `double`.
* **Datum** ist ein `int` in **Banktagen** seit 1.1.1970, vereinfacht mit 30 Tagen pro Monat und 360 Tagen pro Jahr.
* Polymorphie: `Bank` kennt nur `Account`. Welche `withdraw`-Regel gilt, entscheidet die Unterklasse.
* Auffälligkeiten: `Main` erstellt ein Lohnkonto mit Limite `+12000`, das ist ungültig und gibt `null` zurück.
  Die Felder `Account.booking` und `Bank.account` (mit Getter/Setter) stammen aus dem UML-Tool und werden nicht verwendet.

---

## Aufgabe 4 – Unit-Tests implementieren

Tests in [`bank/src/test/java/ch/schule/bank/junit5`](bank/src/test/java/ch/schule/bank/junit5):

| Testklasse | Tests | Was wird getestet |
|---|---:|---|
| `AccountTests` | 7 | Initialisierung, Ein-/Auszahlen, negative Beträge, `canTransact` (Rückdatierung), Kontoauszug, Monatsauszug |
| `BankTests` | 9 | Konten erstellen (IDs, ungültige Limite), Ein-/Auszahlen, unbekanntes Konto, Bank-Saldo, Top 5 / Bottom 5, Auszüge |
| `BookingTests` | 4 | Buchung erstellen, Buchungszeile, Datums- und Betragsformatierung (`BankUtils`) |
| `SavingsAccountTests` | 2 | Nicht unter 0 abheben, Comparatoren (grösser / kleiner / gleich) |
| `SalaryAccountTests` | 3 | Abheben bis genau zur Kreditlimite, darüber nicht, negativer Betrag |
| `PromoYouthSavingsAccountTests` | 3 | 1 % Bonus (inkl. Abrundung), negative Einzahlung, Abheben |

Ausgaben auf der Konsole (`print`) werden geprüft, indem `System.out` im Test auf einen `ByteArrayOutputStream` umgeleitet wird.

### Ausführen

```bash
cd unit-testing/bank
mvn test            # oder ./mvnw test
```

```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

In IntelliJ: Rechtsklick auf den Ordner `src/test/java` → **Run 'All Tests' with Coverage**.

### Code Coverage

Gemessen mit JaCoCo (im `pom.xml` eingebunden, Report nach `mvn test` unter `target/site/jacoco/index.html`):

| Klasse | Klassen | Zeilen | Zweige |
|---|---:|---:|---:|
| Account | 100 % | 49/49 | 20/20 |
| AccountBalanceComparator | 100 % | 6/6 | 4/4 |
| AccountInverseBalanceComparator | 100 % | 6/6 | 4/4 |
| Bank | 100 % | 63/63 | 20/22 |
| BankUtils | 100 % | 14/15 | 2/2 |
| Booking | 100 % | 10/10 | – |
| PromoYouthSavingsAccount | 100 % | 4/4 | – |
| SalaryAccount | 100 % | 7/7 | 2/2 |
| SavingsAccount | 100 % | 5/5 | 2/2 |
| Main | 0 % | 0/5 | – |
| **Total** | **90 % (9/10)** | | |

Gleich wie in der Vorgabe: alle Klassen ausser `Main` sind abgedeckt. `Main` ist nur ein Startprogramm ohne Logik.
Die fehlende Zeile in `BankUtils` ist der automatisch erzeugte Konstruktor (Klasse hat nur `static`-Methoden).
