package com.itcelaya.cosmosexplorerdemo.controllers.solarSystem;

import com.google.gson.Gson;
import com.itcelaya.cosmosexplorerdemo.DAO.SolarSystem.SolarSystemDAO;
import com.itcelaya.cosmosexplorerdemo.DTO.SolarSystemDto;
import com.itcelaya.cosmosexplorerdemo.cosmosExplorerApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static com.itcelaya.cosmosexplorerdemo.util.SceneManager.launchNewWindow;

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

        bodiesTable.setRowFactory(tv -> {
            javafx.scene.control.TableRow<SolarSystemDto.Body> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                // Verifica si fue doble click y si la fila no está vacía
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    SolarSystemDto.Body rowData = row.getItem();
                    // Llamamos al método que abre la ventana
                    openDetailWindow(rowData);
                }
            });
            return row;
        });
    }

    private void openDetailWindow(SolarSystemDto.Body body) {
        try {
//            launchNewWindow("fxml/SolarSystem/detail-view.fxml");
            // 1. Cargar el FXML
            FXMLLoader loader = new FXMLLoader(cosmosExplorerApplication.class.getResource("fxml/SolarSystem/detail-view.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de esa nueva vista
            DetailController controller = loader.getController();

            // 3. PASARLE LOS DATOS (Aquí ocurre la magia)
            controller.setPlanetData(body);

            // 4. Mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalles de " + body.getEnglishName());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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