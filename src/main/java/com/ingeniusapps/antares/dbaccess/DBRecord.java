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

public class DBRecord {

    private LinkedHashMap<String, Object> fields = new LinkedHashMap<>();

    public DBRecord() {
        this.fields = new LinkedHashMap<>();
    }

    public void addField(String fieldName, Object fieldValue) {
        this.fields.put(fieldName, fieldValue);
    }

    public void removeField(String fieldName) {
        this.fields.remove(fieldName);
    }

    public Object getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    public Character getFieldAsCharacter(String fieldName) {
        return (Character) this.fields.get(fieldName);
    }

    public String getFieldAsString(String fieldName) {
        return (String) this.fields.get(fieldName);
    }

    public Short getFieldAsShort(String fieldName) {
        return (Short) this.fields.get(fieldName);
    }

    public Integer getFieldAsInteger(String fieldName) {
        return (Integer) this.fields.get(fieldName);
    }

    public Long getFieldAsLong(String fieldName) {
        return (Long) this.fields.get(fieldName);
    }

    public Float getFieldAsFloat(String fieldName) {
        return (Float) this.fields.get(fieldName);
    }

    public Double getFieldAsDouble(String fieldName) {
        return (Double) this.fields.get(fieldName);
    }

    public Boolean getFieldAsBoolean(String fieldName) {
        return (Boolean) this.fields.get(fieldName);
    }

    public Byte getFieldAsByte(String fieldName) {
        return (Byte) this.fields.get(fieldName);
    }

    public byte[] getFieldAsArrayByte(String fieldName) {
        return (byte[]) this.fields.get(fieldName);
    }

    public byte[] getFieldAsBlob(String fieldName) {
        try {
            Blob blob = ((Blob) this.fields.get(fieldName));
            return blob.getBytes(0, (int) blob.length());
        } catch (SQLException ex) {
            return null;
        }
    }

    public Date getFieldAsDate(String fieldName) {
        return (Date) this.fields.get(fieldName);
    }

    public Time getFieldAsTime(String fieldName) {
        return (Time) this.fields.get(fieldName);
    }

    public SQLXML getFieldAsSQLXML(String fieldName) {
        return (SQLXML) this.fields.get(fieldName);
    }

    public Timestamp getFieldAsTimestamp(String fieldName) {
        return (Timestamp) this.fields.get(fieldName);
    }

    public BigDecimal getFieldAsBigDecimal(String fieldName) {
        return (BigDecimal) this.fields.get(fieldName);
    }

    public BigInteger getFieldAsBigInteger(String fieldName) {
        return (BigInteger) this.fields.get(fieldName);
    }

    public Clob getFieldAsCLob(String fieldName) {
        return (Clob) this.fields.get(fieldName);
    }

    public Set<Map.Entry<String, Object>> getCollection() {
        return this.fields.entrySet();
    }

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

    public int count() {
        return this.fields.size();
    }

}
