package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProductoVentaTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BuscarProductoDlgController implements Initializable {


    private Stage stage;

    ProductoTableModel productoSeleccionado;

    @FXML
    public Button botonAgregarProducto;
    @FXML
    public VBox panelTablaProductos;
    @FXML
    public TextField nombreProductoTextField;
    @FXML
    public TextField productoSeleccionadoTextField;

    public void setStage(Stage stage) { this.stage = stage; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductoVentaTableView tablaProductos = new ProductoVentaTableView();

        int numColumnas = tablaProductos.getColumns().size();

        // removemos las columnas de la tabla ya que no se utilizaran...
        tablaProductos.getColumns().remove(numColumnas - 1); // se elimina la columna del total
        tablaProductos.getColumns().remove(numColumnas - 2); // se elimina la columna de la cantidad

        // producto seleccionado en la tabla
        productoSeleccionado = null;

        productoSeleccionadoTextField.setText("");

        // cuando se hace click en la tabla
        tablaProductos.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Obtener la fila seleccionada
                ProductoTableModel productoTablaSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

                if (productoTablaSeleccionado == null) {
                    botonAgregarProducto.setDisable(true);
                    return;
                }

                botonAgregarProducto.setDisable(false);
                productoSeleccionado = productoTablaSeleccionado;
                productoSeleccionadoTextField.setText(productoSeleccionado.toString());
            }
        });

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
