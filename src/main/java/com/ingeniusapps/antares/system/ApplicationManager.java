package com.ingeniusapps.antares.system;

import com.ingeniusapps.antares.javafx.FXUIAnimations;
import com.ingeniusapps.antares.javafx.FXUIManager;
import com.ingeniusapps.antares.javafx.FXUISound;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Administrador central de utilidades globales de aplicación para entornos JavaFX.
 *
 * <p>Esta clase expone instancias compartidas de componentes de infraestructura
 * visual de Antares, así como utilidades para mostrar ventanas y finalizar la
 * aplicación de forma controlada.</p>
 *
 * <p>Además, proporciona banderas de conveniencia para identificar el sistema
 * operativo actual, lo cual facilita decisiones condicionales de comportamiento
 * en tiempo de ejecución.</p>
 */
public class ApplicationManager {

    /**
     * Instancia global del administrador principal de interfaz de usuario.
     */
    public static final FXUIManager fxUIManager = new FXUIManager();

    /**
     * Instancia global del administrador de animaciones JavaFX.
     */
    public static final FXUIAnimations fxAnimations = new FXUIAnimations();

    /**
     * Instancia global del administrador de sonido para la interfaz.
     */
    public static final FXUISound fxSoundManager = new FXUISound();

    /**
     * Indica si la aplicación se está ejecutando sobre Linux.
     */
    public static final boolean IS_LINUX = OSUtils.isLinux();

    /**
     * Indica si la aplicación se está ejecutando sobre Windows.
     */
    public static final boolean IS_WINDOWS = OSUtils.isWindows();

    /**
     * Indica si la aplicación se está ejecutando sobre macOS.
     */
    public static final boolean IS_MAC = OSUtils.isMac();

    /**
     * Muestra una ventana JavaFX utilizando la configuración definida en un
     * {@link StageControlBlock}.
     *
     * <p>Este método aplica al {@link Stage} los parámetros incluidos en el bloque
     * de control, tales como estilo, modalidad, propietario, dimensiones, título,
     * icono y comportamiento visual. Finalmente, muestra la ventana usando
     * {@code show()} o {@code showAndWait()} según la configuración indicada.</p>
     *
     * <p>Si el bloque es inválido o se produce un error durante la configuración
     * o visualización de la ventana, el método retorna {@code false}.</p>
     *
     * @param scb bloque de configuración de la ventana a mostrar
     * @return {@code true} si la ventana fue configurada y mostrada correctamente;
     *         de lo contrario, {@code false}
     */
    public static boolean showStage(StageControlBlock scb) {
        try {
            if (scb == null || scb.stage == null || scb.fxController == null) {
                return false;
            }

            Stage stageToWork = (scb.stage == null ? new Stage() : scb.stage);

            stageToWork.initStyle(scb.stageStyle);

            try {
                stageToWork.initModality(scb.modality);
            } catch (Exception e) {
            }

            if (scb.ownerStage != null) {
                stageToWork.initOwner(scb.ownerStage);
            }

            stageToWork.setScene(scb.fxController.getScene());

            stageToWork.setTitle(scb.stageTitle != null ? scb.stageTitle : "");

            stageToWork.setAlwaysOnTop(scb.alwaysOnTop);

            if (scb.maximized) {
                stageToWork.setResizable(true);
                stageToWork.setMaximized(scb.maximized);
            } else {
                stageToWork.setMaximized(scb.maximized);
            }

            if (scb.centerOnScreen) {
                stageToWork.centerOnScreen();
            }

            if (scb.max_width != null) {
                stageToWork.setMaxWidth(scb.max_width);
            }

            if (scb.max_height != null) {
                stageToWork.setMaxHeight(scb.max_height);
            }

            if (scb.width != null) {
                stageToWork.setWidth(scb.width);
            }

            if (scb.height != null) {
                stageToWork.setHeight(scb.height);
            }

            if (scb.min_width != null) {
                stageToWork.setMinWidth(scb.min_width);
            }

            if (scb.min_height != null) {
                stageToWork.setMinHeight(scb.min_height);
            }

            if (scb.stageIcon != null) {
                stageToWork.getIcons().add(scb.stageIcon);
            }

            stageToWork.setResizable(scb.resizable);

            stageToWork.toFront();
            if (scb.showAndWait) {
                stageToWork.showAndWait();
            } else {
                stageToWork.show();
            }

            return true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Finaliza la aplicación JavaFX de manera controlada.
     *
     * <p>Primero solicita el cierre normal de la plataforma JavaFX mediante
     * {@link Platform#exit()}. Luego inicia un hilo auxiliar que fuerza la
     * terminación del proceso tras un breve retraso, como medida de respaldo
     * en caso de que existan recursos o hilos no finalizados correctamente.</p>
     *
     * <p>Si ocurre un error durante el proceso de salida controlada, se intenta
     * finalizar la JVM inmediatamente con código de salida {@code -1}.</p>
     */
    public static void exitApp() {
        try {
            Platform.exit();

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                System.exit(0);
            }, "ForceExitThread").start();

        } catch (Exception ex) {
            System.err.println("Error during safe exit: " + ex.getMessage());
            System.exit(-1);
        }
    }
}