package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für das Lohnkonto (darf bis zur Kreditlimite ins Minus).
 */
public class SalaryAccountTests {

    @Test
    @DisplayName("Abheben bis genau zur Kreditlimite ist erlaubt, darüber nicht")
    public void test() {
        SalaryAccount account = new SalaryAccount("P-1", -1000);

        assertTrue(account.withdraw(1, 1000));      // Saldo -1000 = Limite
        assertEquals(-1000, account.getBalance());

        assertFalse(account.withdraw(2, 1));        // -1001 wäre unter der Limite
        assertEquals(-1000, account.getBalance());
    }

    @Test
    @DisplayName("Nach einer Einzahlung kann wieder abgehoben werden")
    public void testDepositAndWithdraw() {
        SalaryAccount account = new SalaryAccount("P-1", -1000);
        account.deposit(1, 500);

        assertTrue(account.withdraw(2, 1500));
        assertEquals(-1000, account.getBalance());
    }

    @Test
    @DisplayName("Negativer Betrag beim Abheben wird abgelehnt")
    public void testNegativeWithdraw() {
        SalaryAccount account = new SalaryAccount("P-1", -1000);

        assertFalse(account.withdraw(1, -500));
        assertEquals(0, account.getBalance());
    }
}
