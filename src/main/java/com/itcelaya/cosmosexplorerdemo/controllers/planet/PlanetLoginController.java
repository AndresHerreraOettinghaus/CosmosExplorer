package com.itcelaya.cosmosexplorerdemo.controllers.planet;

import com.itcelaya.cosmosexplorerdemo.launcher.UsuarioService;
import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PlanetLoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        boolean valido = usuarioService.validarLogin(user, pass);

        if (valido) {
            abrirPantallaPrincipal();
        } else {
            mostrarAlerta("Usuario o contraseña incorrectos");
        }
    }

    @FXML
    private void registrarse() {
        try {
            SceneManager.menuController.loadRegistryPlanet();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la pantalla de registro.");
        }
    }

    private void abrirPantallaPrincipal() {
        try {
            SceneManager.menuController.loadPlanet();
        } catch (Exception e) {
            System.out.println("❌ Error al abrir pantalla principal: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
