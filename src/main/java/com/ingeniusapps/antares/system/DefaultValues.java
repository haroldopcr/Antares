package com.ingeniusapps.antares.system;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DefaultValues {

    // Default Values.
    // ==============================================================================================================
    public static final LocalDate NULLDATE = LocalDate.MIN;
    public static final LocalDateTime NULLDATETIME = LocalDateTime.MIN;
    public static final String EMPTYSTRING = "";
    public static final int NULLTIMESTAMP = 0;
    public static final double NULLDOUBLE = 0.0;
    public static final boolean DEFAULTBOOLEAN = false;
    public static final int NULLID = -1;
    public static final BufferedInputStream NULLBUFFEREDINPUTSTREAM = null;
    public static final byte[] NULLBINARY = null;
    public static final InputStream NULLINPUTSTREAM = null;
    public static final int NULLINTEGER = 0;
    public static final String NULLREGEX = ".";

    // Double (amount) Format.
    // ==============================================================================================================
    public static String INTEGER_FORMAT = "#,##0";
    public static String AMOUNT_FORMAT = "#,##0.00";
    public static String DOUBLE_FORMAT = "#,##0.00";
    public static String DECIMAL_SEPARATOR = ".";
    public static String NUMERIC_SEPARATOR = ",";

    // DateTime Formats.
    // ==============================================================================================================
    public static String DATETIME_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_DB = "yyyy-MM-dd";
    public static String DATE_FORMAT = "dd/MM/yyyy";
    public static String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    // Regular Expressions.
    // ==============================================================================================================
    public static String REGEX_EMAIL = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
}