package com.bazar.sistemabazar.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetosDTO.UsuarioDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.util.regex.Pattern;

public class LoginController {

    private UsuarioDTO usuario;
    private IPersistenciaBazar persistencia;

    private Stage stage;

    public Label phoneNumberErrorLabel;
    public Label passwordErrorLabel;


    @FXML
    public TextField loginPhoneField;
    @FXML
    public PasswordField loginPasswordField;
    @FXML
    public Button loginButton;


    public LoginController(IPersistenciaBazar persistencia) {
        this.persistencia = persistencia;
    }


    @FXML
    public void onLoginBtnClick() {
        Pattern phoneNumberPattern = Pattern.compile("^[0-9}]{8,15}$");
        Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9?]{8,15}$");

        boolean validPhoneNumber = phoneNumberPattern.matcher(loginPhoneField.getText()).matches();

        //System.out.println(validPhoneNumber);

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

        try {
            this.usuario = persistencia.iniciarSesionUsuario(loginPhoneField.getText(), loginPasswordField.getText());

            if (this.usuario == null) {
                throw new PersistenciaBazarException("No se encontro al usuario con el telefono especificado.");
            }

            // continua...
        } catch (PersistenciaBazarException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Inicio Sesion");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }

        this.close();
    }

    public UsuarioDTO obtenerUsuarioLogeado() {
        return this.usuario;
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

