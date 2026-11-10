package ch.schule.bank.junit5;

import ch.schule.AccountBalanceComparator;
import ch.schule.AccountInverseBalanceComparator;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für das Sparkonto (darf nicht ins Minus) und die Comparatoren.
 */
public class SavingsAccountTests {

    private SavingsAccount account;

    @BeforeEach
    public void setUp() {
        account = new SavingsAccount("S-1");
        account.deposit(1, 1000);
    }

    @Test
    @DisplayName("Abheben bis 0 ist erlaubt, ins Minus nicht")
    public void test() {
        assertFalse(account.withdraw(2, 1001));
        assertEquals(1000, account.getBalance());

        assertTrue(account.withdraw(2, 1000));
        assertEquals(0, account.getBalance());
    }

    @Test
    @DisplayName("Comparatoren sortieren nach Saldo (absteigend / aufsteigend)")
    public void testComparators() {
        SavingsAccount rich = new SavingsAccount("S-2");
        rich.deposit(1, 5000);
        SavingsAccount same = new SavingsAccount("S-3");
        same.deposit(1, 1000);

        AccountBalanceComparator desc = new AccountBalanceComparator();
        assertEquals(1, desc.compare(account, rich));   // account kommt nach rich
        assertEquals(-1, desc.compare(rich, account));
        assertEquals(0, desc.compare(account, same));

        AccountInverseBalanceComparator asc = new AccountInverseBalanceComparator();
        assertEquals(-1, asc.compare(account, rich));   // account kommt vor rich
        assertEquals(1, asc.compare(rich, account));
        assertEquals(0, asc.compare(account, same));
    }
}
