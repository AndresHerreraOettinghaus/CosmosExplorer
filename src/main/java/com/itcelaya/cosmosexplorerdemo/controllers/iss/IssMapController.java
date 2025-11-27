package com.itcelaya.cosmosexplorerdemo.controllers.iss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcelaya.cosmosexplorerdemo.DTO.iss.IssPositionDto;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IssMapController {

    @FXML private WebView mapView;
    @FXML private Label lblConnectionStatus;
    @FXML private Label lblTimestamp;
    @FXML private ProgressBar progressBar;
    @FXML private Button btnCenterIss;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

    private WebEngine webEngine;
    private IssPositionDto lastIss;
    private boolean mapReady = false;
    private boolean autoCenter = true;

    @FXML
    public void initialize() {
        webEngine = mapView.getEngine();
        mapView.setContextMenuEnabled(false);
        loadLeafletMap();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                mapReady = true;
                setupBridge();
            }
        });

        setupCenterButton();
        startTracking();
    }

    private void loadLeafletMap() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    body { margin: 0; padding: 0; }
                    #map { height: 100vh; width: 100vw; }
                    /* Icono personalizado CSS para la ISS si no carga la imagen */
                    .iss-icon { font-size: 24px; text-align: center; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var map = L.map('map').setView([0, 0], 3);
                    
                    // Capa de OpenStreetMap (Gratis y rápida)
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 18,
                        attribution: '© OpenStreetMap'
                    }).addTo(map);

                    // Icono personalizado de la ISS
                    var issIcon = L.icon({
                        iconUrl: 'https://upload.wikimedia.org/wikipedia/commons/d/d0/International_Space_Station.svg',
                        iconSize: [50, 50],
                        iconAnchor: [25, 25],
                        popupAnchor: [0, -25]
                    });

                    var marker = L.marker([0, 0], {icon: issIcon}).addTo(map);
                    marker.bindPopup("<b>ISS Location</b><br>Esperando datos...");

                    // Función llamada desde Java
                    function updateMap(lat, lon, shouldCenter, infoText) {
                        var newLatLng = new L.LatLng(lat, lon);
                        marker.setLatLng(newLatLng);
                        marker.setPopupContent(infoText);
                        
                        if (shouldCenter) {
                            map.panTo(newLatLng);
                        }
                    }
                    
                    function centerNow() {
                        map.panTo(marker.getLatLng());
                        map.setZoom(5); // Zoom in
                    }
                </script>
            </body>
            </html>
            """;
        webEngine.loadContent(html);
    }

    private void setupBridge() {
        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("javaApp", this);
    }

    private void setupCenterButton() {
        btnCenterIss.setOnAction(e -> {
            autoCenter = true;
            if (lastIss != null && mapReady) {
                webEngine.executeScript("centerNow()");
            }
        });
    }

    private void startTracking() {
        scheduler.scheduleAtFixedRate(this::fetchIssPosition, 0, 2, TimeUnit.SECONDS);
    }

    private void fetchIssPosition() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.open-notify.org/iss-now.json"))
                .timeout(Duration.ofSeconds(4))
                .GET()
                .build();

        try {
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                IssPositionDto dto = mapper.readValue(resp.body(), IssPositionDto.class);
                lastIss = dto;
                Platform.runLater(() -> updateUiOnSuccess(dto));
            } else {
                Platform.runLater(() -> updateUiOnFailure("HTTP " + resp.statusCode()));
            }
        } catch (Exception ex) {
            Platform.runLater(() -> updateUiOnFailure("Error de Red"));
        }
    }

    private void updateUiOnSuccess(IssPositionDto dto) {
        lblConnectionStatus.setText("Status: Online");
        lblConnectionStatus.setStyle("-fx-text-fill: green;");

        String timeStr = formatter.format(Instant.ofEpochSecond(dto.getTimestamp()));
        lblTimestamp.setText("UTC: " + timeStr);
        progressBar.setProgress(1.0);

        if (mapReady) {
            double lat = dto.getIss_position().getLatitude();
            double lon = dto.getIss_position().getLongitude();
            String popupContent = String.format("<b>Lat:</b> %.4f<br><b>Lon:</b> %.4f<br><small>%s</small>", lat, lon, timeStr);
            webEngine.executeScript(String.format("updateMap(%f, %f, %b, '%s')",
                    lat, lon, autoCenter, popupContent));
            if(autoCenter) autoCenter = false;
        }
    }

    private void updateUiOnFailure(String reason) {
        lblConnectionStatus.setText("Status: ❌ " + reason);
        lblConnectionStatus.setStyle("-fx-text-fill: red;");
        progressBar.setProgress(0);
    }
    public void shutdown() {
        scheduler.shutdownNow();
    }
}