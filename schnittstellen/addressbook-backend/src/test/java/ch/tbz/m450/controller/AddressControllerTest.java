package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testet den Controller, der Service wird gemockt.
 */
@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressService service;

    @InjectMocks
    private AddressController controller;

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(1, "Max", "Muster", "079", null);
    }

    @Test
    void createAddressReturns201() {
        when(service.save(address)).thenReturn(address);

        ResponseEntity<Address> response = controller.createAddress(address);

        assertEquals(201, response.getStatusCode().value());
        assertSame(address, response.getBody());
        verify(service).save(address);
    }

    @Test
    void getAddressesReturns200() {
        when(service.getAll()).thenReturn(List.of(address));

        ResponseEntity<List<Address>> response = controller.getAddresses();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(address), response.getBody());
    }

    @Test
    void getAddressFoundReturns200() {
        when(service.getAddress(1)).thenReturn(Optional.of(address));

        ResponseEntity<Address> response = controller.getAddress(1);

        assertEquals(200, response.getStatusCode().value());
        assertSame(address, response.getBody());
    }

    @Test
    void getAddressNotFoundReturns404() {
        when(service.getAddress(99)).thenReturn(Optional.empty());

        ResponseEntity<Address> response = controller.getAddress(99);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
