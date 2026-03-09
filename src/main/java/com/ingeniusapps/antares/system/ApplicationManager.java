package com.ingeniusapps.antares.system;

import com.ingeniusapps.antares.javafx.FXUIAnimations;
import com.ingeniusapps.antares.javafx.FXUIManager;
import com.ingeniusapps.antares.javafx.FXUISound;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *
 * @author haroldop
 */
public class ApplicationManager {

    public static final FXUIManager fxUIManager = new FXUIManager();
    public static final FXUIAnimations fxAnimations = new FXUIAnimations();
    public static final FXUISound fxSoundManager = new FXUISound();

    public static final boolean IS_LINUX = OSUtils.isLinux();
    public static final boolean IS_WINDOWS = OSUtils.isWindows();
    public static final boolean IS_MAC = OSUtils.isMac();

    public static final boolean showStage(StageControlBlock scb) {
        try {
            if (scb == null || scb.stage == null || scb.fxController == null) {
                return false;
            }

            Stage stageToWork = (scb.stage == null ? new Stage() : scb.stage);

            stageToWork.initStyle(scb.stageStyle);

            try {
                stageToWork.initModality(scb.modality);
            } catch (Exception e) {
            }

            if (scb.ownerStage != null) {
                stageToWork.initOwner(scb.ownerStage);
            }

            stageToWork.setScene(scb.fxController.getScene());

            stageToWork.setTitle(scb.stageTitle != null ? scb.stageTitle : "");

            stageToWork.setAlwaysOnTop(scb.alwaysOnTop);

            if (scb.maximized) {
                stageToWork.setResizable(true);
                stageToWork.setMaximized(scb.maximized);
            } else {
                stageToWork.setMaximized(scb.maximized);
            }

            if (scb.centerOnScreen) {
                stageToWork.centerOnScreen();
            }

            if (scb.max_width != null) {
                stageToWork.setMaxWidth(scb.max_width);
            }

            if (scb.max_height != null) {
                stageToWork.setMaxHeight(scb.max_height);
            }

            if (scb.width != null) {
                stageToWork.setWidth(scb.width);
            }

            if (scb.height != null) {
                stageToWork.setHeight(scb.height);
            }

            if (scb.min_width != null) {
                stageToWork.setMinWidth(scb.min_width);
            }

            if (scb.min_height != null) {
                stageToWork.setMinHeight(scb.min_height);
            }

            if (scb.stageIcon != null) {
                stageToWork.getIcons().add(scb.stageIcon);
            }

            stageToWork.setResizable(scb.resizable);

            stageToWork.toFront();
            if (scb.showAndWait) {
                stageToWork.showAndWait();
            } else {
                stageToWork.show();
            }

            return true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        }
    }

    public static void exitApp() {
        try {
            Platform.exit();

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                System.exit(0);
            }, "ForceExitThread").start();

        } catch (Exception ex) {
            System.err.println("Error during safe exit: " + ex.getMessage());
            System.exit(-1);
        }
    }
}
