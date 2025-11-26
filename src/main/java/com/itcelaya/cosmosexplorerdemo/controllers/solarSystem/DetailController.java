package com.itcelaya.cosmosexplorerdemo.controllers.solarSystem;

import com.itcelaya.cosmosexplorerdemo.DTO.SolarSystemDto;
import com.itcelaya.cosmosexplorerdemo.cosmosExplorerApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DetailController {

    @FXML private Label lblName;
    @FXML private Label lblType;
    @FXML private Label lblGravity;
    @FXML private Label lblRadius;
    @FXML private Label lblDensity;
    @FXML private Label lblDiscovery;
    @FXML private Label lblDate;
    @FXML private ImageView imgPlanet;

    public void setPlanetData(SolarSystemDto.Body body) throws IOException {

        lblName.setText(body.getEnglishName());
        lblType.setText(body.getBodyType());
        lblGravity.setText(body.getGravity() + " m/s²");

        Map<String, String> imageByType = new HashMap<>();
        imageByType.put("planet", "default.jpg");
        imageByType.put("moon", "moon.png");
        imageByType.put("asteroid", "asteroid.png");
        imageByType.put("comet", "comet.png");
        imageByType.put("star", "star.png");
        imageByType.put("dwarf planet", "dwarf_planet.png");

        lblRadius.setText(body.getMeanRadius() != null ? body.getMeanRadius() + " km" : "N/D");
        lblDensity.setText(body.getDensity() != null ? body.getDensity() + " g/cm³" : "N/D");
        lblDiscovery.setText(body.getDiscoveredBy() != null && !body.getDiscoveredBy().isEmpty() ? body.getDiscoveredBy() : "Antigüedad");
        lblDate.setText(body.getDiscoveryDate() != null && !body.getDiscoveryDate().isEmpty() ? body.getDiscoveryDate() : "-");

        // 🔍 Normalizar el tipo
        String typeKey = body.getBodyType().toLowerCase().trim();

        String fileName = imageByType.getOrDefault(typeKey, "default.jpg");

        String imagePath = "/com/itcelaya/cosmosexplorerdemo/images/SolarSystem/" + fileName;

        InputStream stream = getClass().getResourceAsStream(imagePath);

        if (stream != null) {
            imgPlanet.setImage(new Image(stream));
        } else {
            System.out.println("⚠️ No se encontró la imagen: " + imagePath);
            imgPlanet.setImage(new Image(cosmosExplorerApplication.class.getResource("images/SolarSystem/moon.png").openStream()));
        }
    }


    @FXML
    void closeWindow() {
        if (lblName.getScene() != null) {
            Stage stage = (Stage) lblName.getScene().getWindow();
            stage.close();
        }
    }
}