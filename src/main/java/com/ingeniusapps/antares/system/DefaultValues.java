package com.ingeniusapps.antares.system;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Conjunto centralizado de valores por defecto utilizados por la librería Antares.
 *
 * <p>Esta clase define constantes que representan valores "nulos", vacíos o
 * predeterminados para distintos tipos de datos. Su propósito es proporcionar
 * una referencia consistente dentro del framework para evitar el uso repetido
 * de valores literales ("magic values") en el código.</p>
 *
 * <p>Estas constantes pueden utilizarse como valores iniciales, marcadores de
 * ausencia de datos o configuraciones predeterminadas en distintos componentes
 * del sistema.</p>
 *
 * <p>La clase contiene además formatos comunes de números y fechas, así como
 * expresiones regulares de uso frecuente.</p>
 *
 * <p><b>Nota:</b> Algunos valores se definen explícitamente como {@code null}
 * para representar la ausencia de datos en contextos donde el tipo del objeto
 * debe mantenerse explícito.</p>
 *
 * <p>Esta clase no está diseñada para ser instanciada.</p>
 */
public final class DefaultValues {

    private DefaultValues() {}

    /**
     * Fecha mínima utilizada como marcador para representar una fecha "nula"
     * o no inicializada.
     */
    public static final LocalDate NULLDATE = LocalDate.MIN;

    /**
     * Fecha y hora mínima utilizada como marcador para representar un
     * {@link LocalDateTime} no inicializado.
     */
    public static final LocalDateTime NULLDATETIME = LocalDateTime.MIN;

    /**
     * Cadena vacía utilizada como valor por defecto para campos de texto.
     */
    public static final String EMPTYSTRING = "";

    /**
     * Valor entero utilizado para representar un timestamp no inicializado.
     */
    public static final int NULLTIMESTAMP = 0;

    /**
     * Valor double por defecto utilizado para representar valores numéricos
     * no definidos o inicializados.
     */
    public static final double NULLDOUBLE = 0.0;

    /**
     * Valor booleano por defecto utilizado cuando no se ha especificado un valor.
     */
    public static final boolean DEFAULTBOOLEAN = false;

    /**
     * Identificador inválido utilizado como marcador para entidades no persistidas
     * o no inicializadas.
     */
    public static final int NULLID = -1;

    /**
     * Representa un {@link BufferedInputStream} nulo.
     * Se utiliza cuando se requiere indicar explícitamente la ausencia de un flujo.
     */
    public static final BufferedInputStream NULLBUFFEREDINPUTSTREAM = null;

    /**
     * Representa un arreglo binario nulo.
     * Se utiliza como marcador para datos binarios inexistentes.
     */
    public static final byte[] NULLBINARY = null;

    /**
     * Representa un {@link InputStream} nulo.
     * Utilizado cuando no se dispone de un flujo de entrada válido.
     */
    public static final InputStream NULLINPUTSTREAM = null;

    /**
     * Valor entero utilizado para representar un entero no inicializado.
     */
    public static final int NULLINTEGER = 0;

    /**
     * Expresión regular utilizada como valor por defecto cuando no se especifica
     * un patrón de validación.
     */
    public static final String NULLREGEX = ".";

    /**
     * Formato numérico utilizado para representar valores enteros.
     * Ejemplo: {@code 1,234}
     */
    public static String INTEGER_FORMAT = "#,##0";

    /**
     * Formato numérico utilizado para representar cantidades monetarias.
     * Ejemplo: {@code 1,234.00}
     */
    public static String AMOUNT_FORMAT = "#,##0.00";

    /**
     * Formato numérico utilizado para representar valores de tipo double.
     */
    public static String DOUBLE_FORMAT = "#,##0.00";

    /**
     * Separador decimal utilizado en representaciones numéricas.
     */
    public static String DECIMAL_SEPARATOR = ".";

    /**
     * Separador de miles utilizado en representaciones numéricas.
     */
    public static String NUMERIC_SEPARATOR = ",";

    /**
     * Formato estándar de fecha y hora utilizado en bases de datos.
     * Ejemplo: {@code yyyy-MM-dd HH:mm:ss}
     */
    public static String DATETIME_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";

    /**
     * Formato estándar de fecha utilizado en bases de datos.
     * Ejemplo: {@code yyyy-MM-dd}
     */
    public static String DATE_FORMAT_DB = "yyyy-MM-dd";

    /**
     * Formato de fecha utilizado comúnmente en interfaces de usuario.
     * Ejemplo: {@code dd/MM/yyyy}
     */
    public static String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Formato de fecha y hora utilizado comúnmente en interfaces de usuario.
     * Ejemplo: {@code dd/MM/yyyy HH:mm:ss}
     */
    public static String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    /**
     * Expresión regular utilizada para validar direcciones de correo electrónico.
     * Compatible con la mayoría de formatos de correo válidos según estándares RFC.
     */
    public static String REGEX_EMAIL =
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                    "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";

}