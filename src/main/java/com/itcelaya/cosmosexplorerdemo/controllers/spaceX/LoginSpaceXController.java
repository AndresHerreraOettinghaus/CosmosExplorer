package com.itcelaya.cosmosexplorerdemo.controllers.spaceX;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.itcelaya.cosmosexplorerdemo.DAO.spaceX.UserDAO;
import com.itcelaya.cosmosexplorerdemo.models.spaceX.User;
import com.itcelaya.cosmosexplorerdemo.util.SceneManager;
import com.itcelaya.cosmosexplorerdemo.util.spaceX.loginImageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginSpaceXController implements Initializable {
    @FXML
    private ImageView imageviewAPI;
    private List<User> userList = new ArrayList<>();
    @FXML
    private Label messageLabel;
    @FXML
    private VBox vbContainer;
    private StringField usernameField;
    private StringField passwordField;
    private StringField suUsername;
    private StringField suEmail;
    private StringField suPassword;
    private StringField suConfirm;
    private loginImageManager loginImageManager;

    @FXML
    private Button singUpBtn;
    @FXML
    private Button loginBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vbContainer.setSpacing(8);

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

        vbContainer.getChildren().clear();
        vbContainer.getChildren().add(new FormRenderer(loginForm));
    }


    @FXML
    protected void onSendClick() throws SQLException {
        searchUsers();
        String userName = usernameField.getValue();
        String password = passwordField.getValue();
        if(authenticate(userName, password)) {
            loginImageManager.stop();
            SceneManager.menuController.loadSpaceX();
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

    @FXML
    protected void onSignUpClick() throws SQLException {
        String newUsername = suUsername.getValue();
        String newEmail = suEmail.getValue();
        String newPassword = suPassword.getValue();
        String confirmPassword = suConfirm.getValue();

        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            messageLabel.getStyleClass().add("error");
            return;
        }

        User newUser = new User(0, newUsername, newEmail, newPassword);

        UserDAO userDAO = new UserDAO();
        if (userDAO.save(newUser)) {
            messageLabel.setText("User created successfully!");
            messageLabel.getStyleClass().add("success");
            showLoginScreen();
        } else {
            messageLabel.setText("Error creating user");
            messageLabel.getStyleClass().add("error");
        }
    }
    @FXML
    protected void onShowSignUp() {
        // Ocultar botones de Login
        loginBtn.setVisible(false);
        singUpBtn.setVisible(false);

        Button returnBtn = new Button("Return");

        returnBtn.setOnAction(e -> {
            try {
                showLoginScreen();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Limpiar el contenedor
        vbContainer.getChildren().clear();

        // ---------------------
        // Crear campos SignUp
        // ---------------------
        suUsername = Field.ofStringType("")
                .label("New username:")
                .required("Enter new username");

        suEmail = Field.ofStringType("")
                .label("Email:")
                .required("Enter email");

        suPassword = Field.ofStringType("")
                .label("Password:")
                .required("Enter password");

        suConfirm = Field.ofStringType("")
                .label("Password:")
                .required("Confirm password");

        Form signUpForm = Form.of(
                Group.of(suUsername, suEmail, suPassword, suConfirm)
        ).title("Create Account");

        FormRenderer renderer = new FormRenderer(signUpForm);

        // ---------------------
        // Botón Create Account
        // ---------------------
        Button createAccountBtn = new Button("Create Account");
        createAccountBtn.setOnAction(e -> {
            try {
                onSignUpClick();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox hBox  = new HBox(createAccountBtn, returnBtn);
        hBox.setSpacing(16);
        hBox.setAlignment(Pos.CENTER);
        vbContainer.getChildren().addAll(renderer, hBox);
    }

    private void showLoginScreen() {
        // Mostrar botones
        loginBtn.setVisible(true);
        singUpBtn.setVisible(true);

        // Limpiar el contenedor
        vbContainer.getChildren().clear();

        usernameField = Field.ofStringType("")
                .label("Username:")
                .required("Enter username");

        passwordField = Field.ofStringType("")
                .label("Password:")
                .required("Enter password");

        Form loginForm = Form.of(
                Group.of(usernameField, passwordField)
        ).title("Login");

        vbContainer.getChildren().clear();
        vbContainer.getChildren().add(new FormRenderer(loginForm));
    }

}