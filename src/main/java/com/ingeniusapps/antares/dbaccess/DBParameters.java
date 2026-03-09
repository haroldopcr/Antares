package com.ingeniusapps.antares.dbaccess;

public class DBParameters {

    private Object[] parameters = null;

    public DBParameters(Object... parameters) {
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return this.parameters != null ? parameters : new Object[0];
    }

}
