package ch.tbz.m450.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Date date;

    @BeforeEach
    void setUp() {
        date = new Date(0);
    }

    @Test
    void createWithAllArgsConstructor() {
        Address address = new Address(1, "Max", "Muster", "0791234567", date);

        assertEquals(1, address.getId());
        assertEquals("Max", address.getFirstname());
        assertEquals("Muster", address.getLastname());
        assertEquals("0791234567", address.getPhonenumber());
        assertEquals(date, address.getRegistrationDate());
    }

    @Test
    void createWithSetters() {
        Address address = new Address();
        address.setId(2);
        address.setFirstname("Anna");
        address.setLastname("Meier");
        address.setPhonenumber("0441234567");
        address.setRegistrationDate(date);

        assertEquals(2, address.getId());
        assertEquals("Anna", address.getFirstname());
        assertEquals("Meier", address.getLastname());
        assertEquals("0441234567", address.getPhonenumber());
        assertEquals(date, address.getRegistrationDate());
    }

    @Test
    void emptyAddressHasDefaultValues() {
        Address address = new Address();

        assertEquals(0, address.getId());
        assertNull(address.getFirstname());
        assertNull(address.getLastname());
    }
}
