package com.ingeniusapps.antares.dbaccess;

import java.util.ArrayList;

public class DBRecordSet {

    private final ArrayList<DBRecord> records;

    public DBRecordSet() {
        this.records = new ArrayList<>();
    }

    public DBRecord addRecord() {
        DBRecord dbRecord = new DBRecord();

        this.records.add(dbRecord);

        return dbRecord;
    }

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

    public ArrayList<DBRecord> getCollection() {
        return this.records;
    }

    public int count() {
        return this.records.size();
    }

}
