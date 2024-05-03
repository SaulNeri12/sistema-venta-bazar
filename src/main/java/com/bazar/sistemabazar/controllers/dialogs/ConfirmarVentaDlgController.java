package com.bazar.sistemabazar.controllers.dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetosNegocio.UsuarioDTO;
import objetosNegocio.VentaDTO;
import objetosNegocio.VentaDTO;
import persistencia.IPersistenciaBazar;

import java.net.URL;
import java.util.IllegalFormatException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmarVentaDlgController implements Initializable {


    private String nombreCliente;
    private String apellidoCliente;
    private VentaDTO.MetodoPago metodoPago;
    private Float montoTotal;

    private Stage stage;

    @FXML
    public TextField nombreClienteTextField;
    public TextField apellidoClienteTextField;
    public ChoiceBox metodoPagoChoiceBox;
    public TextField montoTotalTextField;
    public TextField campoMontoPagoTextField;
    public TextField campoCambioTextField;
    public Button completarVentaBoton;

    public ConfirmarVentaDlgController(Float montoTotal) {
        this.montoTotal = montoTotal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.nombreCliente = "";
        this.apellidoCliente = "";
        this.metodoPago = VentaDTO.MetodoPago.EFECTIVO;

        this.campoCambioTextField.setEditable(false);
        this.montoTotalTextField.setEditable(false);
        this.montoTotalTextField.setText(this.montoTotal.toString());
        this.completarVentaBoton.setDisable(false);

        this.metodoPagoChoiceBox.setItems(FXCollections.observableArrayList(
                "EFECTIVO",
                "TARJETA"
        ));


        this.campoMontoPagoTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String nuevoValor) {
                if (!nuevoValor.matches("\\d*")) {
                    campoMontoPagoTextField.setText(nuevoValor.replaceAll("[^\\d]", ""));
                }

                Float cantidadPago = Float.valueOf(campoMontoPagoTextField.getText());
                Float cambioCalculado = cantidadPago - montoTotal;
                if (cambioCalculado >= 0) {
                    completarVentaBoton.setDisable(false);
                    campoCambioTextField.setText(cambioCalculado.toString());
                } else {
                    completarVentaBoton.setDisable(true);
                    campoCambioTextField.setText("");
                }
            }
        });

        this.campoMontoPagoTextField.setEditable(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    @FXML
    public void completarVenta() {
        Pattern patronNombreApellido = Pattern.compile("^[a-zA-Z?\\s]{2,30}");

        this.nombreCliente = this.nombreClienteTextField.getText();
        this.apellidoCliente = this.apellidoClienteTextField.getText();

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

            String metodoPagoSeleccionado = (String) this.metodoPagoChoiceBox.getSelectionModel().getSelectedItem();

            if (metodoPagoSeleccionado.equals(VentaDTO.MetodoPago.EFECTIVO.toString())) {
                this.metodoPago = VentaDTO.MetodoPago.EFECTIVO;
            }
            else if (metodoPagoSeleccionado.equals(VentaDTO.MetodoPago.TARJETA.toString())) {
                this.metodoPago = VentaDTO.MetodoPago.TARJETA;
            }

            Float montoPagoVenta = Float.valueOf(this.campoMontoPagoTextField.getText());
            Float totalAPagar = Float.valueOf(this.montoTotalTextField.getText());
            if (montoPagoVenta < totalAPagar) {
                this.completarVentaBoton.setDisable(true);
                return;
            }

            this.completarVentaBoton.setDisable(false);

            this.cerrarDialogo();

        } catch (IllegalArgumentException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Confirmar Compra");
            alert.setHeaderText("");
            alert.setContentText(ex.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }

    public String getApellidoCliente() {
        return this.apellidoCliente;
    }

    public VentaDTO.MetodoPago getMetodoPago() {
        return this.metodoPago;
    }

    @FXML
    public void cerrarDialogo() {
        this.stage.close();
    }
}
