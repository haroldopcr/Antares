package com.ingeniusapps.antares.system;

import com.ingeniusapps.antares.javafx.FXControllerScene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class StageControlBlock {

    Stage stage;
    public Stage ownerStage = null;
    public StageStyle stageStyle = StageStyle.DECORATED;
    public Modality modality = Modality.NONE;

    FXControllerScene fxController;

    public Double max_width = null;
    public Double max_height = null;
    public Double width = null;
    public Double height = null;
    public Double min_width = null;
    public Double min_height = null;

    public String stageTitle = null;

    public Image stageIcon = null;

    public boolean centerOnScreen = false;
    public boolean alwaysOnTop = false;
    public boolean resizable = false;
    public boolean maximized = false;
    public boolean showAndWait = false;

    public StageControlBlock(Stage stage, FXControllerScene fxController) {
        this.stage = stage;
        this.fxController = fxController;
    }

    public StageControlBlock(FXControllerScene fxController) {
        this.stage = new Stage();
        this.fxController = fxController;
    }
}
