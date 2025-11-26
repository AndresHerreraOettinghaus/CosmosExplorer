package com.itcelaya.cosmosexplorerdemo.util;

import com.itcelaya.cosmosexplorerdemo.controllers.menuController;
import com.itcelaya.cosmosexplorerdemo.cosmosExplorerApplication;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class SceneManager {
    public static menuController menuController;
    private static Stage primaryStage;
    private static Stage popupStage;

    private static int userKey;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void loadFxml(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(cosmosExplorerApplication.class.getResource(fxmlFile));
            Pane root = loader.load();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.setOpacity(0.0);
            primaryStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(primaryStage.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(primaryStage.opacityProperty(), 1.0)
                        )
                );
                fade.play();
            });
            delay.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void launchNewWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(cosmosExplorerApplication.class.getResource(fxmlFile));
            Pane root = loader.load();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void closePopup(){
        popupStage.close();
    }

    public static void loadVbox(VBox windowContainer, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(cosmosExplorerApplication.class.getResource(fxmlFile));
            Pane root = loader.load();
            windowContainer.getChildren().clear();
            windowContainer.getChildren().add(root);
            VBox.setVgrow(root, Priority.ALWAYS);
            HBox.setHgrow(root, Priority.ALWAYS);
            windowContainer.setOpacity(0.0);
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(windowContainer.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(windowContainer.opacityProperty(), 1.0)
                        )
                );
                fade.play();
            });
            delay.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUserKey(int userKey) {
        SceneManager.userKey = userKey;
    }

    public static int getUserKey() {
        return userKey;
    }

    public static void closeProgram(){
        primaryStage.close();
    }

    public static void showImageFullscreen(Image imageToShow) {
        if (imageToShow == null) {
            System.err.println("No hay imagen para mostrar.");
            return;
        }

        // 1. Crear el ImageView para la ventana emergente
        ImageView fullscreenImageView = new ImageView(imageToShow);
        fullscreenImageView.setPreserveRatio(true);
        fullscreenImageView.setSmooth(true);

        // 2. Crear un panel contenedor (StackPane centra la imagen)
        StackPane root = new StackPane();
        root.getChildren().add(fullscreenImageView);
        // Estilo de "lightbox" (fondo oscuro semitransparente)
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");

        // 3. Obtener el tamaño de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        scene.setFill(Color.TRANSPARENT); // El fondo de la escena es transparente

        // 4. Configurar el Stage (ventana)
        // Usamos 'popupStage' que ya tienes
        popupStage = new Stage();
        popupStage.initStyle(StageStyle.TRANSPARENT); // Sin bordes
        popupStage.setScene(scene);

        // Maximizar a pantalla completa
        popupStage.setFullScreen(true);
        popupStage.setFullScreenExitHint(""); // Oculta el mensaje "Press ESC to exit"

        // 5. Ajustar la imagen al tamaño de la ventana
        fullscreenImageView.fitWidthProperty().bind(scene.widthProperty());
        fullscreenImageView.fitHeightProperty().bind(scene.heightProperty());

        // 6. Añadir listeners para cerrar la ventana
        scene.setOnKeyPressed(e -> popupStage.close());
        scene.setOnMouseClicked(e -> popupStage.close());

        // 7. Mostrar
        popupStage.show();
    }

    public static void setController(menuController menuController) {
        SceneManager.menuController = menuController;
    }
}
