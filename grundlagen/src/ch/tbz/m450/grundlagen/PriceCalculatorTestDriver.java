package ch.tbz.m450.grundlagen;

/**
 * Testtreiber (kein Unit-Test-Framework!) für die Preisberechnung.
 *
 * Der Treiber ruft die Schnittstelle calculatePrice(...) mit definierten Eingaben auf,
 * vergleicht das IST-Resultat mit dem SOLL-Resultat aus der Spezifikation und gibt
 * das Ergebnis auf der Konsole aus.
 */
public class PriceCalculatorTestDriver {

    private static final double EPSILON = 0.0001;

    /** Signatur der zu testenden Methode, damit Original und Korrektur getestet werden können. */
    interface PriceFunction {
        double calc(double baseprice, double specialprice, double extraprice, int extras, double discount);
    }

    public static void main(String[] args) {
        PriceCalculator calculator = new PriceCalculator();

        System.out.println("=== Test der ORIGINAL-Implementation (aus der Aufgabe) ===");
        boolean originalOk = test_calculate_price(calculator::calculatePriceOriginal);

        System.out.println();
        System.out.println("=== Test der KORRIGIERTEN Implementation ===");
        boolean fixedOk = test_calculate_price(calculator::calculatePrice);

        System.out.println();
        System.out.println("Original:  " + (originalOk ? "alle Tests OK" : "FEHLER gefunden"));
        System.out.println("Korrigiert: " + (fixedOk ? "alle Tests OK" : "FEHLER gefunden"));

        // Exit-Code != 0, wenn die korrigierte Version nicht alle Tests besteht
        System.exit(fixedOk ? 0 : 1);
    }

    static boolean test_calculate_price(PriceFunction f) {
        boolean test_ok = true;

        // Nr, Beschreibung, baseprice, specialprice, extraprice, extras, discount, SOLL
        test_ok &= check(1, "Nur Grundpreis, kein Rabatt",
                f.calc(20000, 0, 0, 0, 0), 20000);
        test_ok &= check(2, "Grundpreis mit 10% Händlerrabatt",
                f.calc(20000, 0, 0, 0, 10), 18000);
        test_ok &= check(3, "2 Extras -> kein Zubehörrabatt",
                f.calc(20000, 1000, 2000, 2, 0), 23000);
        test_ok &= check(4, "3 Extras (Grenzwert) -> 10% auf Zubehör",
                f.calc(20000, 1000, 2000, 3, 0), 22800);
        test_ok &= check(5, "4 Extras -> 10% auf Zubehör",
                f.calc(20000, 1000, 2000, 4, 0), 22800);
        test_ok &= check(6, "5 Extras (Grenzwert) -> 15% auf Zubehör",
                f.calc(20000, 1000, 2000, 5, 0), 22700);
        test_ok &= check(7, "6 Extras -> 15% auf Zubehör",
                f.calc(20000, 1000, 2000, 6, 0), 22700);
        test_ok &= check(8, "Händlerrabatt 20% darf nicht auf Zubehör wirken",
                f.calc(20000, 0, 2000, 0, 20), 18000);
        test_ok &= check(9, "Händlerrabatt 5% + 3 Extras",
                f.calc(20000, 0, 2000, 3, 5), 20800);
        test_ok &= check(10, "Kombination: 10% Händler, Sondermodell, 6 Extras",
                f.calc(20000, 500, 3000, 6, 10), 21050);
        test_ok &= check(11, "Alles 0",
                f.calc(0, 0, 0, 0, 0), 0);

        return test_ok;
    }

    private static boolean check(int nr, String description, double actual, double expected) {
        boolean ok = Math.abs(actual - expected) < EPSILON;
        System.out.printf("Test %2d %-4s %-50s SOLL=%10.2f IST=%10.2f%n",
                nr, ok ? "OK" : "FAIL", description, expected, actual);
        return ok;
    }
}
