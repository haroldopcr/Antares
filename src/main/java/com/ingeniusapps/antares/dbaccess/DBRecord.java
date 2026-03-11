package com.ingeniusapps.antares.dbaccess;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLXML;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Representa un registro individual de datos compuesto por campos nombrados y
 * sus respectivos valores.
 *
 * <p>Esta clase actúa como contenedor dinámico de pares clave-valor, donde
 * cada clave corresponde al nombre de un campo y cada valor al contenido
 * asociado. Está diseñada para modelar una fila de resultados proveniente
 * de una consulta o de una estructura intermedia de acceso a datos.</p>
 *
 * <p>Además de permitir acceso genérico mediante {@link Object}, la clase
 * proporciona métodos de acceso tipado para los tipos más comunes utilizados
 * en entornos JDBC y de persistencia.</p>
 *
 * <p>La colección interna preserva el orden de inserción de los campos gracias
 * al uso de {@link LinkedHashMap}.</p>
 */
public class DBRecord {

    /**
     * Colección interna de campos del registro.
     *
     * <p>Cada entrada representa un nombre de campo y su valor asociado.</p>
     */
    private LinkedHashMap<String, Object> fields = new LinkedHashMap<>();

    /**
     * Crea una nueva instancia vacía de {@code DBRecord}.
     */
    public DBRecord() {
        this.fields = new LinkedHashMap<>();
    }

    /**
     * Agrega o reemplaza un campo dentro del registro.
     *
     * @param fieldName nombre del campo
     * @param fieldValue valor asociado al campo
     */
    public void addField(String fieldName, Object fieldValue) {
        this.fields.put(fieldName, fieldValue);
    }

    /**
     * Elimina un campo del registro según su nombre.
     *
     * @param fieldName nombre del campo a eliminar
     */
    public void removeField(String fieldName) {
        this.fields.remove(fieldName);
    }

    /**
     * Obtiene el valor bruto de un campo.
     *
     * @param fieldName nombre del campo
     * @return valor almacenado o {@code null} si no existe
     */
    public Object getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Character}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Character}
     */
    public Character getFieldAsCharacter(String fieldName) {
        return (Character) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link String}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link String}
     */
    public String getFieldAsString(String fieldName) {
        return (String) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Short}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Short}
     */
    public Short getFieldAsShort(String fieldName) {
        return (Short) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Integer}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Integer}
     */
    public Integer getFieldAsInteger(String fieldName) {
        return (Integer) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Long}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Long}
     */
    public Long getFieldAsLong(String fieldName) {
        return (Long) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Float}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Float}
     */
    public Float getFieldAsFloat(String fieldName) {
        return (Float) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Double}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Double}
     */
    public Double getFieldAsDouble(String fieldName) {
        return (Double) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Boolean}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Boolean}
     */
    public Boolean getFieldAsBoolean(String fieldName) {
        return (Boolean) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Byte}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Byte}
     */
    public Byte getFieldAsByte(String fieldName) {
        return (Byte) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como arreglo de bytes.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@code byte[]}
     */
    public byte[] getFieldAsArrayByte(String fieldName) {
        return (byte[]) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo de tipo {@link Blob} como arreglo de bytes.
     *
     * @param fieldName nombre del campo
     * @return contenido binario del {@link Blob}, o {@code null} si ocurre un error SQL
     */
    public byte[] getFieldAsBlob(String fieldName) {
        try {
            Blob blob = ((Blob) this.fields.get(fieldName));
            return blob.getBytes(0, (int) blob.length());
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Obtiene el valor de un campo como {@link Date}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Date}
     */
    public Date getFieldAsDate(String fieldName) {
        return (Date) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Time}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Time}
     */
    public Time getFieldAsTime(String fieldName) {
        return (Time) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link SQLXML}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link SQLXML}
     */
    public SQLXML getFieldAsSQLXML(String fieldName) {
        return (SQLXML) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Timestamp}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Timestamp}
     */
    public Timestamp getFieldAsTimestamp(String fieldName) {
        return (Timestamp) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link BigDecimal}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link BigDecimal}
     */
    public BigDecimal getFieldAsBigDecimal(String fieldName) {
        return (BigDecimal) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link BigInteger}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link BigInteger}
     */
    public BigInteger getFieldAsBigInteger(String fieldName) {
        return (BigInteger) this.fields.get(fieldName);
    }

    /**
     * Obtiene el valor de un campo como {@link Clob}.
     *
     * @param fieldName nombre del campo
     * @return valor convertido a {@link Clob}
     */
    public Clob getFieldAsCLob(String fieldName) {
        return (Clob) this.fields.get(fieldName);
    }

    /**
     * Obtiene la colección de entradas del registro.
     *
     * <p>Cada entrada contiene el nombre del campo y su valor asociado.</p>
     *
     * @return conjunto de entradas del registro
     */
    public Set<Map.Entry<String, Object>> getCollection() {
        return this.fields.entrySet();
    }

    /**
     * Convierte un objeto {@link Clob} a {@link String}.
     *
     * <p>El contenido se lee línea por línea y se reconstruye en memoria
     * preservando saltos de línea. Si el valor recibido es {@code null},
     * el método retorna {@code null}.</p>
     *
     * @param clob valor CLOB a convertir
     * @return representación textual del CLOB, {@code null} si el parámetro es nulo,
     *         o el mensaje localizado de la excepción si ocurre un error durante la lectura
     */
    public static String clobToString(Clob clob) {
        if (clob == null) {
            return null;
        }

        try (Reader reader = clob.getCharacterStream(); BufferedReader br = new BufferedReader(reader)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
    }

    /**
     * Obtiene la cantidad de campos contenidos en el registro.
     *
     * @return número de campos almacenados
     */
    public int count() {
        return this.fields.size();
    }

}