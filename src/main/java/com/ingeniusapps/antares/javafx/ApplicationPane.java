package com.ingeniusapps.antares.javafx;

import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Panel raíz especializado para escenas JavaFX con soporte integrado de
 * superposición visual de estado ocupado y bloqueo de interfaz.
 *
 * <p>Esta clase implementa programáticamente una estructura equivalente a un
 * contenedor FXML compuesto por un {@link StackPane} raíz y un overlay
 * superpuesto que contiene un {@link ProgressIndicator}. El propósito de este
 * componente es proporcionar una superficie base reutilizable para vistas que
 * necesiten mostrar estados de carga, progreso o bloqueo de interacción.</p>
 *
 * <p>El overlay se construye mediante un {@link AnchorPane} que cubre toda la
 * superficie del panel, dentro del cual se aloja un {@link BorderPane} con un
 * indicador de progreso centrado. Cuando el estado {@code busy} o el bloqueo
 * de interfaz están activos, dicho overlay se mantiene al frente y captura la
 * interacción del usuario.</p>
 *
 * <p>El componente está anotado con {@link DefaultProperty} para que los hijos
 * agregados declarativamente se inserten directamente en la colección
 * {@code children} del {@link StackPane}, quedando como hermanos del overlay.
 * El overlay se reordena internamente para permanecer por encima del resto del
 * contenido.</p>
 *
 * <p>No incorpora hojas de estilo por defecto más allá de las clases CSS
 * estructurales definidas en el propio componente, por lo que puede estilizarse
 * externamente según las necesidades de la aplicación.</p>
 */
@DefaultProperty("children")
public class ApplicationPane extends StackPane {

    /**
     * Panel superpuesto utilizado para cubrir la interfaz mientras el componente
     * se encuentra en estado ocupado o bloqueado.
     */
    private final AnchorPane busyPane = new AnchorPane();

    /**
     * Contenedor principal del overlay de estado ocupado.
     */
    private final BorderPane busyBorder = new BorderPane();

    /**
     * Indicador visual de progreso mostrado en el centro del overlay.
     */
    private final ProgressIndicator busyIndicator = new ProgressIndicator();

    /**
     * Propiedad que indica si la vista se encuentra en estado ocupado.
     *
     * <p>Cuando su valor es {@code true}, el overlay se hace visible y permanece
     * al frente del resto del contenido.</p>
     */
    private final BooleanProperty busy = new SimpleBooleanProperty(this, "busy", false);

    /**
     * Indica si el panel se encuentra actualmente en estado ocupado.
     *
     * @return {@code true} si el overlay de ocupación está activo;
     *         en caso contrario, {@code false}
     */
    public final boolean isBusy() {
        return busy.get();
    }

    /**
     * Define el estado ocupado del panel.
     *
     * <p>Cuando el valor recibido es {@code true}, se garantiza que el overlay
     * quede al frente de la jerarquía visual y el indicador de progreso sea
     * visible. Cuando se desactiva el estado ocupado, el progreso se restablece
     * a {@code 0}.</p>
     *
     * @param value nuevo estado ocupado
     */
    public final void setBusy(boolean value) {
        busyIndicator.setVisible(true);

        if (value) {
            bringOverlayToFront();
        } else {
            setProgress(0);
        }

        busy.set(value);
    }

    /**
     * Indica si el panel se encuentra actualmente bloqueado.
     *
     * <p>En la implementación actual este valor coincide con el estado de la
     * propiedad {@code busy}.</p>
     *
     * @return {@code true} si la interfaz está bloqueada; en caso contrario,
     *         {@code false}
     */
    public final boolean isBlocked() {
        return busy.get();
    }

    /**
     * Activa o desactiva un bloqueo visual de la interfaz sin mostrar el
     * indicador de progreso.
     *
     * <p>Cuando el valor recibido es {@code true}, el overlay se mantiene al
     * frente pero el {@link ProgressIndicator} se oculta. Cuando se desactiva el
     * bloqueo, el progreso se restablece a {@code 0}.</p>
     *
     * @param value nuevo estado de bloqueo
     */
    public final void setBlock(boolean value) {
        busyIndicator.setVisible(false);

        if (value) {
            bringOverlayToFront();
        } else {
            setProgress(0);
        }

        busy.set(value);
    }

    /**
     * Obtiene la propiedad observable del estado ocupado.
     *
     * @return propiedad {@code busy}
     */
    public final BooleanProperty busyProperty() {
        return busy;
    }

    /**
     * Propiedad observable del progreso visual mostrado por el indicador.
     */
    private final DoubleProperty progress = new SimpleDoubleProperty(this, "progress", 0.0);

    /**
     * Obtiene el valor actual del progreso.
     *
     * @return progreso actual
     */
    public final double getProgress() {
        return progress.get();
    }

    /**
     * Define el valor actual del progreso visual.
     *
     * @param value nuevo valor de progreso
     */
    public final void setProgress(double value) {
        progress.set(value);
    }

    /**
     * Obtiene la propiedad observable del progreso.
     *
     * @return propiedad {@code progress}
     */
    public final DoubleProperty progressProperty() {
        return progress;
    }

    /**
     * Crea una nueva instancia del panel y construye internamente la jerarquía
     * visual del overlay de estado ocupado.
     *
     * <p>Durante la construcción se:</p>
     *
     * <ul>
     *   <li>asigna la clase CSS base del contenedor raíz,</li>
     *   <li>configura el overlay con su estilo y comportamiento de bloqueo,</li>
     *   <li>ancla el {@link BorderPane} interno al tamaño completo del overlay,</li>
     *   <li>vincula el progreso del indicador a la propiedad {@code progress},</li>
     *   <li>y enlaza la visibilidad del overlay a la propiedad {@code busy}.</li>
     * </ul>
     */
    public ApplicationPane() {
        getStyleClass().add("BasePaneClass");

        busyPane.getStyleClass().add("BusyPaneClass");
        busyPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        busyPane.setVisible(false);
        busyPane.setPickOnBounds(true);
        busyPane.setMouseTransparent(false);

        AnchorPane.setTopAnchor(busyBorder, 0.0);
        AnchorPane.setRightAnchor(busyBorder, 0.0);
        AnchorPane.setBottomAnchor(busyBorder, 0.0);
        AnchorPane.setLeftAnchor(busyBorder, 0.0);
        busyPane.getChildren().add(busyBorder);

        busyIndicator.getStyleClass().add("BusyIndicatorClass");
        busyIndicator.progressProperty().bind(progressProperty());
        busyBorder.setCenter(busyIndicator);

        setAlignment(busyPane, Pos.CENTER);
        busyPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        getChildren().add(busyPane);
        bringOverlayToFront();

        busyPane.visibleProperty().bind(busyProperty());
        busyPane.managedProperty().bind(busyPane.visibleProperty());
    }

    /**
     * Reordena el overlay para asegurar que permanezca visualmente al frente.
     *
     * <p>El overlay se elimina y vuelve a agregarse como último hijo del
     * {@link StackPane}, se invoca {@code toFront()} y además se ajusta su
     * {@code viewOrder} para reforzar su prioridad visual.</p>
     */
    private void bringOverlayToFront() {
        getChildren().remove(busyPane);
        getChildren().add(busyPane);
        busyPane.toFront();
        busyPane.setViewOrder(-1);
    }

    /**
     * Obtiene el panel overlay utilizado para estado ocupado o bloqueo.
     *
     * @return panel {@link AnchorPane} del overlay
     */
    public AnchorPane getBusyPane() {
        return busyPane;
    }

    /**
     * Obtiene el contenedor principal interno del overlay.
     *
     * @return contenedor {@link BorderPane} del overlay
     */
    public BorderPane getBusyBorder() {
        return busyBorder;
    }

    /**
     * Obtiene el indicador visual de progreso del overlay.
     *
     * @return indicador de progreso
     */
    public ProgressIndicator getBusyIndicator() {
        return busyIndicator;
    }
}