package com.ingeniusapps.antares.dbaccess;

import java.util.ArrayList;

public class DBResultSet {

    private final ArrayList<DBRecordSet> recordsets;

    public DBResultSet() {
        this.recordsets = new ArrayList<>();
    }

    public DBRecordSet addRecordSet() {
        DBRecordSet dbRecordSet = new DBRecordSet();

        this.recordsets.add(dbRecordSet);

        return dbRecordSet;
    }

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

    public DBRecordSet getRecordSet() {
        try {
            return this.recordsets.getFirst();
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public ArrayList<DBRecordSet> getCollection() {
        return this.recordsets;
    }

    public int count() {
        return this.recordsets.size();
    }

}
