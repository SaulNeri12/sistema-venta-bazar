package com.bazar.sistemabazar.controllers.dialogs;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetosDTO.DireccionDTO;
import objetosDTO.UsuarioDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegistrarUsuarioDlgController implements Initializable {

    private IPersistenciaBazar persistencia;

    private Stage stage;

    private boolean operacionCancelada;

    @FXML
    public TextField nombreUsuarioTextField;
    @FXML
    public TextField apellidoUsuarioTextField;
    @FXML
    public ChoiceBox<String> puestoChoiceBox;
    @FXML
    public TextField telefonoTextField;
    @FXML
    public TextField calleDireccionTextField;
    @FXML
    public TextField coloniaDireccionTextField;
    @FXML
    public TextField codigoPostalTextField;
    @FXML
    public TextField ciudadTextField;
    @FXML
    public TextField numeroEdificioTextField;
    @FXML
    public PasswordField contrasenaPasswordField;
    @FXML
    public PasswordField confirmarContrasenaPasswordField;
    @FXML
    public Button botonCancelar;
    @FXML
    public Button botonRegistrar;


    public RegistrarUsuarioDlgController(IPersistenciaBazar persistencia) {
        this.persistencia = persistencia;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.puestoChoiceBox.setItems(FXCollections.observableArrayList("ADMIN", "GERENTE", "CAJERO"));
        this.operacionCancelada = false;
        this.puestoChoiceBox.setValue("CAJERO");
    }

    public void registrarUsuario() {

        UsuarioDTO usuarioNuevo = new UsuarioDTO();

        usuarioNuevo.setNombre(this.nombreUsuarioTextField.getText());
        usuarioNuevo.setApellido(this.apellidoUsuarioTextField.getText());
        usuarioNuevo.setPuesto(UsuarioDTO.Puesto.valueOf(this.puestoChoiceBox.getValue()));
        usuarioNuevo.setTelefono(this.telefonoTextField.getText());

        DireccionDTO direccion = new DireccionDTO();
        direccion.setCalle(this.calleDireccionTextField.getText());
        direccion.setCiudad(this.ciudadTextField.getText());
        direccion.setColonia(this.coloniaDireccionTextField.getText());
        direccion.setCodigoPostal(this.codigoPostalTextField.getText());
        direccion.setNumeroEdificio(this.numeroEdificioTextField.getText());

        usuarioNuevo.setDireccion(direccion);

        // TODO: Validar con regexs

        try {
            boolean contrasenasCoinciden = this.contrasenaPasswordField.getText().equals(this.confirmarContrasenaPasswordField.getText());

            if (this.contrasenaPasswordField.getText().length() < 8) {
                throw new PersistenciaBazarException("La contraseña debe tener al menos 8 caracteres");
            }


            if (!contrasenasCoinciden) {
                throw new PersistenciaBazarException("Las contraseñas dadas son diferentes");
            }


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Registrar Usuario");
            alert.setHeaderText("");
            alert.setContentText("¿Desea registrar el usuario en el sistema?");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait().ifPresent(response -> {
                if (response != ButtonType.OK) {
                    operacionCancelada = true;
                }
            });

            if (operacionCancelada) {
                return;
            }

            usuarioNuevo.setContrasena(this.contrasenaPasswordField.getText());

            persistencia.registrarUsuario(usuarioNuevo);

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registrar Usuario");
            alert.setHeaderText("");
            alert.setContentText("Usuario registrado con exito");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();


            this.cerrarDialogo();

        } catch (PersistenciaBazarException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registrar Usuario");
            alert.setHeaderText("");
            alert.setContentText(ex.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }


    public void cerrarDialogo() {
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
