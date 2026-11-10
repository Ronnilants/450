package ch.schule.bank.junit5;

import ch.schule.BankUtils;
import ch.schule.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests für die Klassen Booking und BankUtils.
 */
public class BookingTests {

    @Test
    @DisplayName("Buchung speichert Datum und Betrag")
    public void testInitialization() {
        Booking booking = new Booking(100, 5000);

        assertEquals(100, booking.getDate());
        assertEquals(5000, booking.getAmount());

        Booking withdrawal = new Booking(100, -5000);
        assertEquals(-5000, withdrawal.getAmount());
    }

    @Test
    @DisplayName("Buchungszeile enthält Datum, Betrag und neuen Saldo")
    public void testPrint() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        new Booking(0, 100000).print(200000);  // 1.00 CHF auf Saldo 2.00 CHF

        System.setOut(originalOut);

        String expected = "01.01.1970 "
                + BankUtils.formatAmount(100000) + " "
                + BankUtils.formatAmount(300000);
        assertEquals(expected, output.toString().trim());
    }

    @Test
    @DisplayName("Banktage als Datum formatieren (1 Jahr = 360 Tage, 1 Monat = 30 Tage)")
    public void testFormatBankDate() {
        assertEquals("01.01.1970", BankUtils.formatBankDate(0));
        assertEquals("30.01.1970", BankUtils.formatBankDate(29));
        assertEquals("01.02.1970", BankUtils.formatBankDate(30));
        assertEquals("30.12.1970", BankUtils.formatBankDate(359));
        assertEquals("01.01.1971", BankUtils.formatBankDate(360));
    }

    @Test
    @DisplayName("Betrag von Millirappen in Franken umrechnen, auf 10 Zeichen auffüllen")
    public void testFormatAmount() {
        String formatted = BankUtils.formatAmount(150000);  // 1.50 CHF

        assertEquals(10, formatted.length());
        // Dezimaltrennzeichen hängt von der Spracheinstellung ab (. oder ,)
        assertTrue(formatted.trim().matches("1[.,]50"));
    }
}
