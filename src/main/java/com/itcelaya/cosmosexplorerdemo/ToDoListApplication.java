package com.itcelaya.cosmosexplorerdemo;

import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ToDoListApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoListApplication.class.getResource("fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setOpacity(0.0);
        stage.setScene(scene);
        stage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(stage.opacityProperty(), 0.0)
                    ),
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(stage.opacityProperty(), 1.0)
                    )
            );
            fade.play();
        });
        delay.play();
    }
    public static void main(String[] args) {
        launch();
    }
}