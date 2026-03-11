package com.ingeniusapps.antares.math;

import static com.ingeniusapps.antares.system.FunctionBox.legalAmountDecimals;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

/**
 * Proporciona operaciones matemáticas de precisión controlada para la librería Antares.
 *
 * <p>Esta clase centraliza operaciones aritméticas básicas implementadas con
 * {@link BigDecimal} y {@link MathContext#DECIMAL64}, con el objetivo de reducir
 * problemas comunes de precisión asociados al uso directo de tipos de punto flotante.</p>
 *
 * <p>Además de las variantes estándar, la clase incluye versiones con redondeo
 * aplicado según el {@link RoundType} indicado, así como utilidades auxiliares
 * como la generación de números enteros aleatorios.</p>
 *
 * <p>Esta clase es utilitaria y no debe ser instanciada.</p>
 */
public final class Calculator {

    /**
     * Evita la instanciación accidental de esta clase utilitaria.
     */
    private Calculator() {
        throw new AssertionError("This class must not be instantiated.");
    }

    // <editor-fold defaultstate="collapsed" desc="High-precision Math">

    /**
     * Multiplica dos valores utilizando precisión decimal controlada.
     *
     * @param a primer operando
     * @param b segundo operando
     * @return resultado de la multiplicación
     */
    public static double multiply(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.multiply(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    /**
     * Multiplica dos valores y aplica redondeo al resultado según el tipo indicado.
     *
     * @param a primer operando
     * @param b segundo operando
     * @param longDecimalSize cantidad de decimales deseada en el resultado
     * @param roundType estrategia de redondeo a aplicar
     * @return resultado multiplicado y redondeado según corresponda
     */
    public static double multiplyr(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.multiply(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };
    }

    /**
     * Divide dos valores utilizando precisión decimal controlada.
     *
     * @param a dividendo
     * @param b divisor
     * @return resultado de la división
     */
    public static double divide(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.divide(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    /**
     * Divide dos valores y aplica redondeo al resultado según el tipo indicado.
     *
     * @param a dividendo
     * @param b divisor
     * @param longDecimalSize cantidad de decimales deseada en el resultado
     * @param roundType estrategia de redondeo a aplicar
     * @return resultado dividido y redondeado según corresponda
     */
    public static double divider(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.divide(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };
    }

    /**
     * Suma dos valores utilizando precisión decimal controlada.
     *
     * @param a primer operando
     * @param b segundo operando
     * @return resultado de la suma
     */
    public static double add(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.add(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    /**
     * Suma dos valores y aplica redondeo al resultado según el tipo indicado.
     *
     * @param a primer operando
     * @param b segundo operando
     * @param longDecimalSize cantidad de decimales deseada en el resultado
     * @param roundType estrategia de redondeo a aplicar
     * @return resultado sumado y redondeado según corresponda
     */
    public static double addr(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.add(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };
    }

    /**
     * Resta el segundo valor al primero utilizando precisión decimal controlada.
     *
     * @param a minuendo
     * @param b sustraendo
     * @return resultado de la resta
     */
    public static double substract(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.subtract(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    /**
     * Resta el segundo valor al primero y aplica redondeo al resultado según el tipo indicado.
     *
     * @param a minuendo
     * @param b sustraendo
     * @param longDecimalSize cantidad de decimales deseada en el resultado
     * @param roundType estrategia de redondeo a aplicar
     * @return resultado restado y redondeado según corresponda
     */
    public static double substractr(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.subtract(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other Math Functions">

    /**
     * Genera un número entero aleatorio dentro del rango indicado.
     *
     * @param minValue valor mínimo base
     * @param maxValue valor máximo utilizado en la generación aleatoria
     * @return número entero aleatorio generado
     */
    public static int randomInt(int minValue, int maxValue) {
        Random random = new Random();
        return (random.nextInt(maxValue + 1)) + minValue;
    }
    // </editor-fold>
}