package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testet den Service ohne H2-Datenbank: das Repository wird mit Mockito gemockt.
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository repository;   // Mock statt echter Datenbank

    @InjectMocks
    private AddressService service;          // bekommt den Mock im Konstruktor

    private Address max;
    private Address anna;

    @BeforeEach
    void setUp() {
        max = new Address(1, "Max", "Zürcher", "079", null);
        anna = new Address(2, "Anna", "Arnold", "044", null);
    }

    @Test
    void saveCallsRepository() {
        when(repository.save(max)).thenReturn(max);         // Stub

        Address result = service.save(max);

        assertSame(max, result);
        verify(repository).save(max);                       // Mock: wurde save aufgerufen?
    }

    @Test
    void getAllReturnsSortedList() {
        when(repository.findAll()).thenReturn(List.of(max, anna));

        List<Address> result = service.getAll();

        assertEquals(List.of(anna, max), result);           // Arnold vor Zürcher
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllWithEmptyDatabase() {
        when(repository.findAll()).thenReturn(List.of());

        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void getAddressFound() {
        when(repository.findById(1)).thenReturn(Optional.of(max));

        Optional<Address> result = service.getAddress(1);

        assertTrue(result.isPresent());
        assertEquals("Max", result.get().getFirstname());
    }

    @Test
    void getAddressNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertTrue(service.getAddress(99).isEmpty());
        verify(repository).findById(99);
        verifyNoMoreInteractions(repository);
    }
}
