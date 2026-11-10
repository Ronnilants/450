package ch.tbz.bank.software;

import java.io.ByteArrayInputStream;

/**
 * Einfacher Testtreiber für die Bank-Software (ohne Test-Framework).
 * Jeder Test vergleicht IST mit SOLL und gibt OK oder FEHLER aus.
 */
public class BankTestDriver {

    static int ok = 0;
    static int fehler = 0;

    public static void main(String[] args) {
        // Account
        testDeposit();
        testWithdraw();
        testWithdrawTooMuch();
        testWithdrawWholeBalance();
        testDepositNegative();
        testWithdrawNegative();
        testDepositNaN();

        // Bank
        testCreateAccount();
        testGetAccountNotFound();
        testDeleteAccount();

        // Counter
        testTransferSameCurrency();
        testTransferUsdToChf();
        testTransferEurToChf();
        testTransferNegative();

        System.out.println();
        System.out.println("Resultat: " + ok + " OK, " + fehler + " FEHLER");
    }

    // ---------- Account ----------

    static void testDeposit() {
        Account a = new Account("Test", Currency.CHF, 100);
        a.deposit(50);
        check("Einzahlen 50 auf 100", 150, a.getBalance());
    }

    static void testWithdraw() {
        Account a = new Account("Test", Currency.CHF, 100);
        a.withdraw(30);
        check("Abheben 30 von 100", 70, a.getBalance());
    }

    static void testWithdrawTooMuch() {
        Account a = new Account("Test", Currency.CHF, 100);
        boolean result = a.withdraw(200);
        check("Abheben 200 von 100 wird abgelehnt", false, result);
        check("Kontostand bleibt 100", 100, a.getBalance());
    }

    static void testWithdrawWholeBalance() {
        Account a = new Account("Test", Currency.CHF, 100);
        a.withdraw(100);
        check("Ganzen Kontostand abheben", 0, a.getBalance());
    }

    static void testDepositNegative() {
        Account a = new Account("Test", Currency.CHF, 100);
        a.deposit(-1000);
        check("Negativer Betrag einzahlen wird ignoriert", 100, a.getBalance());
    }

    static void testWithdrawNegative() {
        Account a = new Account("Test", Currency.CHF, 100);
        boolean result = a.withdraw(-1000);
        check("Negativer Betrag abheben wird abgelehnt", false, result);
    }

    static void testDepositNaN() {
        Account a = new Account("Test", Currency.CHF, 100);
        a.deposit(Double.NaN);
        check("NaN einzahlen wird ignoriert", 100, a.getBalance());
    }

    // ---------- Bank ----------

    static void testCreateAccount() {
        Bank bank = new Bank();
        Account a = bank.createAccount("Meier", Currency.EUR, 500);
        check("Neues Konto ist in der Bank", true, bank.getAccount(a.getId()) == a);
        check("Anzahl Konten ist 1", 1, bank.getNumberOfAccounts());
    }

    static void testGetAccountNotFound() {
        Bank bank = new Bank();
        check("Nicht existierendes Konto gibt null", true, bank.getAccount(9999) == null);
    }

    static void testDeleteAccount() {
        Bank bank = new Bank();
        Account a = bank.createAccount("Meier", Currency.EUR, 500);
        bank.deleteAccount(a);
        check("Gelöschtes Konto ist weg", true, bank.getAccount(a.getId()) == null);
    }

    // ---------- Counter (Überweisung) ----------

    /** Simuliert die Eingabe eines Betrags und überweist. */
    static void transfer(Bank bank, Account from, Account to, String betrag) {
        System.setIn(new ByteArrayInputStream((betrag + "\n").getBytes()));
        Counter counter = new Counter(bank);
        counter.transferAmount(from, to);
        System.out.println();
    }

    static void testTransferSameCurrency() {
        Bank bank = new Bank();
        Account from = bank.createAccount("A", Currency.CHF, 1000);
        Account to = bank.createAccount("B", Currency.CHF, 0);
        transfer(bank, from, to, "100");
        check("Überweisung CHF->CHF: Absender", 900, from.getBalance());
        check("Überweisung CHF->CHF: Empfänger", 100, to.getBalance());
    }

    static void testTransferUsdToChf() {
        Bank bank = new Bank();
        Account from = bank.createAccount("A", Currency.USD, 1000);
        Account to = bank.createAccount("B", Currency.CHF, 0);
        transfer(bank, from, to, "100");
        check("Überweisung USD->CHF wird umgerechnet (Kurs 1.11)", 111, to.getBalance());
    }

    static void testTransferEurToChf() {
        Bank bank = new Bank();
        Account from = bank.createAccount("A", Currency.EUR, 1000);
        Account to = bank.createAccount("B", Currency.CHF, 0);
        transfer(bank, from, to, "100");
        check("Überweisung EUR->CHF wird umgerechnet", true, to.getBalance() != 100);
    }

    static void testTransferNegative() {
        Bank bank = new Bank();
        Account from = bank.createAccount("A", Currency.CHF, 1000);
        Account to = bank.createAccount("B", Currency.CHF, 1000);
        // Nach dem abgelehnten Betrag wird ein gültiger Betrag (10) eingegeben
        transfer(bank, from, to, "-500\n10");
        check("Negativer Betrag überweisen wird abgelehnt", 990, from.getBalance());
    }

    // ---------- Hilfsmethoden ----------

    static void check(String name, double soll, double ist) {
        print(name, Math.abs(soll - ist) < 0.001, String.valueOf(soll), String.valueOf(ist));
    }

    static void check(String name, boolean soll, boolean ist) {
        print(name, soll == ist, String.valueOf(soll), String.valueOf(ist));
    }

    static void print(String name, boolean passed, String soll, String ist) {
        if (passed) {
            ok++;
            System.out.println("OK      " + name);
        } else {
            fehler++;
            System.out.println("FEHLER  " + name + "  (SOLL: " + soll + ", IST: " + ist + ")");
        }
    }
}
