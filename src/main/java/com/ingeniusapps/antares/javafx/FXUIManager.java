package com.ingeniusapps.antares.javafx;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Administrador de carga de recursos FXML para interfaces JavaFX.
 *
 * <p>Esta clase ofrece una utilidad simplificada para cargar archivos FXML y
 * obtener directamente la instancia del controlador asociada al recurso
 * cargado.</p>
 *
 * <p>Está pensada como punto de apoyo para centralizar la creación de
 * controladores JavaFX basados en FXML dentro de la librería Antares o en
 * aplicaciones consumidoras.</p>
 */
public class FXUIManager {

    /**
     * Carga un recurso FXML y retorna su controlador asociado.
     *
     * <p>Internamente se crea un {@link FXMLLoader}, se carga el árbol visual
     * definido por el archivo FXML y luego se obtiene la instancia del
     * controlador mediante {@link FXMLLoader#getController()}.</p>
     *
     * <p>Si ocurre un error durante la carga, el método registra información
     * de diagnóstico en la salida de error estándar y retorna {@code null}.</p>
     *
     * @param <T> tipo esperado del controlador asociado al archivo FXML
     * @param fxml URL del recurso FXML a cargar
     * @return controlador asociado al recurso cargado, o {@code null} si ocurre un error
     */
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