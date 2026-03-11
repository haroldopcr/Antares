package com.ingeniusapps.antares.math;

/**
 * Define los tipos de redondeo soportados por la librería.
 *
 * <p>Este enumerado permite expresar de forma semántica la estrategia de
 * redondeo que debe aplicarse en operaciones matemáticas o financieras dentro
 * de la librería Antares.</p>
 *
 * <p>Actualmente se encuentra disponible únicamente el redondeo legal, pensado
 * para escenarios donde el valor final debe ajustarse conforme a una regla
 * formal de redondeo definida por la aplicación.</p>
 */
public enum RoundType {

    /**
     * Representa el redondeo legal utilizado por la librería.
     *
     * <p>Este valor puede emplearse en contextos contables, financieros o de
     * negocio donde se requiera aplicar una política formal de redondeo.</p>
     */
    LEGAL_ROUND
}