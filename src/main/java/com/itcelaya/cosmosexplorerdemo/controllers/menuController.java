package com.itcelaya.cosmosexplorerdemo.controllers;

import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class menuController implements Initializable {
    @FXML
    public VBox windowContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneManager.setController(this);
        SceneManager.loadVbox(windowContainer, "fxml/spaceX/loginSpaceX-view.fxml");
    }
    @FXML
    public VBox loadSpaceX() {
        SceneManager.loadVbox(windowContainer, "fxml/spaceX/spaceXAPI-view.fxml");
        return null;
    }
    @FXML
    void loadPlanetData() {
        SceneManager.loadVbox(windowContainer, "fxml/planet/loginPlanet.fxml");
    }
    @FXML
    void loadISS() {

    }

    @FXML
    void loadSolarSystem() {

    }
    @FXML
    void exitProgram() {
        SceneManager.launchNewWindow("fxml/exit-view.fxml");
    }

    @FXML
    VBox loadLoginSpaceX() {
        SceneManager.loadVbox(windowContainer, "fxml/spaceX/loginSpaceX-view.fxml");
        return null;
    }
    @FXML
    public VBox loadRegistryPlanet() {
        SceneManager.loadVbox(windowContainer, "fxml/planet/RegistroExplorador.xml");
        return null;
    }
    @FXML
    public VBox loadLoginPlanet() {
        SceneManager.loadVbox(windowContainer, "fxml/planet/loginPlanet.fxml");
        return null;
    }
    @FXML
    public VBox loadPlanet() {
        SceneManager.loadVbox(windowContainer, "fxml/planet/planet.fxml");
        return null;
    }

}
