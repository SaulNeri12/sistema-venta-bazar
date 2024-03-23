package com.bazar.sistemabazar.controllers.panels;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.components.tables.ProductoVentaTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import com.bazar.sistemabazar.controllers.dialogs.BuscarProductoDlgController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class PanelVentaController implements Initializable {

    @FXML
    public TabPane ventasTabPane;
    public VBox tablaVentaPane;
    public Button botonBuscarProducto;

    @FXML
    private AnchorPane controlVentaAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // la tabla principal de ventas se anade al panel de venta
        ProductoVentaTableView tablaVenta = new ProductoVentaTableView();
        tablaVentaPane.getChildren().add(tablaVenta);
    }

    @FXML
    public void mostrarDialogoBusquedaProducto(ActionEvent actionEvent) {
        FXMLLoader buscarProductoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/BuscarProductoDlg.fxml"));
        Parent root = null;
        try {
            root = buscarProductoDlgFxmlLoader.load();

            Stage buscarProductoStage = new Stage();
            buscarProductoStage.setScene(new Scene(root));

            BuscarProductoDlgController buscarProductoController = buscarProductoDlgFxmlLoader.getController();
            buscarProductoController.setStage(buscarProductoStage);

            buscarProductoStage.setTitle("Buscar Producto");
            buscarProductoStage.initModality(Modality.APPLICATION_MODAL);
            buscarProductoStage.showAndWait();

            /* logica importante */
            ProductoVentaTableModel productoSeleccionado = (ProductoVentaTableModel) buscarProductoController.getProductoSeleccionado();

            if (productoSeleccionado == null) {
                return;
            }

            TableView tablaVenta = (TableView) tablaVentaPane.getChildren().get(0);

            ObservableList<ProductoVentaTableModel> productosTabla = tablaVenta.getItems();

            // si el producto ya se encuentra en la tabla, solo se actualiza la cantidad de productos
            for (ProductoVentaTableModel p: productosTabla) {
                boolean encontrado = p.getIdProperty().get() == productoSeleccionado.getIdProperty().get();
                if (encontrado) {
                    int cantidadNueva = p.getCantidadProperty().get() + productoSeleccionado.getCantidadProperty().get();
                    p.setCantidad(cantidadNueva);
                    return;
                }
            }

            tablaVenta.getItems().add(productoSeleccionado);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void mostrarDialogoEliminarProducto() {
        TextInputDialog productoIdDlg = new TextInputDialog();
        productoIdDlg.setTitle("Eliminar Producto");
        productoIdDlg.setHeaderText("Ingresa el ID del producto a eliminar de la venta:");

        productoIdDlg.showAndWait().ifPresent(numero -> {
            Integer productoId = Integer.valueOf(numero);

            TableView tablaProductosVenta = (TableView) this.tablaVentaPane.getChildren().get(0);

            ObservableList<ProductoVentaTableModel> productos = tablaProductosVenta.getItems();

            System.out.println(productos.toString());

            for (ProductoVentaTableModel producto: productos) {
                if (producto.getIdProperty().get() == productoId) {
                    tablaProductosVenta.getItems().remove(producto);
                    return;
                }
            }
        });



    }
}
