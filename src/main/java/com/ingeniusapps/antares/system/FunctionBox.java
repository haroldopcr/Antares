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
 * @author Harold Ortega Pérez.
 * @version 2.0
 *
 */
public class FunctionBox {

    // <editor-fold defaultstate="collapsed" desc="Data type Parsers">
    public static double toDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLDOUBLE;
        }
    }

    public static int toUnsignedInteger(String value, int defaultValue) {
        try {
            return Integer.parseUnsignedInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return (defaultValue >= 0) ? defaultValue : 0;
        }
    }

    public static int toUnsignedInteger(String value) {
        try {
            return Integer.parseUnsignedInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLINTEGER;
        }
    }

    public static int toInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLINTEGER;
        }
    }

    public static int toInteger(double value, int defaultValue) {
        try {
            Double mydouble = value;
            return mydouble.intValue();
        } catch (NullPointerException | NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int toInteger(double value) {
        try {
            Double mydouble = value;
            return mydouble.intValue();
        } catch (NullPointerException | NumberFormatException e) {
            return DefaultValues.NULLINTEGER;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------------
    // Quantity and amount parser.
    public static String toQuantityFormat(double value) {
        try {
            return new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toQuantityFormat(String value) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toAmountFormat(double value) {
        try {
            return new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toAmountFormat(String value) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toIntegerFormat(int value) {
        try {
            return new DecimalFormat(INTEGER_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toIntegerFormat(String value) {
        try {
            int dvalue = FunctionBox.toInteger(value, 0);
            return new DecimalFormat(INTEGER_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------------
    // Currency parser.
    public static String toCurrencyFormatHTML(double value, String CURRENCY_SYMBOL_HTML) {
        try {
            return CURRENCY_SYMBOL_HTML + " " + new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toCurrencyFormatHTML(String value, String CURRENCY_SYMBOL_HTML) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return CURRENCY_SYMBOL_HTML + " " + new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toCurrencyFormat(double value, String CURRENCY_SYMBOL) {
        try {
            return CURRENCY_SYMBOL + " " + new DecimalFormat(AMOUNT_FORMAT).format(value);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String toCurrencyFormat(String value, String CURRENCY_SYMBOL) {
        try {
            double dvalue = FunctionBox.toDouble(value, 0);
            return CURRENCY_SYMBOL + " " + new DecimalFormat(AMOUNT_FORMAT).format(dvalue);
        } catch (Exception ex) {
            return "";
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------------
    // Credit Card parser.
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

    // -----------------------------------------------------------------------------------------------------------------------------------
    // JSON parser.
    public static <T> String toJSON(T model) {
        try {
            Gson jsonHandler = new Gson();
            return jsonHandler.toJson(model);
        } catch (Exception ex) {
            return "{}";
        }
    }

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

    public static <T> T toModel(String JSON, Class<T> classType) {
        try {
            Gson jsonHandler = new Gson();
            return jsonHandler.fromJson(JSON, classType);
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------------
    // Image parser.
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

    public static BufferedInputStream toBufferedInputStream(byte[] binaryImage) {
        try {
            InputStream iStream = new ByteArrayInputStream(binaryImage);

            return new BufferedInputStream(iStream);
        } catch (Exception ex) {
            return null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------------
    // HTML parser.
    public static String htmlToText(String html) {
        if (html == null || html.isBlank()) {
            return "";
        }

        // Parsear el HTML
        Document doc = Jsoup.parse(html);

        // Opcional: evitar que Jsoup "reformatee" demasiado el HTML
        doc.outputSettings().prettyPrint(false);

        // Truco: marcamos <br> y <p> con "\n" para que se conviertan en saltos de línea
        doc.select("br").append("\\n");
        doc.select("p").prepend("\\n");

        // Obtener el texto plano
        String text = doc.text();

        // Reemplazar las marcas "\n" por saltos de línea reales
        text = text.replace("\\n", "\n");

        // Limpieza adicional:
        // - Normalizar espacios múltiples
        // - Quitar espacios al inicio/fin de líneas
        text = text
                // Sustituye múltiples espacios por uno solo
                .replaceAll("[ \\t\\x0B\\f\\r]+", " ")
                // Cambia secuencias de 3+ saltos de línea por solo dos
                .replaceAll("\\n{3,}", "\n\n")
                .trim();

        return text;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Database Parsers">
    public static String toDBDateTime(String datetime, String humanformat) {
        try {
            LocalDateTime parse = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(humanformat));
            return parse.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_DB));
        } catch (Exception ex) {
            return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_DB));
        }
    }

    public static String toHumanDateTime(String dbdate, String humanformat) {
        try {
            LocalDateTime parse = LocalDateTime.parse(dbdate, DateTimeFormatter.ofPattern(DATETIME_FORMAT_DB));
            return parse.format(DateTimeFormatter.ofPattern(humanformat));
        } catch (Exception ex) {
            return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern(humanformat));
        }
    }

    public static String toDBDate(String date, String humanformat) {
        try {
            LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(humanformat));
            return parse.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DB));
        } catch (Exception ex) {
            return LocalDate.MIN.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DB));
        }
    }

    public static String toHumanDate(String dbdate, String humanformat) {
        try {
            LocalDate parse = LocalDate.parse(dbdate, DateTimeFormatter.ofPattern(DATE_FORMAT_DB));
            return parse.format(DateTimeFormatter.ofPattern(humanformat));
        } catch (Exception ex) {
            return LocalDate.MIN.format(DateTimeFormatter.ofPattern(humanformat));
        }
    }

    public static String getDatePart(String date) {
        try {
            return date.substring(0, date.indexOf(" "));
        } catch (Exception ex) {
            return LocalDate.MIN.toString();
        }
    }

    public static String getTimePart(String date) {
        try {
            return date.substring(date.indexOf(" ") + 1);
        } catch (Exception ex) {
            return LocalTime.MIN.toString();
        }
    }

    public static String removeMilliseconds(String time) {
        try {
            return (time.contains(".")) ? time.substring(0, time.indexOf(".")) : time;
        } catch (Exception ex) {
            return time;
        }
    }

    public static String addMilliseconds(String time) {
        try {
            return (!time.contains(".")) ? time + ".0" : time;
        } catch (Exception ex) {
            return time;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other">   
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }

    public static String repeat(char what, int howmany) {
        char[] chars = new char[howmany];
        Arrays.fill(chars, what);
        return new String(chars);
    }

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Truncates a double value.
     *
     * @param value Value to truncate.
     * @param places Number of decimal places.
     * @return Truncated value in double format.
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
     * Round to the legal amount format.
     *
     * @param value Value to round.
     * @param decimals Number of decimal places.
     * @return Rounded value to the specified decimals.
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
     * Downloads a file from the fromURL parameter and stores it into the
     * specified file by toLocalDestiny.
     *
     * @param fromURL Source address of the file.
     * @param toLocalDestiny Destiny to store the file.
     * @return true if the download process was succesfuly completed, otherwise
     * false is returned.
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
     * Deletes a file from the local storage.
     *
     * @param fileName File name to delete (it might include the full path).
     * @return true if the delete process was succesfuly completed, otherwise
     * false is returned.
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);

        return file.delete();
    }

    public static boolean renameFile(String fileNameOld, String fileNameNew) {
        File fileOld = new File(fileNameOld);
        File fileNew = new File(fileNameNew);

        return fileOld.renameTo(fileNew);
    }

    public static String fileSeparatorOS(String pathToOS) {
        String pathSeparator = System.getProperty("file.separator");

        return pathToOS.replaceAll("/", (pathSeparator.compareTo("\\") == 0) ? "\\\\" : "/");
    }

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
