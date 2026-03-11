package com.ingeniusapps.antares.system;

import static com.ingeniusapps.antares.system.DefaultValues.AMOUNT_FORMAT;
import static com.ingeniusapps.antares.system.DefaultValues.DATETIME_FORMAT_DB;
import static com.ingeniusapps.antares.system.DefaultValues.DATE_FORMAT_DB;
import static com.ingeniusapps.antares.system.DefaultValues.INTEGER_FORMAT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Conjunto de utilidades generales de conversión, formateo, manipulación de
 * fechas, serialización, archivos y procesamiento de texto utilizadas por la
 * librería Antares.
 *
 * <p>Esta clase centraliza funciones auxiliares de uso frecuente para:
 * conversión entre tipos primitivos y cadenas, formato de cantidades y monedas,
 * serialización JSON, transformación de datos binarios, procesamiento de HTML,
 * conversión de fechas para persistencia, operaciones básicas sobre archivos y
 * carga de configuraciones simples.</p>
 *
 * <p>Los métodos de esta clase están diseñados para ser tolerantes a errores
 * de entrada y, en la mayoría de los casos, retornan valores por defecto en
 * lugar de propagar excepciones.</p>
 *
 * <p>Esta clase es utilitaria y no debe ser instanciada.</p>
 */
public final class FunctionBox {

    /**
     * Evita la instanciación accidental de esta clase utilitaria.
     */
    private FunctionBox() {
        throw new AssertionError("This class must not be instantiated.");
    }

    // <editor-fold defaultstate="collapsed" desc="Data type Parsers">

    /**
     * Convierte una cadena a {@code double}.
     *
     * @param value cadena a convertir
     * @param defaultValue valor a retornar si la conversión falla
     * @return valor convertido o {@code defaultValue} si la cadena es nula o inválida
     */
    public static double toDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Convierte una cadena a {@code double}.
     *
     * @param value cadena a convertir
     * @return valor convertido o {@link DefaultValues#NULLDOUBLE} si la conversión falla
     */
    public static double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLDOUBLE;
        }
    }

    /**
     * Convierte una cadena a entero sin signo.
     *
     * @param value cadena a convertir
     * @param defaultValue valor a retornar si la conversión falla; si es negativo se retorna {@code 0}
     * @return entero sin signo convertido o un valor seguro por defecto
     */
    public static int toUnsignedInteger(String value, int defaultValue) {
        try {
            return Integer.parseUnsignedInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return (defaultValue >= 0) ? defaultValue : 0;
        }
    }

    /**
     * Convierte una cadena a entero sin signo.
     *
     * @param value cadena a convertir
     * @return entero convertido o {@link DefaultValues#NULLINTEGER} si la conversión falla
     */
    public static int toUnsignedInteger(String value) {
        try {
            return Integer.parseUnsignedInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLINTEGER;
        }
    }

    /**
     * Convierte una cadena a {@code int}.
     *
     * @param value cadena a convertir
     * @param defaultValue valor a retornar si la conversión falla
     * @return entero convertido o {@code defaultValue}
     */
    public static int toInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Convierte una cadena a {@code int}.
     *
     * @param value cadena a convertir
     * @return entero convertido o {@link DefaultValues#NULLINTEGER} si la conversión falla
     */
    public static int toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLINTEGER;
        }
    }

    /**
     * Convierte un {@code double} a {@code int}.
     *
     * @param value valor a convertir
     * @param defaultValue valor a retornar si la conversión falla
     * @return valor entero convertido o {@code defaultValue}
     */
    public static int toInteger(double value, int defaultValue) {
        try {
            Double mydouble = value;
            return mydouble.intValue();
        } catch (NullPointerException | NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Convierte un {@code double} a {@code int}.
     *
     * @param value valor a convertir
     * @return entero convertido o {@link DefaultValues#NULLINTEGER} si la conversión falla
     */
    public static int toInteger(double value) {
        try {
            Double mydouble = value;
            return mydouble.intValue();
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLINTEGER;
        }
    }

    /**
     * Formatea un valor numérico como cantidad usando {@link DefaultValues#AMOUNT_FORMAT}.
     *
     * @param value valor a formatear
     * @return cadena formateada o cadena vacía si ocurre un error
     */
    public static String toQuantityFormat(double value) {
        try {
            return new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Convierte una cadena a cantidad y la formatea usando {@link DefaultValues#AMOUNT_FORMAT}.
     *
     * @param value cadena con valor numérico
     * @return cadena formateada o cadena vacía si ocurre un error
     */
    public static String toQuantityFormat(String value) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un monto numérico usando {@link DefaultValues#AMOUNT_FORMAT}.
     *
     * @param value monto a formatear
     * @return cadena formateada o cadena vacía si ocurre un error
     */
    public static String toAmountFormat(double value) {
        try {
            return new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Convierte una cadena a monto y la formatea usando {@link DefaultValues#AMOUNT_FORMAT}.
     *
     * @param value cadena con valor numérico
     * @return cadena formateada o cadena vacía si ocurre un error
     */
    public static String toAmountFormat(String value) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un entero usando {@link DefaultValues#INTEGER_FORMAT}.
     *
     * @param value entero a formatear
     * @return cadena formateada o cadena vacía si ocurre un error
     */
    public static String toIntegerFormat(int value) {
        try {
            return new DecimalFormat(INTEGER_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Convierte una cadena a entero y la formatea usando {@link DefaultValues#INTEGER_FORMAT}.
     *
     * @param value cadena con valor entero
     * @return cadena formateada o cadena vacía si ocurre un error
     */
    public static String toIntegerFormat(String value) {
        try {
            int dvalue = FunctionBox.toInteger(value, 0);
            return new DecimalFormat(INTEGER_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un monto monetario en HTML agregando el símbolo de moneda indicado.
     *
     * @param value monto a formatear
     * @param CURRENCY_SYMBOL_HTML símbolo o representación HTML de la moneda
     * @return monto formateado con símbolo o cadena vacía si ocurre un error
     */
    public static String toCurrencyFormatHTML(double value, String CURRENCY_SYMBOL_HTML) {
        try {
            return CURRENCY_SYMBOL_HTML + " " + new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Convierte una cadena a monto monetario en HTML agregando el símbolo de moneda indicado.
     *
     * @param value cadena con valor numérico
     * @param CURRENCY_SYMBOL_HTML símbolo o representación HTML de la moneda
     * @return monto formateado con símbolo o cadena vacía si ocurre un error
     */
    public static String toCurrencyFormatHTML(String value, String CURRENCY_SYMBOL_HTML) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return CURRENCY_SYMBOL_HTML + " " + new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un monto monetario agregando el símbolo de moneda indicado.
     *
     * @param value monto a formatear
     * @param CURRENCY_SYMBOL símbolo de moneda
     * @return monto formateado con símbolo o cadena vacía si ocurre un error
     */
    public static String toCurrencyFormat(double value, String CURRENCY_SYMBOL) {
        try {
            return CURRENCY_SYMBOL + " " + new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Convierte una cadena a monto monetario agregando el símbolo de moneda indicado.
     *
     * @param value cadena con valor numérico
     * @param CURRENCY_SYMBOL símbolo de moneda
     * @return monto formateado con símbolo o cadena vacía si ocurre un error
     */
    public static String toCurrencyFormat(String value, String CURRENCY_SYMBOL) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return CURRENCY_SYMBOL + " " + new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un número de tarjeta de crédito.
     *
     * <p>Cuando {@code hidden} es {@code true}, el método retorna solamente
     * los últimos cuatro dígitos precedidos por asteriscos. Cuando es
     * {@code false}, intenta retornar una representación segmentada con guiones.</p>
     *
     * @param value número de tarjeta sin formato
     * @param hidden indica si el número debe ocultarse parcialmente
     * @return tarjeta formateada o cadena vacía si ocurre un error durante el formateo
     * @throws Exception si el número es nulo o su longitud no está entre 15 y 16 caracteres
     */
    public static String toCreditCardFormat(String value, boolean hidden) throws Exception {
        if (value == null || value.length() > 16 || value.length() < 15) {
            throw new Exception("Invalid credit card number");
        }

        try {
            if (hidden) {
                return "****-" + value.substring(value.length() - 4);
            } else {
                if (value.length() == 16) {
                    return value.substring(0, 4) + "-" + value.substring(4, 4) + "-" + value.substring(8, 4) + "-" + value.substring(12, 4);
                } else {
                    return value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(10, 5);
                }
            }
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Serializa un objeto a JSON utilizando una configuración estándar de Gson.
     *
     * @param <T> tipo del modelo
     * @param model objeto a serializar
     * @return representación JSON del objeto o {@code "{}"} si ocurre un error
     */
    public static <T> String toJSON(T model) {
        try {
            Gson jsonHandler = new Gson();
            return jsonHandler.toJson(model);
        } catch (Exception ex) {
            return "{}";
        }
    }

    /**
     * Serializa un objeto a JSON registrando un adaptador personalizado en Gson.
     *
     * @param <T> tipo del modelo
     * @param model objeto a serializar
     * @param gSonTypeAdapter adaptador de tipo a registrar para la clase del modelo
     * @return representación JSON del objeto o {@code "{}"} si ocurre un error
     */
    public static <T> String toJSON(T model, Object gSonTypeAdapter) {
        try {
            GsonBuilder jsonHandlerBuilder = new GsonBuilder();
            jsonHandlerBuilder.registerTypeAdapter(model.getClass(), gSonTypeAdapter);
            Gson jsonHandler = jsonHandlerBuilder.create();

            return jsonHandler.toJson(model);
        } catch (Exception ex) {
            return "{}";
        }
    }

    /**
     * Deserializa una cadena JSON a una instancia del tipo indicado.
     *
     * @param <T> tipo de retorno esperado
     * @param JSON cadena JSON a deserializar
     * @param classType clase de destino
     * @return instancia deserializada o {@code null} si el JSON es inválido
     */
    public static <T> T toModel(String JSON, Class<T> classType) {
        try {
            Gson jsonHandler = new Gson();
            return jsonHandler.fromJson(JSON, classType);
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }

    /**
     * Convierte un {@link BufferedInputStream} a arreglo de bytes.
     *
     * @param bufferedInput flujo de entrada a convertir
     * @return arreglo de bytes leído desde el flujo o {@code null} si no hay datos o ocurre un error
     */
    public static byte[] toByteArray(BufferedInputStream bufferedInput) {
        byte[] binaryarray = null;

        try {
            if (bufferedInput != null) {
                binaryarray = bufferedInput.readAllBytes();
            }
        } catch (IOException ex) {
            binaryarray = null;
        }

        return binaryarray;
    }

    /**
     * Convierte un arreglo de bytes en un {@link BufferedInputStream}.
     *
     * @param binaryImage arreglo binario de entrada
     * @return flujo de entrada bufferizado o {@code null} si ocurre un error
     */
    public static BufferedInputStream toBufferedInputStream(byte[] binaryImage) {
        try {
            InputStream iStream = new ByteArrayInputStream(binaryImage);

            return new BufferedInputStream(iStream);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Convierte contenido HTML a texto plano preservando saltos de línea básicos
     * para etiquetas como {@code <br>} y {@code <p>}.
     *
     * @param html contenido HTML
     * @return texto plano normalizado; retorna cadena vacía si la entrada es nula o vacía
     */
    public static String htmlToText(String html) {
        if (html == null || html.isBlank()) {
            return "";
        }

        Document doc = Jsoup.parse(html);
        doc.outputSettings().prettyPrint(false);
        doc.select("br").append("\\n");
        doc.select("p").prepend("\\n");

        String text = doc.text();

        text = text.replace("\\n", "\n");

        text = text
                .replaceAll("[ \\t\\x0B\\f\\r]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();

        return text;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Database Parsers">

    /**
     * Convierte una fecha y hora en formato humano al formato de base de datos.
     *
     * @param datetime fecha y hora de entrada
     * @param humanformat patrón del formato humano esperado
     * @return fecha y hora en formato de base de datos o {@link LocalDateTime#MIN} formateado si falla la conversión
     */
    public static String toDBDateTime(String datetime, String humanformat) {
        try {
            LocalDateTime parse = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(humanformat));
            return parse.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_DB));
        } catch (Exception ex) {
            return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_DB));
        }
    }

    /**
     * Convierte una fecha y hora en formato de base de datos a formato humano.
     *
     * @param dbdate fecha y hora en formato de base de datos
     * @param humanformat patrón de salida deseado
     * @return fecha y hora en formato humano o {@link LocalDateTime#MIN} formateado si falla la conversión
     */
    public static String toHumanDateTime(String dbdate, String humanformat) {
        try {
            LocalDateTime parse = LocalDateTime.parse(dbdate, DateTimeFormatter.ofPattern(DATETIME_FORMAT_DB));
            return parse.format(DateTimeFormatter.ofPattern(humanformat));
        } catch (Exception ex) {
            return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern(humanformat));
        }
    }

    /**
     * Convierte una fecha en formato humano al formato de base de datos.
     *
     * @param date fecha de entrada
     * @param humanformat patrón del formato humano esperado
     * @return fecha en formato de base de datos o {@link LocalDate#MIN} formateado si falla la conversión
     */
    public static String toDBDate(String date, String humanformat) {
        try {
            LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(humanformat));
            return parse.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DB));
        } catch (Exception ex) {
            return LocalDate.MIN.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DB));
        }
    }

    /**
     * Convierte una fecha en formato de base de datos a formato humano.
     *
     * @param dbdate fecha en formato de base de datos
     * @param humanformat patrón de salida deseado
     * @return fecha en formato humano o {@link LocalDate#MIN} formateado si falla la conversión
     */
    public static String toHumanDate(String dbdate, String humanformat) {
        try {
            LocalDate parse = LocalDate.parse(dbdate, DateTimeFormatter.ofPattern(DATE_FORMAT_DB));
            return parse.format(DateTimeFormatter.ofPattern(humanformat));
        } catch (Exception ex) {
            return LocalDate.MIN.format(DateTimeFormatter.ofPattern(humanformat));
        }
    }

    /**
     * Obtiene la parte de fecha de una cadena con formato fecha y hora separadas por espacio.
     *
     * @param date cadena fecha-hora
     * @return parte de fecha o {@link LocalDate#MIN} si falla la operación
     */
    public static String getDatePart(String date) {
        try {
            return date.substring(0, date.indexOf(" "));
        } catch (Exception ex) {
            return LocalDate.MIN.toString();
        }
    }

    /**
     * Obtiene la parte de hora de una cadena con formato fecha y hora separadas por espacio.
     *
     * @param date cadena fecha-hora
     * @return parte de hora o {@link LocalTime#MIN} si falla la operación
     */
    public static String getTimePart(String date) {
        try {
            return date.substring(date.indexOf(" ") + 1);
        } catch (Exception ex) {
            return LocalTime.MIN.toString();
        }
    }

    /**
     * Elimina la parte decimal de milisegundos de una cadena de tiempo si existe.
     *
     * @param time cadena de tiempo
     * @return cadena sin milisegundos o la misma entrada si ocurre un error
     */
    public static String removeMilliseconds(String time) {
        try {
            return (time.contains(".")) ? time.substring(0, time.indexOf(".")) : time;
        } catch (Exception ex) {
            return time;
        }
    }

    /**
     * Agrega una fracción de milisegundos por defecto a una cadena de tiempo si no existe.
     *
     * @param time cadena de tiempo
     * @return cadena con milisegundos o la misma entrada si ocurre un error
     */
    public static String addMilliseconds(String time) {
        try {
            return (!time.contains(".")) ? time + ".0" : time;
        } catch (Exception ex) {
            return time;
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other">

    /**
     * Obtiene la fecha local actual del sistema.
     *
     * @return fecha local actual
     */
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }

    /**
     * Repite un carácter un número determinado de veces.
     *
     * @param what carácter a repetir
     * @param howmany cantidad de repeticiones
     * @return cadena resultante con el carácter repetido
     */
    public static String repeat(char what, int howmany) {
        char[] chars = new char[howmany];
        Arrays.fill(chars, what);
        return new String(chars);
    }

    /**
     * Obtiene la fecha y hora local actual del sistema.
     *
     * @return fecha y hora local actual
     */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Trunca un valor decimal a una cantidad específica de posiciones decimales.
     *
     * @param value valor a truncar
     * @param places cantidad de posiciones decimales
     * @return valor truncado
     */
    public static double truncateDecimals(double value, int places) {
        String svalue = String.valueOf(value);

        if (!svalue.contains(".")) {
            svalue = svalue + repeat('0', places);
        } else {
            int realplaces = svalue.substring(svalue.indexOf(".") + 1).length();
            if (realplaces >= places) {
                realplaces = places;
            } else if (realplaces < places) {
                places = places - realplaces;
            }

            svalue = svalue.substring(0, svalue.indexOf(".")) + "." + svalue.substring(svalue.indexOf(".") + 1, svalue.indexOf(".") + 1 + realplaces) + repeat('0', places);
        }

        return Double.parseDouble(svalue);
    }

    /**
     * Redondea un valor decimal al número indicado de posiciones usando
     * {@link RoundingMode#HALF_UP} y retornando el resultado con truncado controlado.
     *
     * @param value valor a redondear
     * @param decimals cantidad de posiciones decimales deseadas
     * @return valor redondeado
     */
    public static double legalAmountDecimals(double value, int decimals) {
        String decValue = String.valueOf(value);

        if (decimals >= decValue.substring(decValue.indexOf(".") + 1).length()) {
            return value;
        } else {
            BigDecimal calc_a = BigDecimal.valueOf(truncateDecimals(value, decimals + 1));
            BigDecimal calc_r = calc_a.setScale(decimals, RoundingMode.HALF_UP);

            return truncateDecimals(calc_r.doubleValue(), decimals);
        }
    }

    /**
     * Descarga un archivo desde una URL y lo almacena en una ruta local.
     *
     * @param fromURL dirección origen del archivo
     * @param toLocalDestiny ruta local de destino
     * @return {@code true} si la descarga se completó correctamente; en caso contrario {@code false}
     */
    public static boolean downloadFile(String fromURL, String toLocalDestiny) {
        try {
            URI uri = new URI(fromURL);
            URL url = uri.toURL();
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(toLocalDestiny);

            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (MalformedURLException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        } catch (IOException | URISyntaxException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

        return true;
    }

    /**
     * Elimina un archivo del almacenamiento local.
     *
     * @param fileName nombre o ruta del archivo a eliminar
     * @return {@code true} si el archivo fue eliminado; de lo contrario {@code false}
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);

        return file.delete();
    }

    /**
     * Renombra o mueve un archivo local.
     *
     * @param fileNameOld nombre o ruta actual del archivo
     * @param fileNameNew nuevo nombre o nueva ruta del archivo
     * @return {@code true} si la operación se completó correctamente; de lo contrario {@code false}
     */
    public static boolean renameFile(String fileNameOld, String fileNameNew) {
        File fileOld = new File(fileNameOld);
        File fileNew = new File(fileNameNew);

        return fileOld.renameTo(fileNew);
    }

    /**
     * Normaliza los separadores de ruta usando el separador propio del sistema operativo actual.
     *
     * @param pathToOS ruta a normalizar
     * @return ruta con separadores adaptados al sistema operativo
     */
    public static String fileSeparatorOS(String pathToOS) {
        String pathSeparator = System.getProperty("file.separator");

        return pathToOS.replaceAll("/", (pathSeparator.compareTo("\\") == 0) ? "\\\\" : "/");
    }

    /**
     * Extrae un archivo específico desde un archivo ZIP, con soporte opcional para contraseña.
     *
     * @param zipFileName ruta del archivo ZIP
     * @param fileName nombre del archivo a extraer desde el ZIP
     * @param destinyPath ruta de destino donde se extraerá el archivo
     * @param password contraseña del ZIP; puede ser {@code null} o vacía si no aplica
     * @return {@code true} si la extracción se completó correctamente; de lo contrario {@code false}
     */
    public static boolean unZipFile(String zipFileName, String fileName, String destinyPath, String password) {
        try {
            ZipFile zipFile = (password != null && password.length() > 0) ? new ZipFile(zipFileName, password.toCharArray()) : new ZipFile(zipFileName);
            zipFile.extractFile(fileName, destinyPath);
            zipFile.close();
        } catch (ZipException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    /**
     * Escribe una línea de mensaje en un archivo de log diario.
     *
     * <p>El nombre del archivo se construye con la fecha actual en formato
     * {@code yyyy-MM-dd.log}, y cada línea incluye una marca de tiempo con milisegundos.</p>
     *
     * @param logFilePath directorio donde se almacenará el archivo de log
     * @param message mensaje a registrar
     */
    public static void writeLogFile(String logFilePath, String message) {
        FileWriter fileOutput = null;

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String logFileName = dtf.format(now) + ".log";

            DateTimeFormatter dtfrow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
            String logTimeMark = dtfrow.format(now);

            fileOutput = new FileWriter(logFilePath + "/" + logFileName, true);

            fileOutput.write("[" + logTimeMark + "] - " + message + "\n");

            fileOutput.close();
        } catch (IOException ex) {
        } finally {
            try {
                if (fileOutput != null) {
                    fileOutput.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Carga un archivo de configuración simple basado en pares clave-valor separados por {@code =}.
     *
     * <p>Las líneas vacías son ignoradas. Cada línea válida se divide únicamente
     * en el primer carácter {@code =}, permitiendo valores que también contengan dicho carácter.</p>
     *
     * @param filename ruta del archivo de configuración
     * @return mapa con las claves y valores cargados; si ocurre un error se retorna un mapa vacío
     */
    public static Map<String, String> loadConfigFile(String filename) {
        Map<String, String> configMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && line.contains("=")) {
                    String[] keyValue = line.split("=", 2);
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    configMap.put(key, value);
                }
            }
        } catch (IOException e) {
        }

        return configMap;
    }
    // </editor-fold>
}