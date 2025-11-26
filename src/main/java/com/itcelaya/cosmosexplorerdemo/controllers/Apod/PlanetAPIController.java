package com.itcelaya.cosmosexplorerdemo.controllers.Apod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itcelaya.cosmosexplorerdemo.services.PlanetAPIService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class PlanetAPIController {

    @FXML private TextField txtFecha;

    @FXML private TableView<JsonObject> tablaEscenas;
    @FXML private TableColumn<JsonObject, String> colId;
    @FXML private TableColumn<JsonObject, String> colFecha;
    @FXML private TableColumn<JsonObject, String> colNubes;

    private ObservableList<JsonObject> escenasList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().get("id").getAsString()
                )
        );

        colFecha.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getAsJsonObject("properties")
                                .get("acquired").getAsString()
                )
        );

        colNubes.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                c.getValue().getAsJsonObject("properties")
                                        .get("cloud_cover").getAsDouble() * 100
                        )
                )
        );
    }

    @FXML
    private void buscarEscenas() {
        String fecha = txtFecha.getText().trim();

        if (fecha.isEmpty()) {
            mostrar("Ingresa una fecha en formato YYYY-MM-DD");
            return;
        }

        JsonArray results = PlanetAPIService.buscarEscenas(fecha);

        if (results == null) {
            mostrar("Error consultando Planet API");
            return;
        }

        escenasList.clear();

        for (JsonElement elem : results) {
            escenasList.add(elem.getAsJsonObject());
        }

        tablaEscenas.setItems(escenasList);

        mostrar("Escenas cargadas: " + escenasList.size());
    }

    @FXML
    private void verMiniatura() {
        JsonObject escena = tablaEscenas.getSelectionModel().getSelectedItem();

        if (escena == null) {
            mostrar("Selecciona una escena primero.");
            return;
        }

        String assetLink = escena.getAsJsonObject("_links")
                .getAsJsonObject("assets")
                .get("thumbnail")
                .getAsString();

        String urlImagen = PlanetAPIService.descargarThumbnail(assetLink);

        if (urlImagen == null) {
            mostrar("No se pudo obtener miniatura.");
            return;
        }

        mostrar("Miniatura disponible en:\n" + urlImagen);
    }

    @FXML
    private void volver() {
        mostrar("Pantalla de retorno pendiente (la conectamos después).");
    }

    private void mostrar(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
