package com.bazar.sistemabazar.controllers.panels;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.components.tables.ProductosVentaTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import com.bazar.sistemabazar.controllers.dialogs.BuscarProductoDlgController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PanelVentaController implements Initializable {

    private ProductosVentaTableView tablaVenta;

    @FXML
    public VBox tablaVentaPane;
    public Button botonBuscarProducto;
    public TextField campoIndicadorTotal;

    @FXML
    private AnchorPane controlVentaAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // la tabla principal de ventas se anade al panel de venta
        ProductosVentaTableView tablaVenta = new ProductosVentaTableView();
        this.tablaVenta = tablaVenta;

        this.tablaVenta.setPlaceholder(new Label(""));

        tablaVentaPane.getChildren().add(tablaVenta); // se anade al frame...

        // dado que no encontre una manera facil de hacer que se actualice el indicador del total
        // en cada evento, llegue a esta solucion. Hace que ese indicador se redibuje y tenga un
        // nuevo valor cada 100 millisegundos.
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            campoIndicadorTotal.setText(Float.toString(this.calcularTotalCompra()));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void mostrarDialogoBusquedaProducto(ActionEvent actionEvent) {
        FXMLLoader buscarProductoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/BuscarProductoDlg.fxml"));
        Parent root = null;
        try {
            root = buscarProductoDlgFxmlLoader.load();

            Stage buscarProductoStage = new Stage();
            buscarProductoStage.setScene(new Scene(root));

            // obtenemos el controlador del frame
            BuscarProductoDlgController buscarProductoController = buscarProductoDlgFxmlLoader.getController();
            buscarProductoController.setStage(buscarProductoStage);

            // configuracion basica para el nuevo frame...
            buscarProductoStage.setTitle("Buscar Producto");
            buscarProductoStage.initModality(Modality.APPLICATION_MODAL);
            buscarProductoStage.showAndWait();

            /* logica importante */
            ProductoVentaTableModel productoSeleccionado = (ProductoVentaTableModel) buscarProductoController.getProductoSeleccionado();

            if (productoSeleccionado == null) {
                return;
            }

            ObservableList<ProductoVentaTableModel> productosTabla = tablaVenta.getItems();

            // si el producto ya se encuentra en la tabla, solo se actualiza la cantidad de productos
            for (ProductoVentaTableModel p: productosTabla) {
                boolean encontrado = p.getIdProperty().get() == productoSeleccionado.getIdProperty().get();
                if (encontrado) {
                    int cantidadNueva = p.getCantidadProperty().get() + productoSeleccionado.getCantidadProperty().get();
                    p.setCantidad(cantidadNueva);
                    campoIndicadorTotal.setText(Float.toString(this.calcularTotalCompra()));
                    return;
                }
            }

            campoIndicadorTotal.setText(Float.toString(this.calcularTotalCompra()));

            tablaVenta.getItems().add(productoSeleccionado);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void cancelarCompra() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancelar Compra");
        alert.setHeaderText("Desea cancelar la compra?");
        alert.setContentText("Todos los productos listos para procesar la compra seran eliminados de la tabla");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.tablaVenta.getItems().remove(0, tablaVenta.getItems().size());
            }
        });
    }

    public float calcularTotalCompra() {
        ObservableList<ProductoVentaTableModel> productos = tablaVenta.getItems();

        if (productos.isEmpty()) {
            return 0.0f;
        }

        float total = 0.0f;

        for (ProductoVentaTableModel prdct: productos) {
            total += prdct.getTotalProperty().get();
        }

        return total;
    }
}
