package com.itcelaya.cosmosexplorerdemo.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itcelaya.cosmosexplorerdemo.DTO.LaunchV5Dto;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.concurrent.CountDownLatch;

public class loginImageManager {
    private ImageView imageView;
    private final List<String> imageUrls = new ArrayList<>();
    private int currentImageIndex = 0;
    private Thread slideshowThread;

    private static final double FADE_DURATION = 1000; // 1 segundo
    private static final long PAUSE_DURATION = 3000; // 3 segundos


    public loginImageManager(ImageView imageView) {
        this.imageView = imageView;
    }

    public void start() {
        slideshowThread = new Thread(this::runSlideshow);
        slideshowThread.setDaemon(true);
        slideshowThread.start();
    }

    public void stop() {
        if (slideshowThread != null) {
            slideshowThread.interrupt();
            imageView = null;
        }
    }

    /**
     * Método principal que se ejecuta en el HILO SECUNDARIO.
     * Ahora usa CountDownLatch para sincronizar con la animación.
     */
    private void runSlideshow() {
        try {
            fetchImageUrlsFromSpaceXAPI();

            if (imageUrls.isEmpty()) {
                System.out.println("No se encontraron imágenes para el slideshow.");
                return;
            }

            // Carga la primera imagen sin animación
            Platform.runLater(() -> {
                imageView.setImage(new Image(imageUrls.get(0), true));
            });
            Thread.sleep((long) (PAUSE_DURATION + FADE_DURATION)); // Espera inicial

            // Bucle infinito
            while (true) {
                // 1. Prepara el Latch para esperar a la animación
                final CountDownLatch animationLatch = new CountDownLatch(1);

                // 2. Avanza el índice para la *siguiente* imagen
                currentImageIndex = (currentImageIndex + 1) % imageUrls.size();

                // 3. Ejecuta la animación en el Hilo de JavaFX
                Platform.runLater(() -> {
                    // 3a. Desvanecer (Fade Out)
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(FADE_DURATION), imageView);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    // 3b. Cuando termine el Fade Out, cambiar imagen y hacer Fade In
                    fadeOut.setOnFinished(event -> {
                        // Cambia la imagen mientras está invisible
                        String imageUrl = imageUrls.get(currentImageIndex);
                        Image newImage = new Image(imageUrl, true);
                        imageView.setImage(newImage);

                        // 3c. Aparecer (Fade In)
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(FADE_DURATION), imageView);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);

                        // 3d. Cuando termine el Fade In, avisar al hilo de fondo
                        fadeIn.setOnFinished(e -> {
                            animationLatch.countDown(); // <-- Libera el Latch
                        });
                        fadeIn.play();
                    });
                    fadeOut.play();
                });

                // 4. El Hilo Secundario espera aquí hasta que la animación termine
                animationLatch.await();

                // 5. Pone el hilo a dormir (pausa con la imagen visible)
                Thread.sleep(PAUSE_DURATION);
            }

        } catch (InterruptedException e) {
            System.out.println("Slideshow detenido.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Llama a la API v5 de SpaceX y extrae las URLs de las imágenes usando Jackson.
     * (Esta es la versión robusta que obtiene /past)
     */
    private void fetchImageUrlsFromSpaceXAPI() throws Exception {
        imageUrls.clear();

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

                imageUrls.addAll(launch.getLinks().getFlickr().getOriginal());
            }
        }
        System.out.println("Se encontraron " + imageUrls.size() + " imágenes de lanzamientos pasados (API v5 con Jackson).");
    }
}