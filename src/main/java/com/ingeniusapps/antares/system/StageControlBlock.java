package com.ingeniusapps.antares.system;

import com.ingeniusapps.antares.javafx.FXControllerScene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Bloque de configuración para la creación y presentación de ventanas JavaFX.
 *
 * <p>Esta clase encapsula un {@link Stage} y su correspondiente
 * {@link FXControllerScene}, junto con un conjunto de propiedades opcionales
 * que permiten definir el comportamiento visual y modal de la ventana antes
 * de ser mostrada.</p>
 *
 * <p>Su propósito es centralizar en una sola estructura la configuración de una
 * ventana, incluyendo tamaño, propietario, modalidad, título, icono y banderas
 * de comportamiento como centrado en pantalla, redimensionamiento o espera
 * modal.</p>
 *
 * <p>Las propiedades se exponen como campos para facilitar su uso como objeto
 * de transporte de configuración dentro de la librería.</p>
 */
public final class StageControlBlock {

    /**
     * Instancia de la ventana JavaFX asociada a este bloque de control.
     */
    Stage stage;

    /**
     * Ventana propietaria del {@link #stage}.
     *
     * <p>Cuando se define, la nueva ventana puede comportarse como hija o dependiente
     * de esta ventana principal.</p>
     */
    public Stage ownerStage = null;

    /**
     * Estilo visual del {@link #stage}.
     *
     * <p>Por defecto se utiliza {@link StageStyle#DECORATED}.</p>
     */
    public StageStyle stageStyle = StageStyle.DECORATED;

    /**
     * Modalidad de la ventana.
     *
     * <p>Por defecto se utiliza {@link Modality#NONE}.</p>
     */
    public Modality modality = Modality.NONE;

    /**
     * Escena controlada asociada a la ventana.
     */
    FXControllerScene fxController;

    /**
     * Ancho máximo permitido para la ventana.
     */
    public Double max_width = null;

    /**
     * Alto máximo permitido para la ventana.
     */
    public Double max_height = null;

    /**
     * Ancho inicial de la ventana.
     */
    public Double width = null;

    /**
     * Alto inicial de la ventana.
     */
    public Double height = null;

    /**
     * Ancho mínimo permitido para la ventana.
     */
    public Double min_width = null;

    /**
     * Alto mínimo permitido para la ventana.
     */
    public Double min_height = null;

    /**
     * Título de la ventana.
     */
    public String stageTitle = null;

    /**
     * Icono de la ventana.
     */
    public Image stageIcon = null;

    /**
     * Indica si la ventana debe centrarse en pantalla al mostrarse.
     */
    public boolean centerOnScreen = false;

    /**
     * Indica si la ventana debe permanecer siempre encima de otras ventanas.
     */
    public boolean alwaysOnTop = false;

    /**
     * Indica si la ventana puede ser redimensionada por el usuario.
     */
    public boolean resizable = false;

    /**
     * Indica si la ventana debe abrirse maximizada.
     */
    public boolean maximized = false;

    /**
     * Indica si la ventana debe mostrarse usando un comportamiento modal
     * bloqueante equivalente a {@code showAndWait()}.
     */
    public boolean showAndWait = false;

    /**
     * Crea un bloque de control asociado a una instancia de {@link Stage}
     * ya existente.
     *
     * @param stage ventana JavaFX que será configurada mediante este bloque
     * @param fxController escena controlada asociada a la ventana
     */
    public StageControlBlock(Stage stage, FXControllerScene fxController) {
        this.stage = stage;
        this.fxController = fxController;
    }

    /**
     * Crea un bloque de control con una nueva instancia de {@link Stage}.
     *
     * @param fxController escena controlada asociada a la nueva ventana
     */
    public StageControlBlock(FXControllerScene fxController) {
        this.stage = new Stage();
        this.fxController = fxController;
    }
}