package com.itcelaya.cosmosexplorerdemo.controllers.Apod;

import com.itcelaya.cosmosexplorerdemo.models.planet.Planet;
import com.itcelaya.cosmosexplorerdemo.services.PlanetService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HistorialApodController {

    @FXML private ListView<String> listaApods;

    @FXML
    public void initialize() {
        listaApods.getItems().clear();

        for (Planet apod : PlanetService.obtenerApodsGuardados()) {
            listaApods.getItems().add(apod.getDate() + " — " + apod.getTitle());
        }
    }
}
