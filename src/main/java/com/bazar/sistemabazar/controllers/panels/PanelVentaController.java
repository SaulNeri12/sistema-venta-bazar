package com.bazar.sistemabazar.controllers.panels;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.components.tables.DetallesVentaTableView;
import com.bazar.sistemabazar.components.tables.models.DetalleVentaTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.controllers.dialogs.BuscarProductoDlgController;
import com.bazar.sistemabazar.controllers.dialogs.ConfirmarVentaDlgController;
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
import objetosNegocio.DetalleVentaDTO;
import objetosNegocio.ProductoDTO;
import objetosNegocio.UsuarioDTO;
import objetosNegocio.VentaDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

// [x] (solucionado, sin hacer nada...) TODO: OCURRE UN ERROR EXTRANO CUANDO SE QUITAN PRODUCTOS DE LA VENTA, SIGUEN CONSERVANDO
// LA CANTIDAD DE PRODUCTOS QUE HABIA ANTES DE QUE SE REMOVIERAN DE LA TABLA DE DETALLLES DE VENTA,
// HACIENDO QUE EL TOTAL DEL PRODUCTO COMPRADO SE VEA AFECTADO...

public class PanelVentaController implements Initializable {


    private Float totalAPagar;

    private IPersistenciaBazar persistencia;
    private UsuarioDTO usuario;

    private DetallesVentaTableView tablaVenta;

    @FXML
    public VBox tablaVentaPane;
    public Button botonBuscarProducto;
    public TextField campoIndicadorTotal;
    public Button finalizarCompraBoton;

    @FXML
    private AnchorPane controlVentaAnchorPane;


    public PanelVentaController(IPersistenciaBazar persistencia, UsuarioDTO usuario) {
        totalAPagar = 0.0f;
        this.persistencia = persistencia;
        this.usuario = usuario;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // la tabla principal de ventas se anade al panel de venta
        DetallesVentaTableView tablaVenta = new DetallesVentaTableView();
        this.tablaVenta = tablaVenta;

        this.tablaVenta.setPlaceholder(new Label(""));

        this.finalizarCompraBoton.setDisable(true);

        tablaVentaPane.getChildren().add(tablaVenta); // se anade al frame...

        // dado que no encontre una manera facil de hacer que se actualice el indicador del total
        // en cada evento, llegue a esta solucion. Hace que ese indicador se redibuje y tenga un
        // nuevo valor cada 100 millisegundos.
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if (tablaVenta.getItems().isEmpty()) {
                this.finalizarCompraBoton.setDisable(true);
            } else {
                this.finalizarCompraBoton.setDisable(false);
            }
            this.calcularTotalCompra();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void mostrarDialogoBusquedaProducto(ActionEvent actionEvent) {
        FXMLLoader buscarProductoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/BuscarProductoDlg.fxml"));

        buscarProductoDlgFxmlLoader.setControllerFactory(c -> {
            return new BuscarProductoDlgController(persistencia);
        });

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
            ProductoDTO productoSeleccionado= buscarProductoController.getProductoSeleccionado();

            if (productoSeleccionado == null) {
                return;
            }

            ObservableList<DetalleVentaTableModel> productosTabla = tablaVenta.getItems();

            DetalleVentaDTO productoVenta = new DetalleVentaDTO();
            productoVenta.setProducto(productoSeleccionado);
            productoVenta.setPrecioProducto(productoSeleccionado.getPrecio());
            productoVenta.setCantidad(buscarProductoController.getCantidadProductos());

            tablaVenta.agregarFilaObjeto(productoVenta);

            this.calcularTotalCompra();

            //tablaVenta.getItems().add(productoSeleccionado);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error - Buscar producto");
            alert.setHeaderText("");
            alert.setContentText("No se pudo realizar la accion");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    this.tablaVenta.getItems().remove(0, tablaVenta.getItems().size());
                }
            });
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

    /**
     * Actualiza el total a pagar por la compra asi como el campo que muestra
     * el total.
     */
    private void calcularTotalCompra() {
        ObservableList<DetalleVentaTableModel> productos = tablaVenta.getItems();

        Float total = 0.0f;

        if (!productos.isEmpty()) {
            for (DetalleVentaTableModel prdct: productos) {
                total += prdct.getTotalProperty().get();
            }
        }

        totalAPagar = total;

        this.campoIndicadorTotal.setText(totalAPagar.toString());
    }

    @FXML
    public void registrarVenta() {
        FXMLLoader confirmarVentaDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/ConfirmarVentaDlg.fxml"));
        Parent root = null;

        confirmarVentaDlgFxmlLoader.setControllerFactory(c -> {
            return new ConfirmarVentaDlgController(this.totalAPagar);
        });

        try {
            root = confirmarVentaDlgFxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
            // TODO
        }

        ConfirmarVentaDlgController confirmarVentaController = confirmarVentaDlgFxmlLoader.getController();
        Stage confirmarVentaDlgStage = new Stage();
        confirmarVentaController.setStage(confirmarVentaDlgStage);
        confirmarVentaDlgStage.setScene(new Scene(root));
        confirmarVentaDlgStage.setTitle("Confirmar Venta");
        confirmarVentaDlgStage.initModality(Modality.APPLICATION_MODAL);
        confirmarVentaDlgStage.showAndWait();

        List<DetalleVentaDTO> productosDetalleVenta = new ArrayList<>();

        for (DetalleVentaTableModel detalleVentaEnTabla: tablaVenta.getItems()) {
            try {
                ProductoDTO producto = persistencia.consultarProductoPorCodigoInterno(detalleVentaEnTabla.getCodigoProperty().get());

                if (producto == null) {
                    throw new PersistenciaBazarException("Ocurrio un error al buscar el detalle del producto");
                }

                DetalleVentaDTO detalleVenta = new DetalleVentaDTO();
                detalleVenta.setProducto(producto);
                detalleVenta.setCantidad(detalleVentaEnTabla.getCantidadProperty().get());
                detalleVenta.setPrecioProducto(producto.getPrecio());

                productosDetalleVenta.add(detalleVenta);

            } catch (PersistenciaBazarException e) {
                throw new RuntimeException(e);
            }
        }

        VentaDTO venta = new VentaDTO();

        // NOTE: PARA SIMULACION...
        Random random = new Random();

        venta.setId(random.nextLong() & Long.MAX_VALUE);

        venta.setNombreCliente(confirmarVentaController.getNombreCliente());
        venta.setApellidoCliente(confirmarVentaController.getApellidoCliente());
        venta.setMetodoPago(confirmarVentaController.getMetodoPago());
        venta.setProductosVendidos(productosDetalleVenta);
        venta.setUsuario(this.usuario);

        try {
            persistencia.registrarVenta(venta);
        } catch (PersistenciaBazarException e) {
            throw new RuntimeException(e);
            // TODO: MANEJAR ERROR CON DIALOGO
        }

        this.tablaVenta.eliminarTodasLasFilas();
    }

}
