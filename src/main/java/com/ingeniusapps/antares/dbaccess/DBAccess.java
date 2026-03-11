package com.ingeniusapps.antares.dbaccess;

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

/**
 * Proporciona una capa de acceso a base de datos con soporte para conexiones,
 * control transaccional, ejecución de consultas parametrizadas y llamadas a
 * procedimientos almacenados.
 *
 * <p>Esta clase encapsula la inicialización del driver JDBC, la apertura y
 * cierre de conexiones, y la ejecución de operaciones contra motores
 * soportados por {@link DBDriverType}. Los resultados de las consultas y
 * procedimientos almacenados se materializan en estructuras propias de la
 * librería como {@link DBResultSet}, {@link DBRecordSet} y {@link DBRecord}.</p>
 *
 * <p>La clase mantiene además una traza del último resultado de operación
 * mediante {@link #getLastOperationResult()}, lo que permite consultar el
 * último mensaje de error o el estado {@code "OK"} tras una operación
 * satisfactoria.</p>
 */
public class DBAccess {

    /**
     * Indica si deben imprimirse en consola las operaciones SQL ejecutadas.
     */
    private boolean executeTracker;

    /**
     * Indica si la conexión debe utilizar SSL.
     *
     * <p>Actualmente el soporte indicado por esta bandera no se encuentra
     * implementado completamente para todos los motores.</p>
     */
    private boolean useSSL;

    /**
     * Tipo de driver de base de datos utilizado por esta instancia.
     */
    private DBDriverType dbDriverType;

    /**
     * Resultado textual de la última operación ejecutada.
     *
     * <p>Por convención, el valor {@code "OK"} indica una operación exitosa.</p>
     */
    private String lastOperationResult = "OK";

    /**
     * Conexión JDBC actualmente asociada a la instancia.
     */
    private Connection connection = null;

    /**
     * Obtiene el resultado textual de la última operación ejecutada.
     *
     * @return mensaje de resultado de la última operación
     */
    public String getLastOperationResult() {
        return lastOperationResult;
    }

    /**
     * Obtiene la conexión JDBC actual.
     *
     * @return conexión activa o {@code null} si no existe una conexión asociada
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Crea una nueva instancia de acceso a base de datos.
     *
     * @param dbDriverType tipo de driver de base de datos a utilizar
     * @param useSSL indica si la conexión utilizará SSL
     */
    public DBAccess(DBDriverType dbDriverType, boolean useSSL) {
        this.newAccess(dbDriverType, useSSL, false);
    }

    /**
     * Crea una nueva instancia de acceso a base de datos con control opcional
     * de trazas de ejecución.
     *
     * @param dbDriverType tipo de driver de base de datos a utilizar
     * @param useSSL indica si la conexión utilizará SSL
     * @param executeTracker indica si deben imprimirse en consola las operaciones ejecutadas
     */
    public DBAccess(DBDriverType dbDriverType, boolean useSSL, boolean executeTracker) {
        this.newAccess(dbDriverType, useSSL, executeTracker);
    }

    /**
     * Inicializa internamente la instancia resolviendo y cargando el driver JDBC
     * correspondiente al motor indicado.
     *
     * @param dbDriverType tipo de driver de base de datos
     * @param useSSL indica si la conexión utilizará SSL
     * @param executeTracker indica si debe activarse el rastreo de ejecución
     */
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
     * Establece una conexión con la base de datos configurada.
     *
     * <p>La URL JDBC se construye internamente según el tipo de driver definido
     * para esta instancia. La conexión se configura con aislamiento de
     * transacción {@link Connection#TRANSACTION_READ_COMMITTED} y con
     * {@code autoCommit} desactivado.</p>
     *
     * @param host host o ruta base de la base de datos
     * @param database nombre de la base de datos
     * @param user nombre de usuario
     * @param password contraseña de acceso
     * @return {@code true} si la conexión fue establecida correctamente;
     *         en caso contrario, {@code false}
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
     * Cierra la conexión actual con la base de datos.
     *
     * @return {@code true} si la desconexión se completó correctamente;
     *         en caso contrario, {@code false}
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
     * Confirma la transacción activa sobre la conexión actual.
     *
     * @return {@code true} si la confirmación se realizó correctamente;
     *         en caso contrario, {@code false}
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
     * Revierte la transacción activa sobre la conexión actual.
     *
     * @return {@code true} si la reversión se realizó correctamente;
     *         en caso contrario, {@code false}
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
     * Inicia explícitamente una transacción sobre la conexión actual.
     *
     * @return {@code true} si la instrucción fue ejecutada correctamente;
     *         en caso contrario, {@code false}
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
     * Ejecuta un procedimiento almacenado con parámetros y materializa todos
     * sus conjuntos de resultados en un {@link DBResultSet}.
     *
     * <p>Los parámetros son enlazados dinámicamente según su tipo en tiempo de
     * ejecución. Si la opción de rastreo se encuentra activa, el nombre de la
     * operación es enviado a la salida estándar para depuración.</p>
     *
     * @param storedProcedure nombre del procedimiento almacenado
     * @param parameters parámetros a suministrar en la llamada
     * @return estructura con los resultados del procedimiento, o {@code null}
     *         si ocurre un error SQL
     */
    public DBResultSet callStoredProcedure(String storedProcedure, DBParameters parameters) {
        try {
            Object[] dbParams = parameters.getParameters();

            DBResultSet dbResultSet = new DBResultSet();

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

            if (this.executeTracker) {
                this.getDebugTrace(storedProcedure);
            }
            boolean hasResults = callableStatement.execute();

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
     * Ejecuta una consulta SQL parametrizada y materializa todos sus conjuntos
     * de resultados en un {@link DBResultSet}.
     *
     * <p>Los parámetros son enlazados dinámicamente según su tipo en tiempo de
     * ejecución. Si la opción de rastreo se encuentra activa, la consulta es
     * enviada a la salida estándar para depuración.</p>
     *
     * @param query consulta SQL a ejecutar
     * @param parameters parámetros a enlazar en la consulta
     * @return estructura con los resultados de la consulta, o {@code null}
     *         si ocurre un error SQL
     */
    public DBResultSet executeQuery(String query, DBParameters parameters) {
        try {
            Object[] dbParams = parameters.getParameters();

            DBResultSet dbResultSet = new DBResultSet();

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

            if (this.executeTracker) {
                this.getDebugTrace(query);
            }
            boolean hasResults = preparedStatement.execute();

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
     * Imprime una traza de depuración de la operación ejecutada, salvo que su
     * nombre se encuentre listado dentro del filtro interno.
     *
     * @param output texto representativo de la operación ejecutada
     */
    private void getDebugTrace(String output) {
        String[] filter = new String[]{};

        if (!Arrays.asList(filter).contains(output.substring(0, output.indexOf("(")))) {
            System.out.println(output);
        }
    }

    /**
     * Genera una lista SQL de marcadores de parámetro con base en la cantidad
     * de elementos del arreglo recibido.
     *
     * <p>Por ejemplo, para tres parámetros se genera la cadena
     * {@code "(?,?,?)"}.</p>
     *
     * @param parameters arreglo de parámetros
     * @return representación textual de la lista SQL de parámetros
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