package ch.schule.bank.junit5;

import ch.schule.Bank;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die Klasse Bank.
 */
public class BankTests {

    private Bank bank;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Konten werden mit fortlaufender Nummer und Präfix erstellt")
    public void testCreate() {
        assertEquals("S-1000", bank.createSavingsAccount());
        assertEquals("Y-1001", bank.createPromoYouthSavingsAccount());
        assertEquals("P-1002", bank.createSalaryAccount(-5000));
        assertEquals("P-1003", bank.createSalaryAccount(0));

        // positive Kreditlimite ist ungültig
        assertNull(bank.createSalaryAccount(1000));
    }

    @Test
    @DisplayName("Einzahlen über die Bank")
    public void testDeposit() {
        String id = bank.createSavingsAccount();

        assertTrue(bank.deposit(id, 1, 1000));
        assertEquals(1000, bank.getBalance(id));

        assertFalse(bank.deposit("X-9999", 1, 1000)); // Konto existiert nicht
    }

    @Test
    @DisplayName("Abheben über die Bank")
    public void testWithdraw() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 1000);

        assertTrue(bank.withdraw(id, 2, 300));
        assertEquals(700, bank.getBalance(id));

        assertFalse(bank.withdraw(id, 3, 5000));       // zu wenig Geld
        assertFalse(bank.withdraw("X-9999", 3, 100)); // Konto existiert nicht
    }

    @Test
    @DisplayName("Kontoauszug über die Bank")
    public void testPrint() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 0, 100000);

        bank.print(id);
        assertTrue(output.toString().contains("Kontoauszug '" + id + "'"));

        output.reset();
        bank.print("X-9999");                 // unbekanntes Konto
        assertEquals("", output.toString());
    }

    @Test
    @DisplayName("Monatsauszug über die Bank")
    public void testMonthlyPrint() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 0, 100000);

        bank.print(id, 1970, 1);
        assertTrue(output.toString().contains("Monat: 1.1970"));

        output.reset();
        bank.print("X-9999", 1970, 1);        // unbekanntes Konto
        assertEquals("", output.toString());
    }

    @Test
    @DisplayName("Saldo der Bank = minus Summe aller Kundenguthaben")
    public void testBalance() {
        assertEquals(0, bank.getBalance());
        assertEquals(0, bank.getBalance("X-9999"));

        String a = bank.createSavingsAccount();
        String b = bank.createSavingsAccount();
        bank.deposit(a, 1, 1000);
        bank.deposit(b, 1, 2000);

        // Kundenguthaben sind für die Bank Schulden
        assertEquals(-3000, bank.getBalance());
    }

    @Test
    @DisplayName("Top 5: die 5 Konten mit dem höchsten Saldo, absteigend")
    public void testTop5() {
        create6Accounts();

        bank.printTop5();

        String[] lines = output.toString().trim().split("\\R");
        assertEquals(5, lines.length);
        assertEquals("S-1005: 600", lines[0]);
        assertEquals("S-1001: 200", lines[4]);
    }

    @Test
    @DisplayName("Bottom 5: die 5 Konten mit dem tiefsten Saldo, aufsteigend")
    public void testBottom5() {
        create6Accounts();

        bank.printBottom5();

        String[] lines = output.toString().trim().split("\\R");
        assertEquals(5, lines.length);
        assertEquals("S-1000: 100", lines[0]);
        assertEquals("S-1004: 500", lines[4]);
    }

    @Test
    @DisplayName("Referenz auf ein Konto setzen und lesen")
    public void testReferences() {
        assertNull(bank.getAccount());

        SavingsAccount account = new SavingsAccount("S-1");
        bank.setAccount(account);

        assertSame(account, bank.getAccount());
    }

    /** Erstellt 6 Sparkonten S-1000 bis S-1005 mit Saldo 100, 200, ... 600. */
    private void create6Accounts() {
        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, i * 100);
        }
    }
}
