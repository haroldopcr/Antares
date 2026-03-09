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
 * AppSurface (JavaFX 26) Conversión 1:1 del FXML provisto:
 *
 * <StackPane styleClass="BasePaneClass">
 * <AnchorPane fx:id="busyPane" style="-fx-background-color: rgba(0, 0, 0, 0.5);" styleClass="BusyPaneClass" visible="false">
 * <BorderPane (anclado a 0) center =
 * <ProgressIndicator fx:id="busyIndicator" styleClass="BusyIndicatorClass" progress="0.0" />
 * </AnchorPane>
 * </StackPane
 * >
 *
 *
 * - Lo que sueltes en Scene Builder se agrega como hijo directo del root (hermano
 * del overlay). - El BusyPane siempre queda por delante para bloquear la UI
 * cuando busy=true. - Sin hoja de estilos por defecto; solo styleClass para
 * opcional CSS externo.
 */
@DefaultProperty("children")
public class ApplicationPane extends StackPane {

    // === Nodos tal cual el FXML ===
    private final AnchorPane busyPane = new AnchorPane();                       // fx:id="busyPane"
    private final BorderPane busyBorder = new BorderPane();
    private final ProgressIndicator busyIndicator = new ProgressIndicator();    // fx:id="busyIndicator"

    // === Propiedades públicas ===
    private final BooleanProperty busy = new SimpleBooleanProperty(this, "busy", false);

    public final boolean isBusy() {
        return busy.get();
    }

    public final void setBusy(boolean value) {
        busyIndicator.setVisible(true);

        if (value) {
            bringOverlayToFront();
        } else {
            setProgress(0);
        }

        busy.set(value);
    }

    public final boolean isBlocked() {
        return busy.get();
    }

    public final void setBlock(boolean value) {
        busyIndicator.setVisible(false);

        if (value) {
            bringOverlayToFront();
        } else {
            setProgress(0);
        }

        busy.set(value);
    }

    public final BooleanProperty busyProperty() {
        return busy;
    }

    private final DoubleProperty progress = new SimpleDoubleProperty(this, "progress", 0.0); // FXML: progress="0.0"

    public final double getProgress() {
        return progress.get();
    }

    public final void setProgress(double value) {
        progress.set(value);
    }

    public final DoubleProperty progressProperty() {
        return progress;
    }

    public ApplicationPane() {
        // styleClass del root (FXML: styleClass="BasePaneClass")
        getStyleClass().add("BasePaneClass");

        // === Construcción del overlay ===
        // busyPane (FXML: style, styleClass, visible="false")
        busyPane.getStyleClass().add("BusyPaneClass");
        busyPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        busyPane.setVisible(false); // por defecto false como en el FXML
        busyPane.setPickOnBounds(true);       // que bloquee clics aunque no tenga nodos hijos
        busyPane.setMouseTransparent(false);  // importante para bloquear interacción

        // BorderPane a tamaño completo dentro de busyPane (anclado a 0)
        AnchorPane.setTopAnchor(busyBorder, 0.0);
        AnchorPane.setRightAnchor(busyBorder, 0.0);
        AnchorPane.setBottomAnchor(busyBorder, 0.0);
        AnchorPane.setLeftAnchor(busyBorder, 0.0);
        busyPane.getChildren().add(busyBorder);

        // ProgressIndicator centrado (FXML: progress="0.0", styleClass)
        busyIndicator.getStyleClass().add("BusyIndicatorClass");
        busyIndicator.progressProperty().bind(progressProperty());
        busyBorder.setCenter(busyIndicator);

        // El overlay debe cubrir y quedar delante en el StackPane
        setAlignment(busyPane, Pos.CENTER);
        busyPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Jerarquía: primero el overlay (puedes añadir otros hijos; este quedará delante)
        getChildren().add(busyPane);
        bringOverlayToFront();

        // Visibilidad del overlay ligada a 'busy' (busy=true => visible)
        busyPane.visibleProperty().bind(busyProperty());
        busyPane.managedProperty().bind(busyPane.visibleProperty());
    }

    /**
     * Asegura que el BusyPane es el último hijo (arriba), y además con
     * viewOrder de máximo frente.
     */
    private void bringOverlayToFront() {
        getChildren().remove(busyPane);
        getChildren().add(busyPane);   // último hijo => tope en StackPane
        busyPane.toFront();
        busyPane.setViewOrder(-1);     // en JavaFX, número más bajo => más al frente
    }

    // === Accesores útiles (si necesitas referenciarlos) ===
    public AnchorPane getBusyPane() {
        return busyPane;
    }

    public BorderPane getBusyBorder() {
        return busyBorder;
    }

    public ProgressIndicator getBusyIndicator() {
        return busyIndicator;
    }
}

//<?xml version="1.0" encoding="UTF-8"?>
//
//<?import javafx.scene.control.ProgressIndicator?>
//<?import javafx.scene.layout.AnchorPane?>
//<?import javafx.scene.layout.BorderPane?>
//<?import javafx.scene.layout.StackPane?>
//
//<StackPane fx:id="basePame" styleClass="BasePaneClass" xmlns="http://javafx.com/javafx/24.0.1" xmlns:fx="http://javafx.com/fxml/1">
//   <children>
//      <AnchorPane fx:id="busyPane" style="-fx-background-color: rgba(0, 0, 0, 0.5);" styleClass="BusyPaneClass" visible="false">
//         <children>
//            <BorderPane AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0">
//               <center>
//                  <ProgressIndicator fx:id="busyIndicator" progress="0.0" styleClass="BusyIndicatorClass" BorderPane.alignment="CENTER" />
//               </center>
//            </BorderPane>
//         </children>
//      </AnchorPane>
//   </children>
//</StackPane>
