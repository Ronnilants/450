package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;

/**
 * Sortiert Adressen nach:
 * 1. Nachname, 2. Vorname (Aufgabe 1),
 * 3. Telefonnummer, 4. Registrierungsdatum, 5. ID (Aufgabe 2).
 * Gross-/Kleinschreibung wird ignoriert, null-Werte kommen ans Ende.
 */
public class AddressComparator implements Comparator<Address> {

    private static final Comparator<String> TEXT =
            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);

    private static final Comparator<Address> ORDER =
            Comparator.comparing(Address::getLastname, TEXT)
                    .thenComparing(Address::getFirstname, TEXT)
                    .thenComparing(Address::getPhonenumber, TEXT)
                    .thenComparing(Address::getRegistrationDate,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Address::getId);

    @Override
    public int compare(Address a1, Address a2) {
        return ORDER.compare(a1, a2);
    }
}
