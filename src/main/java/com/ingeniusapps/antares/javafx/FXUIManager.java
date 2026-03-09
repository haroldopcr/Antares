package com.ingeniusapps.antares.javafx;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FXUIManager {

    public <T> T loadFXML(URL fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(fxml);
            Parent rootPane = loader.load();

            return loader.getController();
        } catch (Exception ex) {
            System.err.printf("\n\n" + ex.getCause().getLocalizedMessage() + "\n\n");
            System.err.printf("Error FXML %s: %s%n", fxml, ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

}
