package com.ingeniusapps.antares.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Controlador base abstracto para vistas JavaFX dentro de la librería Antares.
 *
 * <p>Esta clase define una capa común para controladores FXML que utilizan
 * {@link ApplicationPane} como panel raíz. Su propósito es centralizar el acceso
 * a elementos estructurales del contenedor principal, especialmente aquellos
 * relacionados con estados de carga, bloqueo de interfaz y progreso visual.</p>
 *
 * <p>Además, establece un ciclo de vida simplificado mediante los métodos
 * abstractos {@link #constructor(URL, ResourceBundle)} y
 * {@link #destructor()}, permitiendo que las clases derivadas implementen su
 * lógica de inicialización y liberación de recursos de forma consistente.</p>
 *
 * <p>La clase tiene visibilidad de paquete y está pensada únicamente como base
 * interna para otros controladores del módulo JavaFX.</p>
 */
abstract class FXController implements Initializable {

    /**
     * Panel raíz asociado al controlador.
     *
     * <p>Este campo debe enlazarse desde FXML mediante
     * {@code fx:id="rootPane"}.</p>
     */
    @FXML
    private ApplicationPane rootPane = null;

    /**
     * Obtiene el panel raíz asociado al controlador.
     *
     * @return instancia del panel raíz {@link ApplicationPane}
     */
    protected ApplicationPane getRootParent() {
        return this.rootPane;
    }

    /**
     * Obtiene el panel superpuesto utilizado para indicar estado de carga o bloqueo.
     *
     * @return panel {@link AnchorPane} asociado al estado ocupado
     */
    protected AnchorPane getBusyPane() {
        return this.rootPane.getBusyPane();
    }

    /**
     * Obtiene el contenedor central asociado al panel de estado ocupado.
     *
     * @return contenedor {@link BorderPane} del área de carga
     */
    protected BorderPane getBusyBorder() {
        return this.rootPane.getBusyBorder();
    }

    /**
     * Obtiene el indicador de progreso visual asociado al estado ocupado.
     *
     * @return indicador de progreso
     */
    protected ProgressIndicator getBusyIndicator() {
        return this.rootPane.getBusyIndicator();
    }

    /**
     * Indica si el panel raíz se encuentra actualmente en estado ocupado.
     *
     * @return {@code true} si la vista está en estado busy; en caso contrario {@code false}
     */
    protected boolean isBusy() {
        return this.rootPane.isBusy();
    }

    /**
     * Define el estado ocupado del panel raíz.
     *
     * @param value {@code true} para activar el estado busy; {@code false} para desactivarlo
     */
    protected void setBusy(boolean value) {
        this.rootPane.setBusy(value);
    }

    /**
     * Indica si la interfaz asociada al panel raíz se encuentra bloqueada.
     *
     * @return {@code true} si la vista está bloqueada; en caso contrario {@code false}
     */
    protected boolean isBlocked() {
        return this.rootPane.isBlocked();
    }

    /**
     * Define el estado de bloqueo del panel raíz.
     *
     * @param value {@code true} para bloquear la interfaz; {@code false} para desbloquearla
     */
    protected void setBlock(boolean value) {
        this.rootPane.setBlock(value);
    }

    /**
     * Obtiene el valor actual de progreso visual del panel raíz.
     *
     * @return valor actual de progreso
     */
    protected double getProgress() {
        return this.rootPane.getProgress();
    }

    /**
     * Define el valor de progreso visual del panel raíz.
     *
     * @param value nuevo valor de progreso
     */
    protected void setProgress(double value) {
        this.rootPane.setProgress(value);
    }

    /**
     * Inicializa el controlador e invoca el método de construcción definido
     * por la clase derivada.
     *
     * <p>Este método forma parte del ciclo de vida estándar de JavaFX y actúa
     * como puente hacia {@link #constructor(URL, ResourceBundle)}.</p>
     *
     * @param url ubicación del recurso utilizado para inicializar el controlador
     * @param rb recursos internacionalizados asociados, si existen
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        constructor(url, rb);
    }

    /**
     * Método de inicialización específico de la clase derivada.
     *
     * <p>Las subclases deben implementar aquí su lógica de construcción o
     * preparación inicial del controlador.</p>
     *
     * @param url ubicación del recurso FXML o recurso asociado
     * @param rb recursos internacionalizados disponibles para la inicialización
     */
    protected abstract void constructor(URL url, ResourceBundle rb);

    /**
     * Método de liberación de recursos específico de la clase derivada.
     *
     * <p>Las subclases deben implementar aquí su lógica de limpieza, cierre
     * o liberación antes de desechar el controlador o la ventana asociada.</p>
     */
    protected abstract void destructor();

}