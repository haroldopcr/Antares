package com.ingeniusapps.antares.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class FXControllerScene extends FXController implements Initializable {

    private ChangeListener<Scene> sceneListener = null;
    private ChangeListener<Window> stageListener = null;
    private Scene scene = null;

    public Scene getScene() {
        return this.scene;
    }

    public Stage getStage() {
        return (this.getScene() != null) ? (Stage) this.getScene().getWindow() : null;
    }

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

    public void disposeStage() {
        this.destructor();

        Stage stage = this.getStage();

        this.scene = null;

        stage.setScene(null);
        stage.close();
    }
}
