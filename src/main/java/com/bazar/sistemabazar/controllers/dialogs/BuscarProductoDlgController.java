package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProductoVentaTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ResourceBundle;

public class BuscarProductoDlgController implements Initializable {

    private Stage stage;

    private ProductoVentaTableModel productoSeleccionado;

    @FXML
    public Button botonAgregarProducto;
    @FXML
    public VBox panelTablaProductos;
    @FXML
    public TextField nombreProductoTextField;
    @FXML
    public TextField productoSeleccionadoTextField;
    @FXML
    public Spinner cantidadProductoSpinner;

    public void setStage(Stage stage) { this.stage = stage; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductoVentaTableView tablaProductos = new ProductoVentaTableView();

        int numColumnas = tablaProductos.getColumns().size();

        // removemos las columnas de la tabla ya que no se utilizaran...
        tablaProductos.getColumns().remove(--numColumnas); // se elimina la columna del total
        tablaProductos.getColumns().remove(--numColumnas); // se elimina la columna de la cantidad
        tablaProductos.getColumns().remove(--numColumnas); // se elimina la columna de la cantidad

        // producto seleccionado en la tabla
        productoSeleccionado = null;

        productoSeleccionadoTextField.setText("");

        // cuando se hace click en la tabla
        tablaProductos.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Obtener la fila seleccionada
                ProductoVentaTableModel productoTablaSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

                if (productoTablaSeleccionado == null) {
                    botonAgregarProducto.setDisable(true);
                    return;
                }

                botonAgregarProducto.setDisable(false);
                productoSeleccionado = productoTablaSeleccionado;
                productoSeleccionadoTextField.setText(
                        String.format(
                                "Producto(ID: %d, Nombre: %s)",
                                productoSeleccionado.getIdProperty().get(),
                                productoSeleccionado.getNombreProperty().get()
                        )
                );
            }
        });

        // se le asigna la cantidad maxima para un producto
        this.cantidadProductoSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1,
                        100
                )
        );


        ObservableList<ProductoVentaTableModel> listaProductos = FXCollections.observableArrayList(
                new ProductoVentaTableModel(12, "Jarra Plastico", 54.50f, 1),
                new ProductoVentaTableModel(13, "Vaso Vidrio", 35.0f, 1),
                new ProductoVentaTableModel(14, "Plato Vidrio Blanco", 40.0f, 1),
                new ProductoVentaTableModel(15, "Plato Plastico", 32.0f, 1),
                new ProductoVentaTableModel(16, "Cesta ropa", 90.50f, 1),
                new ProductoVentaTableModel(17, "Caja Madera 25x25cm", 56.5f, 1),
                new ProductoVentaTableModel(18, "Caja ordenadora madera 25x25", 97.0f, 1),
                new ProductoVentaTableModel(19, "Taza Cafe Java", 50.0f, 1),
                new ProductoVentaTableModel(20, "Taza Cafe Jurassic Park", 60.0f, 1),
                new ProductoVentaTableModel(21, "Calentadera Metal Azul", 125.0f,1)
        );

        tablaProductos.getItems().addAll(listaProductos);
        tablaProductos.setEditable(false);


        panelTablaProductos.getChildren().add(tablaProductos);
    }

    public ProductoTableModel getProductoSeleccionado() {
        return this.productoSeleccionado;
    }

    /**
     * Cierra el dialogo conservando el producto seleccionado
     */
    public void agregarProducto() {
        if (this.productoSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Advertencia - Agregar Producto");
            alert.setContentText("Primero selecciona un producto");
            alert.showAndWait();
            return;
        }

        this.productoSeleccionado.setCantidad((Integer) this.cantidadProductoSpinner.getValue());

        this.stage.close();
    }

    /**
     * Sale del dialogo sin conservar el producto seleccionado
     */
    public void cerrarDialogo() {
        productoSeleccionado = null;
        this.stage.close();
    }


}
