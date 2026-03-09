package com.ingeniusapps.antares.system;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class OSUtils {

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("nux")
                || System.getProperty("os.name").toLowerCase().contains("nix");
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static String getOSName() {
        return System.getProperty("os.name");
    }

    public static void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putString(text);
        clipboard.setContent(content);
    }
}
