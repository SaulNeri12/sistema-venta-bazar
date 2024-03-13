package com.bazar.sistemabazar;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LoginController {

    private Stage stage;

    @FXML
    public TextField loginPhoneField;
    @FXML
    public PasswordField loginPasswordField;
    @FXML
    public Button loginButton;

    @FXML
    public void onLoginBtnClick() {
        Pattern phoneNumberPattern = Pattern.compile("\\d{10,}-?\\d*$");
        Pattern passwordPattern = Pattern.compile("admin");

        boolean validPhoneNumber = phoneNumberPattern.matcher(loginPhoneField.getText()).matches();

        if (!validPhoneNumber) {
            showLoginErrorMessage("Telefono Invalido", "El numero de telefono dado no es valido.");
            return;
        }

        boolean validPassword = passwordPattern.matcher(loginPasswordField.getText()).matches();

        if (!validPassword) {
            showLoginErrorMessage("Contrasena Invalida", "La contraseña no es valida, la contrasena debe ser de al menos 8 caracteres y contener al menos un numero.");
        }

        this.close();
    }

    public void setStage(Stage stage) {
        if (stage != null) {
            this.stage = stage;
        }
    }

    public void close() {
        this.stage.close();
    }

    public void showLoginErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        // Mostrar la alerta
        alert.showAndWait();
    }
}
