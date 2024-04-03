package com.bazar.sistemabazar.controllers.panels;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.components.tables.DetallesVentaTableView;
import com.bazar.sistemabazar.components.tables.models.DetalleVentaTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
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
import objetosNegocio.DetalleVenta;
import objetosNegocio.Producto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PanelVentaController implements Initializable {

    private DetallesVentaTableView tablaVenta;

    @FXML
    public VBox tablaVentaPane;
    public Button botonBuscarProducto;
    public TextField campoIndicadorTotal;

    @FXML
    private AnchorPane controlVentaAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // la tabla principal de ventas se anade al panel de venta
        DetallesVentaTableView tablaVenta = new DetallesVentaTableView();
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
            ProductoTableModel productoSeleccionadoTabla = buscarProductoController.getProductoSeleccionado();

            if (productoSeleccionadoTabla == null) {
                return;
            }

            Producto productoSeleccionado = new Producto();
            productoSeleccionado.setCodigo(productoSeleccionadoTabla.getCodigoProperty().get());
            productoSeleccionado.setNombre(productoSeleccionadoTabla.getNombreProperty().get());
            productoSeleccionado.setPrecio(productoSeleccionadoTabla.getPrecioProperty().get());

            ObservableList<DetalleVentaTableModel> productosTabla = tablaVenta.getItems();

            DetalleVenta productoVenta = new DetalleVenta();
            productoVenta.setProducto(productoSeleccionado);
            productoVenta.setPrecioProducto(productoSeleccionado.getPrecio());
            productoVenta.setCantidad(buscarProductoController.getCantidadProductos());

            tablaVenta.agregarFilaObjeto(productoVenta);

            /*
            // si el producto ya se encuentra en la tabla, solo se actualiza la cantidad de productos
            for (DetalleVentaTableModel p: productosTabla) {
                boolean encontrado = p.getCodigoProperty().get() == productoSeleccionado.getCodigoProperty().get();
                if (encontrado) {
                    int cantidadNueva = p.getCantidadProperty().get() + productoSeleccionado.getCantidadProperty().get();
                    p.setCantidad(cantidadNueva);
                    campoIndicadorTotal.setText(Float.toString(this.calcularTotalCompra()));
                    return;
                }
            }

             */

            campoIndicadorTotal.setText(Float.toString(this.calcularTotalCompra()));

            //tablaVenta.getItems().add(productoSeleccionado);
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
        ObservableList<DetalleVentaTableModel> productos = tablaVenta.getItems();

        if (productos.isEmpty()) {
            return 0.0f;
        }

        float total = 0.0f;

        for (DetalleVentaTableModel prdct: productos) {
            total += prdct.getTotalProperty().get();
        }

        return total;
    }
}
