package com.bazar.sistemabazar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class LoginController {

    public Label phoneNumberErrorLabel;
    public Label passwordErrorLabel;
    private Stage stage;

    @FXML
    public TextField loginPhoneField;
    @FXML
    public PasswordField loginPasswordField;
    @FXML
    public Button loginButton;

    @FXML
    public void onLoginBtnClick() {
        Pattern phoneNumberPattern = Pattern.compile("^[0-9}]{10}$");
        Pattern passwordPattern = Pattern.compile("admin");

        boolean validPhoneNumber = phoneNumberPattern.matcher(loginPhoneField.getText()).matches();

        System.out.println(validPhoneNumber);

        if (!validPhoneNumber) {
            phoneNumberErrorLabel.setText("El numero no cumple con el formato correcto");
            phoneNumberErrorLabel.setVisible(true);
            return;
        }

        phoneNumberErrorLabel.setVisible(false);

        boolean validPassword = passwordPattern.matcher(loginPasswordField.getText()).matches();

        if (!validPassword) {
            passwordErrorLabel.setText("La contraseña debe ser de al menos 8 caracteres");
            passwordErrorLabel.setVisible(true);
            return;
        }

        passwordErrorLabel.setVisible(false);

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

}
