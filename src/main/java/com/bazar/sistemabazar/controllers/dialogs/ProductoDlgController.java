package com.bazar.sistemabazar.controllers.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import objetosNegocio.Producto;

public class ProductoDlgController implements Initializable, IControlDialogo<Producto> {


    public TextField precioProductoTextField;
    private Producto producto;

    @FXML
    public Button restaurarButton;
    public Button cancelarButton;
    public Button aceptarButton;
    public TextArea descripcionProductoTextArea;
    public TextField fechaRegistroTextField;
    public TextField nombreProductoTextField;
    public TextField codigoInternoTextField;
    public TextField codigoBarrasTextField;
    public Pane tablaProveedoresPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void guardarCambios() {

    }

    @Override
    public void restaurarCambios() {

    }

    @Override
    public Producto getObjeto() {
        return this.producto;
    }

    @Override
    public void setObjeto(Producto producto) {
        this.producto = producto;

        this.codigoBarrasTextField.setText(producto.getId().toString());
        this.codigoInternoTextField.setText(producto.getCodigo());
        this.nombreProductoTextField.setText(producto.getNombre());
        this.precioProductoTextField.setText(Float.toString(producto.getPrecio()));


    }
}
