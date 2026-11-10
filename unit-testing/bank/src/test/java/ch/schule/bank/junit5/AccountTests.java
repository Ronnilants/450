package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.Booking;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die Klasse Account.
 * Account ist abstrakt, darum wird mit einem SavingsAccount getestet.
 * Beträge sind in Millirappen (100000 = 1 CHF), Datum in Banktagen seit 1.1.1970.
 */
public class AccountTests {

    private Account account;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        account = new SavingsAccount("S-1");
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Neues Konto hat die richtige ID und Saldo 0")
    public void testInit() {
        assertEquals("S-1", account.getId());
        assertEquals(0, account.getBalance());
    }

    @Test
    @DisplayName("Einzahlen: positiv und 0 erlaubt, negativ abgelehnt")
    public void testDeposit() {
        assertTrue(account.deposit(1, 1000));
        assertEquals(1000, account.getBalance());

        assertTrue(account.deposit(2, 0));
        assertEquals(1000, account.getBalance());

        assertFalse(account.deposit(3, -500));
        assertEquals(1000, account.getBalance());
    }

    @Test
    @DisplayName("Abheben: reduziert den Saldo, negativer Betrag wird abgelehnt")
    public void testWithdraw() {
        account.deposit(1, 1000);

        assertTrue(account.withdraw(2, 400));
        assertEquals(600, account.getBalance());

        assertFalse(account.withdraw(3, -100));
        assertEquals(600, account.getBalance());
    }

    @Test
    @DisplayName("Referenz auf eine Buchung setzen und lesen")
    public void testReferences() {
        assertNull(account.getBooking());

        Booking booking = new Booking(1, 500);
        account.setBooking(booking);

        assertSame(booking, account.getBooking());
    }

    @Test
    @DisplayName("Buchungen dürfen nicht vor der letzten Buchung liegen")
    public void testCanTransact() {
        assertTrue(account.canTransact(0));      // noch keine Buchung

        account.deposit(100, 1000);
        assertTrue(account.canTransact(100));    // gleicher Tag
        assertTrue(account.canTransact(101));    // später
        assertFalse(account.canTransact(99));    // früher

        assertFalse(account.deposit(50, 1000));  // Einzahlung in der Vergangenheit
        assertFalse(account.withdraw(50, 100));  // Abhebung in der Vergangenheit
        assertEquals(1000, account.getBalance());
    }

    @Test
    @DisplayName("Kontoauszug enthält alle Buchungen")
    public void testPrint() {
        account.deposit(0, 100000);   // 01.01.1970, 1.00 CHF
        account.withdraw(31, 50000);  // 02.02.1970, 0.50 CHF

        account.print();

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1'"));
        assertTrue(text.contains("01.01.1970"));
        assertTrue(text.contains("02.02.1970"));
    }

    @Test
    @DisplayName("Monatsauszug enthält nur die Buchungen des Monats")
    public void testMonthlyPrint() {
        account.deposit(0, 100000);   // 01.01.1970
        account.deposit(35, 100000);  // 06.02.1970
        account.deposit(65, 100000);  // 06.03.1970

        account.print(1970, 2);

        String text = output.toString();
        assertTrue(text.contains("Monat: 2.1970"));
        assertTrue(text.contains("06.02.1970"));
        assertFalse(text.contains("01.01.1970"));
        assertFalse(text.contains("06.03.1970"));
    }
}
