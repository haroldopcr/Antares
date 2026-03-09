package com.ingeniusapps.antares.dbaccess;

import static com.ingeniusapps.antares.dbaccess.DBDriverType.H2;
import static com.ingeniusapps.antares.dbaccess.DBDriverType.MARIADB;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLXML;

public class DBAccess {

    private boolean executeTracker;
    private boolean useSSL;
    private DBDriverType dbDriverType;
    private String lastOperationResult = "OK";
    private Connection connection = null;

    /**
     * Get the value of lastOperationResult
     *
     * @return the value of lastOperationResult
     */
    public String getLastOperationResult() {
        return lastOperationResult;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Create an DBAccess instance.
     *
     * @param dbDriverType Data Base Driver to use.
     * @param useSSL True if the connection will use SSL. WILL BE IMPLEMENTED IN
     * A FUTURE VERSION.
     */
    public DBAccess(DBDriverType dbDriverType, boolean useSSL) {
        this.newAccess(dbDriverType, useSSL, false);
    }

    /**
     * Create an DBAccess instance.
     *
     * @param dbDriverType Data Base Driver to use.
     * @param useSSL True if the connection will use SSL. WILL BE IMPLEMENTED IN
     * A FUTURE VERSION.
     * @param executeTracker True makes the system print the executed stored
     * procedure to the console.
     */
    public DBAccess(DBDriverType dbDriverType, boolean useSSL, boolean executeTracker) {
        this.newAccess(dbDriverType, useSSL, executeTracker);
    }

    private void newAccess(DBDriverType dbDriverType, boolean useSSL, boolean executeTracker) {
        try {
            String className;
            className = switch (dbDriverType) {
                case MARIADB ->
                    "org.mariadb.jdbc.Driver";
                case H2 ->
                    "org.h2.Driver";
            };

            Class.forName(className).getDeclaredConstructor().newInstance();

            this.executeTracker = executeTracker;
            this.useSSL = useSSL;
            this.dbDriverType = dbDriverType;
            this.lastOperationResult = "OK";
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();
        }
    }

    /**
     * Create a connection to a data base.
     *
     * @param host Host URL.
     * @param database Data base name.
     * @param user Username.
     * @param password Password.
     * @return True if the connection was established.
     */
    public boolean Connect(String host, String database, String user, String password) {
        try {
            String url_db;
            url_db = switch (dbDriverType) {
                case MARIADB ->
                    "jdbc:mariadb://" + host + "/" + database + "?noAccessToProcedureBodies=true" + (this.useSSL ? "" : "");
                case H2 ->
                    "jdbc:h2:file:" + host + "/" + database
                    + (password.contains(" ") ? ";CIPHER=AES" : "") + ";DB_CLOSE_ON_EXIT=TRUE;AUTO_SERVER=TRUE";
            };

            Connection newConnection;
            newConnection = (Connection) DriverManager.getConnection(url_db, user, password);
            newConnection.setTransactionIsolation(TRANSACTION_READ_COMMITTED);
            newConnection.setAutoCommit(false);

            this.connection = newConnection;
            this.lastOperationResult = "OK";

            return true;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return false;
        }
    }

    /**
     * Disconnect the connection with the data base.
     *
     * @return True if the connection was disconnected.
     */
    public boolean Disconnect() {
        try {
            if (getConnection() != null && !connection.isClosed()) {
                getConnection().close();
            }

            this.connection = null;
            this.lastOperationResult = "OK";

            return true;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return false;
        }
    }

    /**
     * Commit the pool of transactions.
     *
     * @return True if the transaction was commited.
     */
    public boolean Commit() {
        try {
            this.getConnection().commit();

            this.lastOperationResult = "OK";

            return true;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return false;
        }
    }

    /**
     * Rollback the pool of transactions.
     *
     * @return True if the transaction was rolled back.
     */
    public boolean Rollback() {
        try {
            this.getConnection().rollback();

            this.lastOperationResult = "OK";

            return true;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return false;
        }
    }

    /**
     * Start Transaction of the open connection.
     *
     * @return True if the transaction was started.
     */
    public boolean StartTransaction() {
        try {
            this.getConnection().prepareStatement("START TRANSACTION").execute();

            this.lastOperationResult = "OK";

            return true;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return false;
        }
    }

    /**
     * Execute a stored procedure.
     *
     * @param storedProcedure Stored procedure name.
     * @param parameters Parameters to call the stored procedure.
     * @return Objects with the stored procedure response.
     */
    public DBResultSet callStoredProcedure(String storedProcedure, DBParameters parameters) {
        try {
            /* Stored Procedure inputs. */
            Object[] dbParams = parameters.getParameters();

            /* Stored Procedure response. */
            DBResultSet dbResultSet = new DBResultSet();

            /* Preparing call to the stored procedure with parameters. */
            CallableStatement callableStatement = getConnection().prepareCall("{call " + storedProcedure + this.formatSqlParameters(dbParams) + "}");
            callableStatement.setFetchSize(2000);
            callableStatement.setFetchDirection(ResultSet.FETCH_FORWARD);

            for (int i = 0; i < dbParams.length; i++) {
                if (dbParams[i] == null) {
                    callableStatement.setNull(i + 1, java.sql.Types.NULL);
                } else {
                    switch (dbParams[i]) {
                        case String value ->
                            callableStatement.setString(i + 1, value);

                        case Short value ->
                            callableStatement.setShort(i + 1, value);

                        case Integer value ->
                            callableStatement.setInt(i + 1, value);

                        case Long value ->
                            callableStatement.setLong(i + 1, value);

                        case Float value ->
                            callableStatement.setFloat(i + 1, value);

                        case Double value ->
                            callableStatement.setDouble(i + 1, value);

                        case Boolean value ->
                            callableStatement.setBoolean(i + 1, value);

                        case Byte value ->
                            callableStatement.setByte(i + 1, value);

                        case byte[] value ->
                            callableStatement.setBytes(i + 1, value);

                        case Date value ->
                            callableStatement.setDate(i + 1, value);

                        case Time value ->
                            callableStatement.setTime(i + 1, value);

                        case BigDecimal value ->
                            callableStatement.setBigDecimal(i + 1, value);

                        case SQLXML value ->
                            callableStatement.setSQLXML(i + 1, value);

                        case Timestamp value ->
                            callableStatement.setTimestamp(i + 1, value);

                        case Clob value ->
                            callableStatement.setClob(i + 1, value);

                        default -> {
                            callableStatement.setString(i + 1, dbParams[i].toString());
                        }
                    }
                }
            }

            /* Execute the Stored Procedure. */
            if (this.executeTracker) {
                this.getDebugTrace(storedProcedure);
            }
            boolean hasResults = callableStatement.execute();

            /* Load Stored Procedure results. */
            while (hasResults) {
                DBRecordSet dbRecordSet = dbResultSet.addRecordSet();

                ResultSet resultSet = callableStatement.getResultSet();
                while (resultSet.next()) {
                    ResultSetMetaData metaResultSet = (ResultSetMetaData) resultSet.getMetaData();

                    DBRecord dbRecord = dbRecordSet.addRecord();
                    for (int i = 1; i <= metaResultSet.getColumnCount(); i++) {
                        dbRecord.addField(metaResultSet.getColumnName(i), resultSet.getObject(i));
                    }
                }

                if (!resultSet.isClosed()) {
                    resultSet.close();
                }

                hasResults = callableStatement.getMoreResults();
            }

            if (!callableStatement.isClosed()) {
                callableStatement.close();
            }

            this.lastOperationResult = "OK";

            return dbResultSet;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return null;
        }
    }

    /**
     * Execute a query.
     *
     * @param query SQL Operation to execute.
     * @param parameters Parameters for the query.
     * @return Objects with the query response.
     */
    public DBResultSet executeQuery(String query, DBParameters parameters) {
        try {
            /* Stored Procedure inputs. */
            Object[] dbParams = parameters.getParameters();

            /* Stored Procedure response. */
            DBResultSet dbResultSet = new DBResultSet();

            /* Preparing call to the stored procedure with parameters. */
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setFetchSize(2000);
            preparedStatement.setFetchDirection(ResultSet.FETCH_FORWARD);

            for (int i = 0; i < dbParams.length; i++) {
                if (dbParams[i] == null) {
                    preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                } else {
                    switch (dbParams[i]) {
                        case String value ->
                            preparedStatement.setString(i + 1, value);

                        case Short value ->
                            preparedStatement.setShort(i + 1, value);

                        case Integer value ->
                            preparedStatement.setInt(i + 1, value);

                        case Long value ->
                            preparedStatement.setLong(i + 1, value);

                        case Float value ->
                            preparedStatement.setFloat(i + 1, value);

                        case Double value ->
                            preparedStatement.setDouble(i + 1, value);

                        case Boolean value ->
                            preparedStatement.setBoolean(i + 1, value);

                        case Byte value ->
                            preparedStatement.setByte(i + 1, value);

                        case byte[] value ->
                            preparedStatement.setBytes(i + 1, value);

                        case Blob value ->
                            preparedStatement.setBlob(i + 1, value);

                        case Date value ->
                            preparedStatement.setDate(i + 1, value);

                        case Time value ->
                            preparedStatement.setTime(i + 1, value);

                        case BigDecimal value ->
                            preparedStatement.setBigDecimal(i + 1, value);

                        case SQLXML value ->
                            preparedStatement.setSQLXML(i + 1, value);

                        case Timestamp value ->
                            preparedStatement.setTimestamp(i + 1, value);

                        case Clob value ->
                            preparedStatement.setClob(i + 1, value);

                        default -> {
                            preparedStatement.setString(i + 1, dbParams[i].toString());
                        }
                    }
                }
            }

            /* Execute the Stored Procedure. */
            if (this.executeTracker) {
                this.getDebugTrace(query);
            }
            boolean hasResults = preparedStatement.execute();

            /* Load Stored Procedure results. */
            while (hasResults) {
                DBRecordSet dbRecordSet = dbResultSet.addRecordSet();

                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    ResultSetMetaData metaResultSet = (ResultSetMetaData) resultSet.getMetaData();

                    DBRecord dbRecord = dbRecordSet.addRecord();
                    for (int i = 1; i <= metaResultSet.getColumnCount(); i++) {
                        dbRecord.addField(metaResultSet.getColumnName(i), resultSet.getObject(i));
                    }
                }

                if (!resultSet.isClosed()) {
                    resultSet.close();
                }

                hasResults = preparedStatement.getMoreResults();
            }

            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }

            this.lastOperationResult = "OK";

            return dbResultSet;
        } catch (SQLException ex) {
            this.lastOperationResult = ex.getLocalizedMessage();

            return null;
        }
    }

    /**
     * Debugger helper to show the executed stored procedure info.
     *
     * @param output
     */
    private void getDebugTrace(String output) {
        // Add to this list the name of the function or stored procedure that you want
        // to exclude from the debug print.
        String[] filter = new String[]{};

        if (!Arrays.asList(filter).contains(output.substring(0, output.indexOf("(")))) {
            System.out.println(output);
        }
    }

    /**
     * Generate the parameter list for a stored procedure based on its array of
     * parameters.
     *
     * @param parameters
     * @return
     */
    private String formatSqlParameters(Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return "()";
        }

        StringBuilder sqlParameters = new StringBuilder("(");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                sqlParameters.append(",");
            }
            sqlParameters.append("?");
        }
        sqlParameters.append(")");

        return sqlParameters.toString();
    }

}
