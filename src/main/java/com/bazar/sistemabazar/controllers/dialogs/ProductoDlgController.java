package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProveedoresTableView;
import com.bazar.sistemabazar.components.tables.models.ProveedorTableModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.layout.Pane;
import objetosNegocio.Producto;
import objetosNegocio.Proveedor;

// TODO: AVERIGUAR QUE SE PUEDE HACER CON ESTE CONTROLADOR (VER SI ES MEJOR DIVIDIRLO)

public class ProductoDlgController implements Initializable {


    private Producto producto;
    private DialogoOperacion modoOperacion;
    private ProveedoresTableView tablaProveedores;

    @FXML
    private TextField precioProductoTextField;
    public Button restaurarButton;
    public Button cancelarButton;
    public Button aceptarButton;
    public TextArea descripcionProductoTextArea;
    public TextField fechaRegistroTextField;
    public TextField nombreProductoTextField;
    public TextField codigoInternoTextField;
    public TextField codigoBarrasTextField;
    public Pane tablaProveedoresPane;

    /*
    public ProductoDlgController(IGestorProductos gestorProductos, IGestorProveedores ) {

    }*/


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.codigoInternoTextField.setText("");
        this.codigoBarrasTextField.setText("");
        this.nombreProductoTextField.setText("");
        this.precioProductoTextField.setText("");
        this.fechaRegistroTextField.setText("");
        this.descripcionProductoTextArea.setText("");

        UnaryOperator<TextFormatter.Change> filtroNumeros = change -> {
            String entrada = change.getControlNewText();
            Pattern pattern = Pattern.compile("-?\\d*\\.?\\d*");
            boolean entradaValida = pattern.matcher(entrada).matches();
            if (entradaValida) {
                return change;
            } else {
                return null; // Rechazar el cambio
            }
        };

        this.precioProductoTextField.setTextFormatter((TextFormatter<?>) filtroNumeros);

        //this.prepararModoOperacion();

        ProveedoresTableView tablaProveedores = new ProveedoresTableView();
        this.tablaProveedores = tablaProveedores;
        this.tablaProveedoresPane.getChildren().add(tablaProveedores);
    }



    /*
    @Override
    public void prepararModoOperacion() {
         switch (modoOperacion) {
             case LECTURA -> {
                 this.codigoBarrasTextField.setEditable(false);
                 this.codigoInternoTextField.setEditable(false);
                 this.nombreProductoTextField.setEditable(false);
                 this.descripcionProductoTextArea.setEditable(false);
                 this.precioProductoTextField.setEditable(false);
                 this.fechaRegistroTextField.setEditable(false);
                 this.aceptarButton.setText("Aceptar");
                 this.restaurarButton.setDisable(true);

                 // deshabilitar botones para agregar o eliminar proveedor.
             }
             case REGISTRAR -> {
                 this.fechaRegistroTextField.setEditable(false);
                 this.fechaRegistroTextField.setVisible(false);
             }
             case ACTUALIZAR -> {
                 this.codigoBarrasTextField.setEditable(false);
                 this.codigoInternoTextField.setEditable(false);
                 this.fechaRegistroTextField.setEditable(false);
                 this.aceptarButton.setText("Actualizar");
             }
             case ELIMINAR -> {
                 this.codigoBarrasTextField.setEditable(false);
                 this.codigoInternoTextField.setEditable(false);
                 this.nombreProductoTextField.setEditable(false);
                 this.descripcionProductoTextArea.setEditable(false);
                 this.precioProductoTextField.setEditable(false);
                 this.fechaRegistroTextField.setEditable(false);
                 // deshabilitar botones para agregar o eliminar proveedor.
                 this.aceptarButton.setText("Eliminar");
                 this.restaurarButton.setDisable(true);

             }
        }
    }


    @Override
    public DialogoOperacion obtenerModoOperacion() {
        return this.modoOperacion;
    }

    @Override
    public void guardarCambios() {
        this.producto = new Producto();

        this.producto.setId(Long.valueOf(this.codigoBarrasTextField.getId()));
        this.producto.setNombre(this.nombreProductoTextField.getText());
        this.producto.setCodigo(this.codigoInternoTextField.getText());

        Float precio;

        try {
            precio = Float.parseFloat(this.precioProductoTextField.getText());
        } catch (Exception e) {
            System.out.println("# ERROR EN EL PRECIO DEL PRODUCTO");
            return;
        }

        this.producto.setPrecio(precio);

        String fechaString = this.fechaRegistroTextField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaRegistro = LocalDateTime.parse(fechaString, formatter);

        this.producto.setFechaRegistro(fechaRegistro);

        List<ProveedorTableModel> proveedores = this.tablaProveedores.getItems().stream().toList();

        for (ProveedorTableModel p: proveedores){
            Proveedor proveedor = new Proveedor();

            proveedor.setId((long) p.getIdProperty().get());
            proveedor.setNombre(p.getNombreProperty().get());
            proveedor.setTelefono(p.getTelefonoProperty().get());

            this.producto.obtenerProveedores().add(proveedor);
        }
    }

    @Override
    public void restaurarCambios() {
        // TODO: Pendiente
    }

    @Override
    public Producto obtenerObjeto() {
        return this.producto;
    }

    @Override
    public void asignarObjeto(Producto producto) {
        if (modoOperacion != DialogoOperacion.REGISTRAR) {
            this.producto = producto;

            this.codigoBarrasTextField.setText(producto.getId().toString());
            this.codigoInternoTextField.setText(producto.getCodigo());
            this.nombreProductoTextField.setText(producto.getNombre());
            this.precioProductoTextField.setText(Float.toString(producto.getPrecio()));
        }
    }*/
}
