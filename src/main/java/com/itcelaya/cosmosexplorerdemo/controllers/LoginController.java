package com.itcelaya.cosmosexplorerdemo.controllers;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.itcelaya.cosmosexplorerdemo.DAO.UserDAO;
import com.itcelaya.cosmosexplorerdemo.models.User;
import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import com.itcelaya.cosmosexplorerdemo.util.loginImageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ImageView imageviewAPI;
    private List<User> userList = new ArrayList<>();
    @FXML
    private Label messageLabel;
    @FXML
    private VBox vbContainer;
    private StringField usernameField;
    private StringField passwordField;
    private loginImageManager loginImageManager;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginImageManager = new loginImageManager(imageviewAPI);
        loginImageManager.start();

        usernameField = Field.ofStringType("")
                .label("Username:")
                .required("Enter username");
        passwordField = Field.ofStringType("")
                .label("Password:")
                .required("Enter password");
        Form loginForm = Form.of(
                Group.of(usernameField, passwordField)
        ).title("Login");
        vbContainer.getChildren().add(new FormRenderer(loginForm));
        vbContainer.setSpacing(0);
    }
    @FXML
    protected void onSendClick() throws SQLException {
        searchUsers();
        String userName = usernameField.getValue();
        String password = passwordField.getValue();
        if(authenticate(userName, password)) {
            SceneManager.loadFxml("fxml/menu-view.fxml");
            System.out.printf("User %s successfully logged in\n", userName);
            loginImageManager.stop();
        } else {
            messageLabel.setText("Invalid username o password");
            messageLabel.getStyleClass().add("error");
        }
    }

    private boolean authenticate(String userName, String password) {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByUsernameOrEmail(userName);
        if (user != null && user.getPassword().equals(password)) {
            SceneManager.setUserKey(user.getId());
            return true;
        }
        return false;
    }

    private void searchUsers() throws SQLException {
        UserDAO userDAO = new UserDAO();
        userList = userDAO.findAll();
        if (userList.isEmpty()) {
            System.out.println("No se encontraron usuarios en la base de datos.");
        } else {
            System.out.println(userList.size() + " usuarios cargados desde la base de datos.");
        }
    }
}