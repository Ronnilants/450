package ch.tbz.m450.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für den Calculator.
 */
class CalculatorTest {

    private static final double DELTA = 0.0001; // erlaubte Abweichung bei double

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // ---------- Addition ----------

    @Test
    @DisplayName("Addition: zwei positive Zahlen")
    void addPositive() {
        assertEquals(5, calculator.add(2, 3), DELTA);
    }

    @Test
    @DisplayName("Addition: mit negativer Zahl und mit 0")
    void addNegativeAndZero() {
        assertEquals(-1, calculator.add(2, -3), DELTA);
        assertEquals(7, calculator.add(7, 0), DELTA);
    }

    @Test
    @DisplayName("Addition: Kommazahlen (0.1 + 0.2)")
    void addDecimals() {
        // ohne DELTA würde dieser Test fehlschlagen: 0.1 + 0.2 = 0.30000000000000004
        assertEquals(0.3, calculator.add(0.1, 0.2), DELTA);
    }

    // ---------- Subtraktion ----------

    @Test
    @DisplayName("Subtraktion: Resultat positiv und negativ")
    void subtract() {
        assertEquals(2, calculator.subtract(5, 3), DELTA);
        assertEquals(-2, calculator.subtract(3, 5), DELTA);
        assertEquals(0, calculator.subtract(4, 4), DELTA);
    }

    // ---------- Multiplikation ----------

    @Test
    @DisplayName("Multiplikation: positiv, negativ und mit 0")
    void multiply() {
        assertEquals(12, calculator.multiply(3, 4), DELTA);
        assertEquals(-12, calculator.multiply(-3, 4), DELTA);
        assertEquals(12, calculator.multiply(-3, -4), DELTA);
        assertEquals(0, calculator.multiply(3, 0), DELTA);
    }

    // ---------- Division ----------

    @Test
    @DisplayName("Division: ganzzahlig und mit Kommastellen")
    void divide() {
        assertEquals(2, calculator.divide(6, 3), DELTA);
        assertEquals(2.5, calculator.divide(5, 2), DELTA);
        assertEquals(-2, calculator.divide(6, -3), DELTA);
    }

    @Test
    @DisplayName("Division: 0 geteilt durch eine Zahl ist 0")
    void divideZero() {
        assertEquals(0, calculator.divide(0, 5), DELTA);
    }

    @Test
    @DisplayName("Division durch 0 wirft eine ArithmeticException")
    void divideByZero() {
        ArithmeticException exception = assertThrows(ArithmeticException.class,
                () -> calculator.divide(5, 0));

        assertEquals("Division durch 0 ist nicht erlaubt", exception.getMessage());
    }

    // ---------- Parametrisierter Test ----------

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "1,    1,    2",
            "-1,   1,    0",
            "0,    0,    0",
            "2.5,  2.5,  5",
            "1000000, 1, 1000001"
    })
    @DisplayName("Addition mit mehreren Werten")
    void addParameterized(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), DELTA);
    }
}
