package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProveedoresTableView;
import com.bazar.sistemabazar.components.tables.models.ProveedorTableModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetosNegocio.ProductoDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

// TODO: AVERIGUAR QUE SE PUEDE HACER CON ESTE CONTROLADOR (VER SI ES MEJOR DIVIDIRLO)

public class ProductoDlgController implements Initializable {


    private IPersistenciaBazar persistencia;
    private DialogoOperacion operacion;
    private ProductoDTO producto;

    private Stage stage;

    private static Pattern codigoBarrasPattern = Pattern.compile("^[0-9]{10,15}$");
    private static Pattern codigoInternoPattern = Pattern.compile("^[a-zA-Z0-9?]{5,10}$");
    private static Pattern nombrePattern = Pattern.compile("^[a-zA-Z0-9? ]{2,30}$");

    @FXML
    private Label tituloDialogoLabel;
    @FXML
    private TextField precioProductoTextField;
    @FXML
    public Button restaurarButton;
    @FXML
    public Button cancelarButton;
    @FXML
    public Button aceptarButton;
    @FXML
    public TextField fechaRegistroTextField;
    @FXML
    public TextField nombreProductoTextField;
    @FXML
    public TextField codigoInternoTextField;
    @FXML
    public TextField codigoBarrasTextField;

    public ProductoDlgController(IPersistenciaBazar persistencia, DialogoOperacion operacion, ProductoDTO producto) {
        this.producto = producto;
        this.persistencia = persistencia;
        this.operacion = operacion;
    }

    public ProductoDlgController(IPersistenciaBazar persistencia, DialogoOperacion operacion) {
        this.persistencia = persistencia;
        this.operacion = operacion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.codigoInternoTextField.setText("");
        this.codigoBarrasTextField.setText("");
        this.nombreProductoTextField.setText("");
        this.precioProductoTextField.setText("");
        this.fechaRegistroTextField.setText("");

        this.prepararModoOperacion();

        this.precioProductoTextField.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String nuevoValor) {
                        if (!nuevoValor.matches("\\d*")) {
                            precioProductoTextField.setText(nuevoValor.replaceAll("[^\\d]", ""));
                        }
                    }
                }
        );

    }

    private void prepararInformacionProducto() {

        if (producto != null) {
            this.codigoBarrasTextField.setText(String.valueOf(producto.getCodigoBarras()));
            this.codigoInternoTextField.setText(producto.getCodigoInterno());
            this.nombreProductoTextField.setText(producto.getNombre());
            this.precioProductoTextField.setText(String.valueOf(producto.getPrecio()));
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            this.fechaRegistroTextField.setText(producto.getFechaRegistro().format(formatoFecha));
        }
    }

    public void prepararModoOperacion() {
        switch (this.operacion) {
            case LECTURA -> {
                this.codigoBarrasTextField.setEditable(false);
                this.codigoInternoTextField.setEditable(false);
                this.nombreProductoTextField.setEditable(false);
                this.precioProductoTextField.setEditable(false);
                this.fechaRegistroTextField.setEditable(false);
                this.aceptarButton.setText("Aceptar");
                this.restaurarButton.setDisable(true);
                this.tituloDialogoLabel.setText("Informacion del Producto");
                this.prepararInformacionProducto();
            }
            case REGISTRAR -> {
                this.fechaRegistroTextField.setEditable(false);
                this.fechaRegistroTextField.setDisable(true);
                this.tituloDialogoLabel.setText("Registrar Producto");
            }
            case ACTUALIZAR -> {
                this.codigoBarrasTextField.setEditable(false);
                this.codigoInternoTextField.setEditable(false);
                this.fechaRegistroTextField.setEditable(false);
                this.aceptarButton.setText("Actualizar");
                this.tituloDialogoLabel.setText("Actualizar Producto");
                this.prepararInformacionProducto();
            }
            case ELIMINAR -> {
                this.codigoBarrasTextField.setEditable(false);
                this.codigoInternoTextField.setEditable(false);
                this.nombreProductoTextField.setEditable(false);
                this.precioProductoTextField.setEditable(false);
                this.fechaRegistroTextField.setEditable(false);
                // deshabilitar botones para agregar o eliminar proveedor.
                this.aceptarButton.setText("Eliminar");
                this.restaurarButton.setDisable(true);
                this.tituloDialogoLabel.setText("Eliminar Producto");
                this.prepararInformacionProducto();
            }
        }
    }

    private void validarCampos() throws PersistenciaBazarException {
        boolean codigoBarrasValido = codigoBarrasPattern.matcher(codigoBarrasTextField.getText()).matches();
        if (!codigoBarrasValido) {
            throw new PersistenciaBazarException("Codigo de barras no valido");
        }

        boolean codigoInternoValido = codigoInternoPattern.matcher(codigoInternoTextField.getText()).matches();
        if (!codigoInternoValido) {
            throw new PersistenciaBazarException("Codigo interno no valido");
        }

        boolean nombreValido = nombrePattern.matcher(nombreProductoTextField.getText()).matches();
        if (!nombreValido) {
            throw new PersistenciaBazarException("Nombre del producto no valido");
        }

        // TODO: validar precio
    }

    public ProductoDTO getProducto() {
        ProductoDTO p = new ProductoDTO();

        p.setCodigoBarras(Long.parseLong(this.codigoBarrasTextField.getText()));
        p.setCodigoInterno(this.codigoInternoTextField.getText());
        p.setNombre(this.nombreProductoTextField.getText());
        p.setPrecio(Float.parseFloat(this.precioProductoTextField.getText()));

        if (this.operacion != DialogoOperacion.ACTUALIZAR) {
            p.setFechaRegistro(LocalDateTime.now());
        }

        return p;
    }

    private void registrar() throws PersistenciaBazarException {
        ProductoDTO producto = this.getProducto();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Registrar Producto");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea registrar el producto?");

        // Mostrar el diálogo y esperar la respuesta del usuario
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                System.out.println("### registrando producto...");
            } else {
                return;
            }
        });

        persistencia.registrarProducto(producto);
    }

    private void actualizar() throws PersistenciaBazarException {
        ProductoDTO producto = this.getProducto();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Actualizar Producto");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea actualizar los datos del producto?");

        // Mostrar el diálogo y esperar la respuesta del usuario
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                System.out.println("### actualizando producto...");
            } else {
                return;
            }
        });

        persistencia.actualizarProducto(producto);
    }

    private void eliminar() throws PersistenciaBazarException {
        ProductoDTO producto = this.getProducto();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Producto");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea eliminar producto?");

        // Mostrar el diálogo y esperar la respuesta del usuario
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                System.out.println("### eliminado producto...");
            } else {
                return;
            }
        });

        persistencia.eliminarProducto(producto.getCodigoInterno());
    }

    public void ejecutarOperacion() {
        try {

            this.validarCampos();

            switch (this.operacion) {
                case LECTURA -> {
                    this.stage.close();
                    return;
                }
                case REGISTRAR -> {
                    this.registrar();
                }
                case ACTUALIZAR -> {
                    this.actualizar();
                }
                case ELIMINAR -> {
                    this.eliminar();
                }
            }

            this.stage.close();
        } catch (PersistenciaBazarException ex) {
            // TODO: muestra dialogo
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String titulo = String.format("%s PRODUCTO", this.operacion.toString());
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
//            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    public void restaurarCambios() {
        // TODO
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void cerrarDialogo(ActionEvent actionEvent) {
        this.stage.close();
    }
}
