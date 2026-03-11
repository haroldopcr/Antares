package com.ingeniusapps.antares.dbaccess;

/**
 * Contenedor simple para una colección ordenada de parámetros de base de datos.
 *
 * <p>Esta clase se utiliza para encapsular un conjunto variable de valores
 * que pueden ser consumidos posteriormente por operaciones de acceso a datos,
 * por ejemplo al preparar sentencias SQL parametrizadas o invocar utilidades
 * internas de persistencia.</p>
 *
 * <p>Los parámetros se almacenan en el mismo orden en que son recibidos en el
 * constructor.</p>
 */
public class DBParameters {

    /**
     * Arreglo interno de parámetros.
     */
    private Object[] parameters = null;

    /**
     * Crea una nueva instancia con una cantidad variable de parámetros.
     *
     * @param parameters valores a encapsular en esta instancia
     */
    public DBParameters(Object... parameters) {
        this.parameters = parameters;
    }

    /**
     * Obtiene el arreglo de parámetros almacenado.
     *
     * <p>Si la colección interna de parámetros es {@code null}, el método
     * retorna un arreglo vacío.</p>
     *
     * @return arreglo de parámetros encapsulado, o un arreglo vacío si no hay datos
     */
    public Object[] getParameters() {
        return this.parameters != null ? parameters : new Object[0];
    }

}