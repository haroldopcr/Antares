package com.ingeniusapps.antares.javafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

abstract class FXController implements Initializable {

    @FXML
    private ApplicationPane rootPane = null;

    protected ApplicationPane getRootParent() {
        return this.rootPane;
    }

    protected AnchorPane getBusyPane() {
        return this.rootPane.getBusyPane();
    }

    protected BorderPane getBusyBorder() {
        return this.rootPane.getBusyBorder();
    }

    protected ProgressIndicator getBusyIndicator() {
        return this.rootPane.getBusyIndicator();
    }

    protected boolean isBusy() {
        return this.rootPane.isBusy();
    }

    protected void setBusy(boolean value) {
        this.rootPane.setBusy(value);
    }

    protected boolean isBlocked() {
        return this.rootPane.isBlocked();
    }

    protected void setBlock(boolean value) {
        this.rootPane.setBlock(value);
    }

    protected double getProgress() {
        return this.rootPane.getProgress();
    }

    protected void setProgress(double value) {
        this.rootPane.setProgress(value);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        constructor(url, rb);
    }

    protected abstract void constructor(URL url, ResourceBundle rb);

    protected abstract void destructor();

}
