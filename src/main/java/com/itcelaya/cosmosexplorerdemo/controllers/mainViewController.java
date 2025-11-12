package com.itcelaya.cosmosexplorerdemo.controllers;

import com.itcelaya.cosmosexplorerdemo.util.FavoriteEntry;
import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import com.itcelaya.cosmosexplorerdemo.util.SpaceXImageManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class mainViewController implements Initializable {
    @FXML
    private Label titleImage;

    @FXML
    private Label date;

    @FXML
    private Label description;

    @FXML
    private Hyperlink hyperLink;

    @FXML
    private ImageView imageviewAPI;
    private SpaceXImageManager imageManager;

    @FXML
    private ListView<FavoriteEntry> favoritesImagesTableview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageManager = new SpaceXImageManager(imageviewAPI);

        titleImage.textProperty().bind(imageManager.currentTitleProperty());
        date.textProperty().bind(imageManager.currentDateProperty());
        description.textProperty().bind(imageManager.currentDescriptionProperty());
        hyperLink.textProperty().bind(imageManager.currentHyperlinkProperty());
        hyperLink.visibleProperty().bind(
                imageManager.currentHyperlinkProperty().isNotNull()
                        .and(imageManager.currentHyperlinkProperty().isNotEmpty())
        );
        imageManager.initializeManager();
        setupFavoritesListView();
        bindDataToManager();
        favoritesImagesTableview.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        onFavoriteSelected(newSelection);
                    }
                }
        );    }


    @FXML
    void addToFavorites() {
        Image currentImage = imageviewAPI.getImage();
        String fullTitle = titleImage.getText();
        String currentDate = date.getText();
        String currentDesc = description.getText();
        String currentLink = hyperLink.getText(); // Hyperlink.getText() da la URL

        if (currentImage == null || currentImage.getUrl() == null ||
                fullTitle == null || fullTitle.isBlank() || fullTitle.equals("Cargando...")) {
            new Alert(Alert.AlertType.WARNING, "No hay información de imagen para guardar.").showAndWait();
            return;
        }

        String imageUrl = currentImage.getUrl();
        String shortTitle = Arrays.stream(fullTitle.split("\\s+"))
                .limit(2)
                .collect(Collectors.joining(" "));
        FavoriteEntry newFavorite = new FavoriteEntry(
                shortTitle, imageUrl, fullTitle, currentDate, currentDesc, currentLink
        );
        if (favoritesImagesTableview.getItems().contains(newFavorite)) {
            new Alert(Alert.AlertType.INFORMATION, "Esta imagen ya está en tus favoritos.").showAndWait();
        } else {
            favoritesImagesTableview.getItems().add(newFavorite);
        }
    }

    @FXML
    private FontIcon playStopIBtncon;

    @FXML
    void maximazeImage() {
        Image currentImage = imageviewAPI.getImage();
        if (currentImage != null) {
            SceneManager.showImageFullscreen(currentImage);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "No hay imagen para maximizar.").showAndWait();
        }
    }

    @FXML
    void nextImage() {
        bindDataToManager();
        if (imageManager != null) {
            imageManager.nextImage();
        }
    }

    @FXML
    void beforeImage() {
        bindDataToManager();
        if (imageManager != null) {
            imageManager.previousImage();
        }
    }

    @FXML
    void deleteSelected() {
        int selectedIndex = favoritesImagesTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar este favorito?");
            alert.setContentText("Esta acción no se puede deshacer.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                favoritesImagesTableview.getItems().remove(selectedIndex);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Elemento no seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un favorito de la lista para eliminar.");
            alert.showAndWait();
        }
    }
    @FXML
    void playBtn() {

    }


    private void setupFavoritesListView() {
        favoritesImagesTableview.setCellFactory(param -> new ListCell<FavoriteEntry>() {
            private final HBox hbox = new HBox(10);
            private final ImageView thumbImageView = new ImageView();
            private final Label titleLabel = new Label();

            {
                thumbImageView.setFitWidth(40);
                thumbImageView.setFitHeight(40);
                thumbImageView.setPreserveRatio(false);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().addAll(thumbImageView, titleLabel);
            }

            @Override
            protected void updateItem(FavoriteEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Image thumbImage = new Image(item.getImageUrl(), 40, 40, true, true, true);
                    thumbImageView.setImage(thumbImage);
                    titleLabel.setText(item.getDisplayTitle());
                    setGraphic(hbox);
                }
            }
        });
    }

    private void bindDataToManager() {
        titleImage.textProperty().bind(imageManager.currentTitleProperty());
        date.textProperty().bind(imageManager.currentDateProperty());
        description.textProperty().bind(imageManager.currentDescriptionProperty());
        hyperLink.textProperty().bind(imageManager.currentHyperlinkProperty());

        hyperLink.visibleProperty().bind(
                imageManager.currentHyperlinkProperty().isNotNull()
                        .and(imageManager.currentHyperlinkProperty().isNotEmpty())
        );
    }

    private void unbindDataFromManager() {
        titleImage.textProperty().unbind();
        date.textProperty().unbind();
        description.textProperty().unbind();
        hyperLink.textProperty().unbind();
        hyperLink.visibleProperty().unbind();
    }
    private void onFavoriteSelected(FavoriteEntry entry) {
        unbindDataFromManager();
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), imageviewAPI);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            titleImage.setText(entry.getFullTitle());
            date.setText(entry.getDate());
            description.setText(entry.getDescription());
            hyperLink.setText(entry.getArticleUrl());
            hyperLink.setVisible(entry.getArticleUrl() != null && !entry.getArticleUrl().isEmpty());
            imageviewAPI.setImage(new Image(entry.getImageUrl(), true));
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), imageviewAPI);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

}
