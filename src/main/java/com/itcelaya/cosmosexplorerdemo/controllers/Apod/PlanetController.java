package com.itcelaya.cosmosexplorerdemo.controllers.Apod;

import com.itcelaya.cosmosexplorerdemo.models.planet.Planet;
import com.itcelaya.cosmosexplorerdemo.services.PlanetService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class PlanetController implements Initializable {

    @FXML private ImageView imgApod;
    @FXML private Label lblTitle;
    @FXML private Label lblDate;
    @FXML private TextArea txtExplanation;

    private Planet apodActual;

    @FXML
    private void cargarApod() {
        Planet apod = PlanetService.obtenerApod();
        if (apod != null) {
            apodActual = apod;
            lblTitle.setText(apod.getTitle());
            lblDate.setText(apod.getDate());
            txtExplanation.setText(apod.getExplanation());
            imgApod.setImage(new Image(apod.getUrl()));
        }
    }

    @FXML
    private void guardarApod() {
        if (apodActual == null) return;

        if (PlanetService.guardarApodEnBD(apodActual)) {
            mostrarMensaje("APOD guardado en la base de datos.");
        }
    }

    @FXML
    private void mostrarHistorial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/historial_apod.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Historial APOD");
            stage.show();
        } catch (Exception e) {
            System.out.println("❌ Error al abrir historial: " + e.getMessage());
        }
    }

    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Planet apod = PlanetService.obtenerApod();
        if (apod != null) {
            apodActual = apod;
            lblTitle.setText(apod.getTitle());
            lblDate.setText(apod.getDate());
            txtExplanation.setText(apod.getExplanation());
            imgApod.setImage(new Image(apod.getUrl()));
        }
    }
}
