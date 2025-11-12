package com.itcelaya.cosmosexplorerdemo.controllers;

import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import javafx.fxml.FXML;

public class exitController {
    @FXML
    void closePrimaryStage() {
        SceneManager.closeProgram();
        SceneManager.closePopup();
    }

    @FXML
    void closeThisStage() {
        SceneManager.closePopup();
    }
}