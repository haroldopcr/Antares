package com.ingeniusapps.antares.javafx;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.AudioClip;

/**
 * Administrador básico de efectos de sonido para interfaces JavaFX.
 *
 * <p>Esta clase permite cargar, almacenar, reproducir, ajustar el volumen y
 * descargar efectos de sonido basados en {@link AudioClip}. Los recursos
 * cargados se mantienen en una colección interna y se referencian mediante
 * un identificador entero correspondiente a su posición en dicha colección.</p>
 *
 * <p>Está orientada a escenarios de reproducción de efectos breves de interfaz,
 * como clics, notificaciones, alertas o retroalimentación sonora de acciones
 * del usuario.</p>
 */
public class FXUISound {

    /**
     * Colección interna de clips de audio cargados en memoria.
     */
    private final List<AudioClip> mediaMatrix = new ArrayList<>();

    /**
     * Carga un efecto de sonido desde una ruta de archivo local.
     *
     * <p>Si la carga es exitosa, el clip se agrega a la colección interna y se
     * retorna su identificador. Si ocurre un error, el método retorna {@code -1}.</p>
     *
     * @param soundFile ruta del archivo de sonido
     * @return identificador del clip cargado, o {@code -1} si la carga falla
     */
    public int loadSoundEffect(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioClip clip = new AudioClip(file.toURI().toString());
            mediaMatrix.add(clip);

            return mediaMatrix.size() - 1;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Carga un efecto de sonido desde una URL.
     *
     * <p>Si la carga es exitosa, el clip se agrega a la colección interna y se
     * retorna su identificador. Si ocurre un error, el método retorna {@code -1}.</p>
     *
     * @param soundFile URL del recurso de audio
     * @return identificador del clip cargado, o {@code -1} si la carga falla
     */
    public int loadSoundEffect(URL soundFile) {
        try {
            AudioClip clip = new AudioClip(soundFile.toExternalForm());
            mediaMatrix.add(clip);

            return mediaMatrix.size() - 1;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Descarga un efecto de sonido previamente cargado.
     *
     * <p>Si el identificador existe dentro de la colección interna, el clip es
     * removido. Si ocurre un error o el identificador no es válido, la operación
     * es ignorada silenciosamente.</p>
     *
     * @param mediaId identificador del clip a remover
     */
    public void unloadSoundEffect(int mediaId) {
        try {
            if (mediaId < mediaMatrix.size()) {
                mediaMatrix.remove(mediaId);
            }
        } catch (Exception _) {
        }
    }

    /**
     * Descarga todos los efectos de sonido actualmente cargados.
     *
     * <p>La colección interna se limpia completamente. Si ocurre un error,
     * la operación es ignorada silenciosamente.</p>
     */
    public void unloadSoundEffects() {
        try {
            mediaMatrix.clear();
        } catch (Exception _) {
        }
    }

    /**
     * Reproduce un efecto de sonido identificado por su índice.
     *
     * <p>Si el clip ya se encuentra en reproducción, primero se detiene y luego
     * se reinicia desde el comienzo. Si el identificador no es válido u ocurre
     * un error, la operación es ignorada silenciosamente.</p>
     *
     * @param mediaId identificador del clip a reproducir
     */
    public void playMedia(int mediaId) {
        try {
            if (mediaMatrix.get(mediaId).isPlaying()) {
                mediaMatrix.get(mediaId).stop();
            }

            mediaMatrix.get(mediaId).play();
        } catch (Exception _) {
        }
    }

    /**
     * Ajusta el volumen de un efecto de sonido identificado por su índice.
     *
     * <p>Si el clip se encuentra reproduciéndose en el momento del ajuste,
     * primero se detiene y luego se aplica el nuevo volumen. Si el identificador
     * no es válido u ocurre un error, la operación es ignorada silenciosamente.</p>
     *
     * @param mediaId identificador del clip cuyo volumen se ajustará
     * @param volume nuevo volumen a establecer
     */
    public void volume(int mediaId, double volume) {
        try {
            if (mediaMatrix.get(mediaId).isPlaying()) {
                mediaMatrix.get(mediaId).stop();
            }

            mediaMatrix.get(mediaId).setVolume(volume);
        } catch (Exception _) {
        }
    }
}