package ch.tbz.m450.grundlagen;

/**
 * Preisberechnung der Auto-Verkauf Software.
 *
 * Enthält zwei Varianten:
 *  - calculatePriceOriginal: der Code genau so wie in der Aufgabe vorgegeben (fehlerhaft)
 *  - calculatePrice:         die korrigierte Version (siehe Bonus-Aufgabe)
 */
public class PriceCalculator {

    /** Originalcode aus der Aufgabenstellung - absichtlich unverändert (enthält Fehler). */
    public double calculatePriceOriginal(double baseprice, double specialprice, double extraprice,
                                         int extras, double discount) {
        double addon_discount;
        double result;

        if (extras >= 3)
            addon_discount = 10;
        else if (extras >= 5)          // Fehler 1: wird nie erreicht
            addon_discount = 15;
        else
            addon_discount = 0;

        if (discount > addon_discount) // Fehler 2: Händlerrabatt wirkt auf Zubehör
            addon_discount = discount;

        result = baseprice / 100.0 * (100 - discount) + specialprice
                + extraprice / 100.0 * (100 - addon_discount);

        return result;
    }

    /** Korrigierte Version gemäss Spezifikation. */
    public double calculatePrice(double baseprice, double specialprice, double extraprice,
                                 int extras, double discount) {
        double addonDiscount;

        if (extras >= 5)
            addonDiscount = 15;
        else if (extras >= 3)
            addonDiscount = 10;
        else
            addonDiscount = 0;

        // Händlerrabatt gilt nur für den Grundpreis, Zubehörrabatt nur für die Zubehörteile.
        return baseprice / 100.0 * (100 - discount)
                + specialprice
                + extraprice / 100.0 * (100 - addonDiscount);
    }
}
