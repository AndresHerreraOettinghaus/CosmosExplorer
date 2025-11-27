package com.itcelaya.cosmosexplorerdemo.util.spaceX;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import com.itcelaya.cosmosexplorerdemo.DTO.spaceX.LaunchV5Dto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpaceXImageManager {
    private final ImageView imageView;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter FRIENDLY_FORMATTER = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault());

    private static class SlideData {
        final String imageUrl;
        final String title;
        final String date;
        final String description;
        final String articleUrl;

        SlideData(String imageUrl, String title, String rawDate, String description, String articleUrl) {
            this.imageUrl = imageUrl;
            this.title = title != null ? title : "No title";
            this.date = formatDateTime(rawDate);
            this.description = description != null ? description : "...";
            this.articleUrl = articleUrl;
        }

        private String formatDateTime(String rawDate) {
            if (rawDate == null) return ".../.../...";
            try {
                ZonedDateTime zdt = ZonedDateTime.parse(rawDate, ISO_FORMATTER);
                return zdt.format(FRIENDLY_FORMATTER);
            } catch (Exception e) {
                return rawDate;
            }
        }
    }

    private final List<SlideData> slides = new ArrayList<>();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(-1); // -1 = Aún no cargado

    private final StringProperty currentTitle = new SimpleStringProperty("Cargando...");
    private final StringProperty currentDate = new SimpleStringProperty("");
    private final StringProperty currentDescription = new SimpleStringProperty("Por favor, espere.");
    private final StringProperty currentHyperlink = new SimpleStringProperty();

    public StringProperty currentTitleProperty() { return currentTitle; }
    public StringProperty currentDateProperty() { return currentDate; }
    public StringProperty currentDescriptionProperty() { return currentDescription; }
    public StringProperty currentHyperlinkProperty() { return currentHyperlink; }

    public SpaceXImageManager(ImageView imageView) {
        this.imageView = imageView;
    }

    public void initializeManager() {
        new Thread(() -> {
            try {
                fetchDataFromAPI();
                if (!slides.isEmpty()) {
                    Platform.runLater(() -> showSlide(0));
                } else {
                    Platform.runLater(() -> currentTitle.set("No se encontraron datos."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> currentTitle.set("Error al cargar datos."));
            }
        }).start();
    }

    private void fetchDataFromAPI() throws Exception {
        slides.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spacexdata.com/v5/launches/past"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonBody = response.body();

        ObjectMapper objectMapper = new ObjectMapper();
        List<LaunchV5Dto> pastLaunches = objectMapper.readValue(jsonBody, new TypeReference<List<LaunchV5Dto>>() {});

        for (LaunchV5Dto launch : pastLaunches) {
            if (launch.getLinks() != null &&
                    launch.getLinks().getFlickr() != null &&
                    launch.getLinks().getFlickr().getOriginal() != null &&
                    !launch.getLinks().getFlickr().getOriginal().isEmpty()) {

                for (String imgUrl : launch.getLinks().getFlickr().getOriginal()) {
                    slides.add(new SlideData(
                            imgUrl,
                            launch.getName(),
                            launch.getDateUtc(),
                            launch.getDetails(),
                            launch.getLinks().getArticle()
                    ));
                }
            }
        }
        System.out.println("Se encontraron " + slides.size() + " imágenes/slides (API v5 con Jackson).");
    }

    public void nextImage() {
        if (slides.isEmpty()) return;
        int nextIndex = (currentIndex.get() + 1) % slides.size();
        showSlide(nextIndex);
    }

    public void previousImage() {
        if (slides.isEmpty()) return;
        int prevIndex = currentIndex.get() - 1;
        if (prevIndex < 0) {
            prevIndex = slides.size() - 1; // Da la vuelta al final
        }
        showSlide(prevIndex);
    }

    private void showSlide(int index) {
        if (index == currentIndex.get()) return;
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), imageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            SlideData currentSlide = slides.get(index);
            currentIndex.set(index);

            Image newImage = new Image(currentSlide.imageUrl, true);
            imageView.setImage(newImage);

            currentTitle.set(currentSlide.title);
            currentDate.set(currentSlide.date);
            currentDescription.set(currentSlide.description);
            currentHyperlink.set(currentSlide.articleUrl);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), imageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}