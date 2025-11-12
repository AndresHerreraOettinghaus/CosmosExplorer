package com.itcelaya.cosmosexplorerdemo.controllers;

import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class menuController implements Initializable {
    @FXML
    private VBox windowContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneManager.loadVbox(windowContainer, "fxml/spaceXAPI-view.fxml");
    }
    @FXML
    void loadSpaceX() {
        SceneManager.loadVbox(windowContainer, "fxml/spaceXAPI-view.fxml");
    }

    @FXML
    void loadNasa() {
        SceneManager.launchNewWindow("fxml/nasaAPI-view.fxml");
    }

    @FXML
    void loadPlanets() {
        SceneManager.launchNewWindow("fxml/planetsAPI-view.fxml");
    }

    @FXML
    void exitProgram() {
        SceneManager.launchNewWindow("fxml/exit-view.fxml");
    }
}
