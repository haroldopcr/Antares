package com.ingeniusapps.antares.dbaccess;

/**
 * Enumeración que define los tipos de controladores de base de datos
 * soportados por el módulo de acceso a datos de Antares.
 *
 * <p>Este enum permite identificar de forma explícita qué motor de base de
 * datos se utilizará en una operación o configuración determinada. Puede ser
 * utilizado por componentes de conexión, factorías de drivers o capas de
 * abstracción de persistencia.</p>
 */
public enum DBDriverType {

    /**
     * Motor de base de datos MariaDB.
     */
    MARIADB,

    /**
     * Motor de base de datos H2.
     */
    H2

}