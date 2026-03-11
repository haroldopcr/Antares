package com.ingeniusapps.antares.dbaccess;

import java.util.ArrayList;

/**
 * Representa una colección de conjuntos de registros obtenidos o construidos
 * dentro de una operación de acceso a datos.
 *
 * <p>Esta clase actúa como contenedor de múltiples instancias de
 * {@link DBRecordSet}, permitiendo agregarlas, consultarlas, eliminarlas y
 * acceder a la colección completa.</p>
 *
 * <p>Su propósito es agrupar uno o varios conjuntos de resultados bajo una
 * misma estructura, lo que resulta útil en operaciones que devuelven múltiples
 * bloques de datos relacionados.</p>
 */
public class DBResultSet {

    /**
     * Colección interna de conjuntos de registros.
     */
    private final ArrayList<DBRecordSet> recordsets;

    /**
     * Crea una nueva instancia vacía de {@code DBResultSet}.
     */
    public DBResultSet() {
        this.recordsets = new ArrayList<>();
    }

    /**
     * Crea un nuevo {@link DBRecordSet}, lo agrega a la colección interna
     * y lo retorna.
     *
     * @return nuevo conjunto de registros agregado a la colección
     */
    public DBRecordSet addRecordSet() {
        DBRecordSet dbRecordSet = new DBRecordSet();

        this.recordsets.add(dbRecordSet);

        return dbRecordSet;
    }

    /**
     * Elimina un conjunto de registros de la colección según su índice.
     *
     * @param index posición del conjunto de registros a eliminar
     * @return {@code true} si el elemento fue eliminado correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean removeRecordSet(int index) {
        try {
            if (index >= this.recordsets.size()) {
                return false;
            }

            this.recordsets.remove(index);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     * Obtiene un conjunto de registros según su índice dentro de la colección.
     *
     * @param index posición del conjunto de registros a obtener
     * @return conjunto de registros correspondiente, o {@code null} si el índice
     *         no es válido
     */
    public DBRecordSet getRecordSet(int index) {
        try {
            if (index >= this.recordsets.size()) {
                return null;
            }

            return this.recordsets.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Obtiene el primer conjunto de registros de la colección.
     *
     * @return primer conjunto de registros, o {@code null} si la colección está vacía
     */
    public DBRecordSet getRecordSet() {
        try {
            return this.recordsets.getFirst();
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Obtiene la colección interna completa de conjuntos de registros.
     *
     * <p>El valor retornado corresponde a la colección real mantenida por la
     * instancia.</p>
     *
     * @return colección interna de {@link DBRecordSet}
     */
    public ArrayList<DBRecordSet> getCollection() {
        return this.recordsets;
    }

    /**
     * Obtiene la cantidad de conjuntos de registros almacenados.
     *
     * @return número de elementos contenidos en la colección
     */
    public int count() {
        return this.recordsets.size();
    }

}