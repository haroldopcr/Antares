package com.ingeniusapps.antares.javafx;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.AudioClip;

/**
 *
 * @author haroldop
 */
public class FXUISound {

    private final List<AudioClip> mediaMatrix = new ArrayList<>();

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

    public int loadSoundEffect(URL soundFile) {
        try {
            AudioClip clip = new AudioClip(soundFile.toExternalForm());
            mediaMatrix.add(clip);

            return mediaMatrix.size() - 1;
        } catch (Exception ex) {
            return -1;
        }
    }

    public void unloadSoundEffect(int mediaId) {
        try {
            if (mediaId < mediaMatrix.size()) {
                mediaMatrix.remove(mediaId);
            }
        } catch (Exception ex) {
        }
    }

    public void unloadSoundEffects() {
        try {
            mediaMatrix.clear();
        } catch (Exception ex) {
        }
    }

    public void playMedia(int mediaId) {
        try {
            if (mediaMatrix.get(mediaId).isPlaying()) {
                mediaMatrix.get(mediaId).stop();
            }

            mediaMatrix.get(mediaId).play();
        } catch (Exception ex) {
        }
    }

    public void volume(int mediaId, double volume) {
        try {
            if (mediaMatrix.get(mediaId).isPlaying()) {
                mediaMatrix.get(mediaId).stop();
            }

            mediaMatrix.get(mediaId).setVolume(volume);
        } catch (Exception ex) {
        }
    }

}
