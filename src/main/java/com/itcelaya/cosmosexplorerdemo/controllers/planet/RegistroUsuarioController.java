package com.itcelaya.cosmosexplorerdemo.controllers.planet;

import com.itcelaya.cosmosexplorerdemo.launcher.UsuarioService;
import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroUsuarioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void registrarUsuario() {

        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            lblError.setText("Todos los campos son obligatorios.");
            return;
        }
        boolean exito = usuarioService.registrarUsuario(nombre, username, password);
        if (exito) {
            volverAlLogin();
        } else {
            lblError.setText("Error al registrar. El usuario podría ya existir.");
        }
    }

    @FXML
    private void volverAlLogin() {
        try {
            SceneManager.menuController.loadLoginPlanet();
        } catch (Exception e) {
            lblError.setText("Error al volver al login.");
            e.printStackTrace();
        }
    }
}
