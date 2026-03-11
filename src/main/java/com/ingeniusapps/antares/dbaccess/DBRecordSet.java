package com.ingeniusapps.antares.dbaccess;

import java.util.ArrayList;

/**
 * Representa un conjunto de registros de datos dentro de una operación de
 * acceso a base de datos.
 *
 * <p>Esta clase actúa como contenedor de múltiples instancias de
 * {@link DBRecord}, permitiendo agregarlas, consultarlas, eliminarlas y
 * acceder a la colección completa.</p>
 *
 * <p>Su propósito es modelar un bloque de resultados homogéneos, donde cada
 * {@link DBRecord} representa una fila o registro individual dentro del
 * conjunto.</p>
 */
public class DBRecordSet {

    /**
     * Colección interna de registros.
     */
    private final ArrayList<DBRecord> records;

    /**
     * Crea una nueva instancia vacía de {@code DBRecordSet}.
     */
    public DBRecordSet() {
        this.records = new ArrayList<>();
    }

    /**
     * Crea un nuevo {@link DBRecord}, lo agrega a la colección interna
     * y lo retorna.
     *
     * @return nuevo registro agregado a la colección
     */
    public DBRecord addRecord() {
        DBRecord dbRecord = new DBRecord();

        this.records.add(dbRecord);

        return dbRecord;
    }

    /**
     * Elimina un registro de la colección según su índice.
     *
     * @param index posición del registro a eliminar
     * @return {@code true} si el elemento fue eliminado correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean removeRecord(int index) {
        try {
            if (index >= this.records.size()) {
                return false;
            }

            this.records.remove(index);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     * Obtiene un registro según su índice dentro de la colección.
     *
     * @param index posición del registro a obtener
     * @return registro correspondiente, o {@code null} si el índice no es válido
     */
    public DBRecord getRecord(int index) {
        try {
            if (index >= this.records.size()) {
                return null;
            }

            return this.records.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Obtiene la colección interna completa de registros.
     *
     * <p>El valor retornado corresponde a la colección real mantenida por la
     * instancia.</p>
     *
     * @return colección interna de {@link DBRecord}
     */
    public ArrayList<DBRecord> getCollection() {
        return this.records;
    }

    /**
     * Obtiene la cantidad de registros almacenados.
     *
     * @return número de elementos contenidos en la colección
     */
    public int count() {
        return this.records.size();
    }

}