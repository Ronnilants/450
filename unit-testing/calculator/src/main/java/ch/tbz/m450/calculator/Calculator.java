package ch.tbz.m450.calculator;

/**
 * Einfacher Rechner mit den vier Grundrechenarten.
 */
public class Calculator {

    public double add(double summand1, double summand2) {
        return summand1 + summand2;
    }

    public double subtract(double minuend, double subtrahend) {
        return minuend - subtrahend;
    }

    public double multiply(double factor1, double factor2) {
        return factor1 * factor2;
    }

    /**
     * @throws ArithmeticException bei Division durch 0
     */
    public double divide(double dividend, double divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Division durch 0 ist nicht erlaubt");
        }
        return dividend / divisor;
    }
}
