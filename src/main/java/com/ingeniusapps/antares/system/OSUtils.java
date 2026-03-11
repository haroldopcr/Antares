package com.ingeniusapps.antares.system;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * Utilidades relacionadas con el sistema operativo y funciones básicas de
 * interoperabilidad con el entorno de ejecución.
 *
 * <p>Esta clase ofrece métodos auxiliares para identificar el sistema operativo
 * actual y para copiar texto al portapapeles del sistema mediante JavaFX.</p>
 *
 * <p>Está diseñada como una clase utilitaria, por lo que no debe ser instanciada.</p>
 */
public final class OSUtils {

    /**
     * Evita la instanciación accidental de esta clase utilitaria.
     */
    private OSUtils() {
        throw new AssertionError("This class must not be instantiated.");
    }

    /**
     * Determina si el sistema operativo actual es Windows.
     *
     * @return {@code true} si el nombre del sistema operativo contiene
     *         {@code "win"}; en caso contrario, {@code false}
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Determina si el sistema operativo actual es Linux o una variante Unix-like
     * identificada por las cadenas {@code "nux"} o {@code "nix"}.
     *
     * @return {@code true} si el nombre del sistema operativo contiene
     *         {@code "nux"} o {@code "nix"}; en caso contrario, {@code false}
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("nux")
                || System.getProperty("os.name").toLowerCase().contains("nix");
    }

    /**
     * Determina si el sistema operativo actual es macOS.
     *
     * @return {@code true} si el nombre del sistema operativo contiene
     *         {@code "mac"}; en caso contrario, {@code false}
     */
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    /**
     * Obtiene el nombre del sistema operativo reportado por la JVM.
     *
     * @return nombre del sistema operativo según la propiedad del sistema
     *         {@code os.name}
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    /**
     * Copia una cadena de texto al portapapeles del sistema utilizando
     * las APIs de JavaFX.
     *
     * @param text texto que será copiado al portapapeles
     */
    public static void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putString(text);
        clipboard.setContent(content);
    }
}