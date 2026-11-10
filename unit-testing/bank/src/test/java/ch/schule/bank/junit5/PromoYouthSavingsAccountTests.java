package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für das Promo-Jugend-Sparkonto (1 % Bonus auf jede Einzahlung).
 */
public class PromoYouthSavingsAccountTests {

    @Test
    @DisplayName("Jede Einzahlung erhält 1 % Bonus")
    public void test() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1");

        assertTrue(account.deposit(1, 10000));
        assertEquals(10100, account.getBalance());

        // Bonus wird abgerundet: 99 / 100 = 0
        assertTrue(account.deposit(2, 99));
        assertEquals(10199, account.getBalance());
    }

    @Test
    @DisplayName("Negative Einzahlung wird abgelehnt")
    public void testNegativeDeposit() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1");

        assertFalse(account.deposit(1, -10000));
        assertEquals(0, account.getBalance());
    }

    @Test
    @DisplayName("Abheben funktioniert wie beim Sparkonto (nicht unter 0)")
    public void testWithdraw() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1");
        account.deposit(1, 10000);  // Saldo 10100

        assertFalse(account.withdraw(2, 20000));
        assertTrue(account.withdraw(2, 10100));
        assertEquals(0, account.getBalance());
    }
}
