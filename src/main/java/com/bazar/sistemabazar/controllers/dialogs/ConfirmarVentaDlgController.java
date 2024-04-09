package com.bazar.sistemabazar.controllers.dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetosNegocio.Usuario;
import persistencia.IPersistenciaBazar;

import java.net.URL;
import java.util.IllegalFormatException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmarVentaDlgController implements Initializable {

    private Stage stage;

    @FXML
    public TextField nombreClienteTextField;
    public Label apellidoClienteTextField;
    public ChoiceBox metodoPagoChoiceBox;
    public TextField montoTotalTextField;
    public Spinner campoMontoPagoSpinner;
    public TextField campoCambioTextField;
    public Button completarVentaBoton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.campoCambioTextField.setEditable(false);
        this.montoTotalTextField.setEditable(false);

        this.metodoPagoChoiceBox = new ChoiceBox<String>();
        this.metodoPagoChoiceBox.setItems(FXCollections.observableArrayList(
                "EFECTIVO",
                "TARJETA"
        ));

        this.campoMontoPagoSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer i, Integer nuevoValor) {
                Float montoTotalCompra = Float.valueOf(montoTotalTextField.getText());

                completarVentaBoton.setDisable(false);

                if (nuevoValor < montoTotalCompra) {
                    completarVentaBoton.setDisable(true);
                }
            }
        });

        this.campoMontoPagoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1,
                100_000
        ));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void completarVenta() {
        Pattern patronNombreApellido = Pattern.compile("^[a-zA-Z\\s]{2,30}");

        String nombreCliente = this.nombreClienteTextField.getText();
        String apellidoCliente = this.apellidoClienteTextField.getText();

        try {

            if (nombreCliente.isEmpty() || apellidoCliente.isEmpty()) {
                throw new IllegalArgumentException("Rellene todos los cambios para poder continuar");
            }

            boolean nombreValido = patronNombreApellido.matcher(this.nombreClienteTextField.getText()).matches();
            boolean apellidoValido = patronNombreApellido.matcher(this.nombreClienteTextField.getText()).matches();

            if (!nombreValido || !apellidoValido) {
                throw new IllegalArgumentException("El nombre y appelido del cliente no debe contener numeros ni simbolos");
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Compra");
            alert.setHeaderText("");
            alert.setContentText("Desea finalizar la venta?");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait().ifPresent(response -> {
                if (response != ButtonType.OK) {
                    return;
                }
            });

            this.cerrarDialogo();

        } catch (IllegalArgumentException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Confirmar Compra");
            alert.setHeaderText("");
            alert.setContentText(ex.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        }
    }

    @FXML
    public void cerrarDialogo() {
        this.stage.close();
    }


}
