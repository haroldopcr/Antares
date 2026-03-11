package com.ingeniusapps.antares.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Controlador base abstracto para escenas JavaFX dentro de la librería Antares.
 *
 * <p>Esta clase extiende {@link FXController} e implementa {@link Initializable}
 * para proporcionar un ciclo de vida más estructurado a controladores asociados
 * a vistas FXML. Su propósito principal es detectar el momento en que el nodo
 * raíz ya pertenece a una {@link Scene} y a un {@link Stage}, de forma que
 * puedan ejecutarse correctamente los métodos de inicialización avanzada y de
 * liberación de recursos.</p>
 *
 * <p>Durante la inicialización, la clase crea una {@link Scene} a partir del
 * nodo raíz del controlador y registra listeners temporales para detectar
 * cuándo esa escena queda asociada a una ventana real. Una vez que el
 * {@link Stage} está disponible, se invoca el método {@code constructor(url, rb)}
 * definido en la jerarquía superior y se configura la llamada a
 * {@code destructor()} al cerrar la ventana.</p>
 *
 * <p>Esta clase requiere que el panel raíz del archivo FXML tenga asignado
 * el identificador {@code fx:id="rootPane"} para que el mecanismo interno
 * pueda resolver correctamente el nodo raíz mediante {@code getRootParent()}.</p>
 *
 * <p>Está diseñada para ser extendida por controladores concretos de escenas,
 * no para usarse directamente.</p>
 */
public abstract class FXControllerScene extends FXController implements Initializable {

    /**
     * Listener temporal utilizado para detectar cuándo el nodo raíz ha sido
     * asociado a una {@link Scene}.
     */
    private ChangeListener<Scene> sceneListener = null;

    /**
     * Listener temporal utilizado para detectar cuándo la {@link Scene}
     * ha sido asociada a una {@link Window}.
     */
    private ChangeListener<Window> stageListener = null;

    /**
     * Escena JavaFX creada a partir del nodo raíz del controlador.
     */
    private Scene scene = null;

    /**
     * Obtiene la escena asociada al controlador.
     *
     * @return escena administrada por esta instancia, o {@code null} si ya fue liberada
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Obtiene el {@link Stage} asociado a la escena actual.
     *
     * @return ventana asociada a la escena, o {@code null} si la escena no está disponible
     */
    public Stage getStage() {
        return (this.getScene() != null) ? (Stage) this.getScene().getWindow() : null;
    }

    /**
     * Inicializa el controlador y configura el ciclo de vida de la escena.
     *
     * <p>Este método:</p>
     *
     * <ul>
     *   <li>valida la existencia del nodo raíz requerido,</li>
     *   <li>crea una nueva {@link Scene} a partir de dicho nodo,</li>
     *   <li>espera a que la escena sea asociada a una ventana,</li>
     *   <li>invoca el método {@code constructor(url, rb)} cuando el {@link Stage} ya existe,</li>
     *   <li>y registra la ejecución de {@code destructor()} al cerrar la ventana.</li>
     * </ul>
     *
     * <p>Si el nodo raíz no puede resolverse mediante {@code getRootParent()},
     * se lanza una {@link IllegalStateException} indicando el requisito del
     * identificador {@code fx:id="rootPane"} en el FXML.</p>
     *
     * @param url ubicación del recurso utilizado para inicializar el controlador
     * @param rb recursos internacionalizados asociados, si existen
     * @throws IllegalStateException si el nodo raíz requerido no está disponible
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sceneListener = (obs, oldScene, newScene) -> {
            if (newScene != null) {
                stageListener = (stageObs, oldStage, newStage) -> {
                    if (newStage != null) {
                        // Llamar al "constructor" una vez que el Stage ya está disponible
                        constructor(url, rb);

                        // Agregar listener de cierre de ventana
                        ((Stage) newStage).setOnCloseRequest(event -> {
                            try {
                                destructor(); // Llamar al destructor al cerrar la ventana.
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }

                    // Desconectar listener, ya no se necesita
                    newScene.windowProperty().removeListener(stageListener);
                    stageListener = null;
                };

                newScene.windowProperty().addListener(stageListener);
            }

            // Desconectar listener, ya no se necesita
            this.getRootParent().sceneProperty().removeListener(sceneListener);
            sceneListener = null;
        };

        if (this.getRootParent() == null) {
            throw new IllegalStateException(
                    "========> ATTENTION!! ANTARES library requires that the fx:id property of the root panel in the FXML file be set to the value \"rootPane\", as shown in the following example: fx:id=\"rootPane\". <======== THE ERROR IS HERE!"
            );
        }

        this.getRootParent().sceneProperty().addListener(sceneListener);
        this.scene = new Scene(this.getRootParent());
    }

    /**
     * Libera la escena actual y cierra la ventana asociada.
     *
     * <p>Antes de cerrar el {@link Stage}, este método invoca explícitamente
     * {@code destructor()}, limpia la referencia local a la escena, desacopla
     * la escena del {@link Stage} y finalmente cierra la ventana.</p>
     *
     * <p>Este método resulta útil cuando el controlador desea cerrar su propia
     * ventana de forma programática asegurando que se ejecute la lógica de
     * liberación asociada.</p>
     */
    public void disposeStage() {
        this.destructor();

        Stage stage = this.getStage();

        this.scene = null;

        stage.setScene(null);
        stage.close();
    }
}