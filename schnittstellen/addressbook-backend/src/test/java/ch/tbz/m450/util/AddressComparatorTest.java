package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressComparatorTest {

    private AddressComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new AddressComparator();
    }

    private Address address(int id, String firstname, String lastname, String phone, Date date) {
        return new Address(id, firstname, lastname, phone, date);
    }

    // ---------- Aufgabe 1: Nachname, Vorname ----------

    @Test
    void sortsByLastname() {
        Address meier = address(1, "Max", "Meier", "1", null);
        Address muster = address(2, "Max", "Muster", "1", null);

        assertTrue(comparator.compare(meier, muster) < 0);
        assertTrue(comparator.compare(muster, meier) > 0);
    }

    @Test
    void sameLastnameSortsByFirstname() {
        Address anna = address(1, "Anna", "Meier", "1", null);
        Address max = address(2, "Max", "Meier", "1", null);

        assertTrue(comparator.compare(anna, max) < 0);
    }

    @Test
    void ignoresUpperAndLowerCase() {
        Address a = address(1, "max", "meier", "1", null);
        Address b = address(1, "Max", "Meier", "1", null);

        assertEquals(0, comparator.compare(a, b));
    }

    @Test
    void equalAddressesReturnZero() {
        Address a = address(1, "Max", "Meier", "1", new Date(0));
        Address b = address(1, "Max", "Meier", "1", new Date(0));

        assertEquals(0, comparator.compare(a, b));
    }

    @Test
    void nullLastnameComesLast() {
        Address withName = address(1, "Max", "Meier", "1", null);
        Address withoutName = address(2, "Max", null, "1", null);

        assertTrue(comparator.compare(withName, withoutName) < 0);
    }

    @Test
    void sortsList() {
        List<Address> list = new ArrayList<>(List.of(
                address(1, "Max", "Zürcher", "1", null),
                address(2, "Max", "Arnold", "1", null),
                address(3, "Anna", "Arnold", "1", null)));

        list.sort(comparator);

        assertEquals(3, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
        assertEquals(1, list.get(2).getId());
    }

    // ---------- Aufgabe 2: zusätzliche Attribute ----------

    @Test
    void sameNameSortsByPhonenumber() {
        Address a = address(1, "Max", "Meier", "0441111111", null);
        Address b = address(2, "Max", "Meier", "0792222222", null);

        assertTrue(comparator.compare(a, b) < 0);
    }

    @Test
    void samePhoneSortsByRegistrationDate() {
        Address older = address(1, "Max", "Meier", "1", new Date(1000));
        Address newer = address(2, "Max", "Meier", "1", new Date(2000));
        Address noDate = address(3, "Max", "Meier", "1", null);

        assertTrue(comparator.compare(older, newer) < 0);
        assertTrue(comparator.compare(newer, noDate) < 0);
    }

    @Test
    void everythingSameSortsById() {
        Address a = address(1, "Max", "Meier", "1", new Date(0));
        Address b = address(2, "Max", "Meier", "1", new Date(0));

        assertTrue(comparator.compare(a, b) < 0);
    }
}
