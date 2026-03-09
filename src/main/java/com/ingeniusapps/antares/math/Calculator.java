package com.ingeniusapps.antares.math;

import static com.ingeniusapps.antares.math.RoundType.LEGAL_ROUND;
import static com.ingeniusapps.antares.system.FunctionBox.legalAmountDecimals;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

/**
 * @author Harold Ortega Pérez.
 * @version 1.0
 *
 */
public class Calculator {

    // <editor-fold defaultstate="collapsed" desc="High-precision Math">
    public static double multiply(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.multiply(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    public static double multiplyr(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.multiply(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };        
    }

    public static double divide(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.divide(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    public static double divider(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.divide(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };          
    }

    public static double add(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.add(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

    public static double addr(double a, double b, int longDecimalSize, RoundType roundType) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.add(calc_b, MathContext.DECIMAL64);

        return switch (roundType) {
            case LEGAL_ROUND -> legalAmountDecimals(calc_r.doubleValue(), longDecimalSize);
            default -> calc_r.doubleValue();
        };                  
    }

    public static double substract(double a, double b) {
        BigDecimal calc_a = BigDecimal.valueOf(a);
        BigDecimal calc_b = BigDecimal.valueOf(b);

        BigDecimal calc_r = calc_a.subtract(calc_b, MathContext.DECIMAL64);

        return calc_r.doubleValue();
    }

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
    public static int randomInt(int minValue, int maxValue) {
        Random random = new Random();
        return (random.nextInt(maxValue + 1)) + minValue;
    }
    // </editor-fold>
}
