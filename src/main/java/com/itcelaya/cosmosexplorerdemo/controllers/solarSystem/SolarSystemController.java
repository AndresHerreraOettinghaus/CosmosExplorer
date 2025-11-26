package com.itcelaya.cosmosexplorerdemo.controllers.solarSystem;

import com.google.gson.Gson;
import com.itcelaya.cosmosexplorerdemo.DAO.SolarSystem.SolarSystemDAO;
import com.itcelaya.cosmosexplorerdemo.DTO.SolarSystemDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class SolarSystemController {

    @FXML private TableView<SolarSystemDto.Body> bodiesTable;
    @FXML private TableColumn<SolarSystemDto.Body, String> colName;
    @FXML private TableColumn<SolarSystemDto.Body, String> colType;
    @FXML private TableColumn<SolarSystemDto.Body, Float> colGravity;

    private final SolarSystemDAO dao = new SolarSystemDAO();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("englishName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("bodyType"));
        colGravity.setCellValueFactory(new PropertyValueFactory<>("gravity"));

        loadDataFromApi();
    }

    private void loadDataFromApi() {
        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();

                String miClave = "6934f1ba-1498-49dc-90c0-aa2752ff7fe3";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.le-systeme-solaire.net/rest/bodies/"))
                        .header("Authorization", "Bearer " + miClave) // <--- ESTO ES LO NUEVO
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("ESTATUS API: " + response.statusCode());
                System.out.println("RESPUESTA API: " + response.body());

                if (response.statusCode() != 200) {
                    throw new RuntimeException("Error en servidor: " + response.statusCode());
                }

                Gson gson = new Gson();
                SolarSystemDto data = gson.fromJson(response.body(), SolarSystemDto.class);

                Platform.runLater(() -> {
                    bodiesTable.setItems(FXCollections.observableArrayList(data.getBodies()));
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error", "Revisa la consola para ver el detalle."));
            }
        });
    }

    @FXML
    void saveToFavorites() {
        SolarSystemDto.Body selectedBody = bodiesTable.getSelectionModel().getSelectedItem();

        if (selectedBody == null) {
            showAlert("Atención", "Selecciona un planeta o cuerpo primero.");
            return;
        }

        if (dao.saveFavorite(selectedBody)) {
            showAlert("Éxito", "¡Guardado en favoritos correctamente!");
        } else {
            showAlert("Error", "No se pudo guardar en la base de datos.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}