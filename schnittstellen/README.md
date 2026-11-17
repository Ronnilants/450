# M450 – Schnittstellen (Test Doubles / Mockito)

Quelle: [UEBUNGEN.md](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/schnittstellen/UEBUNGEN.md)

Projekt: [`addressbook-backend/`](addressbook-backend) (Spring Boot, Java 21)

## Ausführen

```bash
cd schnittstellen/addressbook-backend
./mvnw test        # Windows: mvnw.cmd test
```

Resultat: **22 Tests, 0 Fehler**. In IntelliJ: Rechtsklick auf `src/test/java` → **Run 'All Tests'**.

---

## Aufgabe 1 – Tests für alle Klassen

| Klasse | Testklasse | Was wird getestet |
|---|---|---|
| `Address` | [`AddressTest`](addressbook-backend/src/test/java/ch/tbz/m450/repository/AddressTest.java) | Adressen erstellen (Konstruktor und Setter), Standardwerte |
| `AddressComparator` | [`AddressComparatorTest`](addressbook-backend/src/test/java/ch/tbz/m450/util/AddressComparatorTest.java) | Sortierung nach Nachname/Vorname, Gross-/Kleinschreibung, `null`, Liste sortieren |
| `AddressService` | [`AddressServiceTest`](addressbook-backend/src/test/java/ch/tbz/m450/service/AddressServiceTest.java) | `save`, `getAll` (sortiert), `getAddress` gefunden/nicht gefunden. **H2 ist weggemockt** |
| `AddressController` | [`AddressControllerTest`](addressbook-backend/src/test/java/ch/tbz/m450/controller/AddressControllerTest.java) | Statuscodes 201 / 200 / 404, Service gemockt |
| `AddressbookApplication` | [`AddressbookApplicationTest`](addressbook-backend/src/test/java/ch/tbz/m450/AddressbookApplicationTest.java) | Applikation startet |

`AddressRepository` ist ein Interface (Spring Data) und wird in den Tests gemockt.

**Verwendete Annotationen:** `@Test`, `@BeforeEach`, `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `@SpringBootTest`.

### Datenbank wegmocken (AddressServiceTest)

```java
@Mock
private AddressRepository repository;   // Mock statt H2-Datenbank

@InjectMocks
private AddressService service;          // Service bekommt den Mock

@Test
void saveCallsRepository() {
    when(repository.save(max)).thenReturn(max);   // Stub: Rückgabewert festlegen
    service.save(max);
    verify(repository).save(max);                 // Mock: Aufruf prüfen
}
```

* `when(...).thenReturn(...)` → **Stub** (liefert vordefinierte Daten, State Testing)
* `verify(...)` → **Mock** (prüft, ob die richtige Methode aufgerufen wurde, Behavioral Testing)

### Comparator korrekt implementiert

Vorher gab `compare` immer `-1` zurück (falsch). Jetzt wird sortiert nach **Nachname → Vorname**, ohne Rücksicht auf
Gross-/Kleinschreibung. `null` kommt ans Ende.

---

## Aufgabe 2 – Comparator erweitern

[`AddressComparator`](addressbook-backend/src/main/java/ch/tbz/m450/util/AddressComparator.java) vergleicht bei gleichem
Namen weitere Attribute:

1. Nachname
2. Vorname
3. **Telefonnummer** (neu)
4. **Registrierungsdatum** (neu, älteres zuerst)
5. **ID** (neu)

Getestet in `AddressComparatorTest` (Abschnitt "Aufgabe 2"): `sameNameSortsByPhonenumber`,
`samePhoneSortsByRegistrationDate`, `everythingSameSortsById`.
