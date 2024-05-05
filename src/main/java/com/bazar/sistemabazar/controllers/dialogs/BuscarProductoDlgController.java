package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.components.tables.ProductosTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetosNegocio.ProductoDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BuscarProductoDlgController implements Initializable {

    private ObservableList<ProductoTableModel> listaProductos;

    private IPersistenciaBazar persistencia;

    private Timer timerBusquedaProducto;

    private Stage stage;
    private ProductosTableView tablaProductos;

    private ProductoTableModel productoSeleccionado;
    private ProductoDTO objetoProducto;

    @FXML
    public Button botonCerrarDialogo;
    @FXML
    public Button botonVerDetallesProducto;
    @FXML
    public Button botonModificarProducto;
    @FXML
    public Button botonEliminarProducto;
    @FXML
    public VBox panelTablaProductos;
    @FXML
    public TextField nombreProductoTextField;

    public void setStage(Stage stage) { this.stage = stage; }

    public BuscarProductoDlgController(IPersistenciaBazar persistencia) {
        this.persistencia = persistencia;
        objetoProducto = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductosTableView tablaProductos = new ProductosTableView();

        // producto seleccionado en la tabla
        productoSeleccionado = null;

        timerBusquedaProducto = new Timer();

        this.botonVerDetallesProducto.setDisable(true);
        this.botonModificarProducto.setDisable(true);
        this.botonEliminarProducto.setDisable(true);

        tablaProductos.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Obtener la fila seleccionada
                ProductoTableModel productoTablaSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

                if (productoTablaSeleccionado == null) {
                    botonVerDetallesProducto.setDisable(true);
                    botonModificarProducto.setDisable(true);
                    botonEliminarProducto.setDisable(true);
                    return;
                }

                productoSeleccionado = productoTablaSeleccionado;

                botonVerDetallesProducto.setDisable(false);
                botonModificarProducto.setDisable(false);
                botonEliminarProducto.setDisable(false);
            }
        });

        nombreProductoTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String nuevoString) {
                // Cancelar el temporizador existente si hay uno
                timerBusquedaProducto.cancel();
                timerBusquedaProducto = new Timer();

                timerBusquedaProducto.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        filtrarProductosPorNombre(nuevoString.toLowerCase());
                    }
                }, 100);
            }
        });

        this.actualizarTabla(tablaProductos);

        this.tablaProductos = tablaProductos;

        panelTablaProductos.getChildren().add(tablaProductos);
    }

    private void actualizarTabla(ProductosTableView tablaProductos) {
        try {
            List<ProductoDTO> productosEnSistema = persistencia.consultarProductosTodos();

            // cargar todos los productos en la tabla desde la persistencia...
            this.listaProductos = FXCollections.observableArrayList();

            for (ProductoDTO producto: productosEnSistema) {
                ProductoTableModel productoFilaTabla = new ProductoTableModel(producto);
                this.listaProductos.add(productoFilaTabla);
            }

            tablaProductos.getItems().addAll(this.listaProductos);
            tablaProductos.setEditable(false);

        } catch (PersistenciaBazarException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Consultar Productos");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());

            // Mostrar el diálogo y esperar la respuesta del usuario
            alert.showAndWait();
        }
    }

    public ProductoDTO getProductoSeleccionado() {
        return this.objetoProducto;
    }

    /**
     * Filtra los elementos de la tabla del dialogo por su nombre.
     * @param nombreProducto Nombre del producto a buscar
     */
    public void filtrarProductosPorNombre(String nombreProducto) {
        ObservableList<ProductoTableModel> productos = listaProductos;

        if (nombreProducto == null || nombreProducto.isEmpty()) {
            tablaProductos.getItems().remove(0, tablaProductos.getItems().size());
            // se agregan todos los elementos encontrados cuando la entrada del
            // nombre del producto es null o esta vacia...
            for (ProductoTableModel p: productos) {
                tablaProductos.getItems().add(p);
            }
            return;
        }

        // cada producto el cual contiene en su nombre, el texto almacenado en
        // 'nombreProducto' es anadido a la tabla ya que cumple con la busqueda
        for (ProductoTableModel prdct: productos) {
            if (!prdct.getNombreProperty().get().toLowerCase().contains(nombreProducto)) {
                tablaProductos.getItems().remove(prdct);
            }
        }
    }

    public void verDetallesProducto() {
        ProductoDTO productoObjetivo;

        if (productoSeleccionado == null) {
            return;
        }

        String codigoInterno = productoSeleccionado.getCodigoProperty().get();

        try {
            productoObjetivo = persistencia.consultarProductoPorCodigoInterno(codigoInterno);

            FXMLLoader productoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/ProductoDlg.fxml"));
            productoDlgFxmlLoader.setControllerFactory(c -> {
                return new ProductoDlgController(persistencia, DialogoOperacion.LECTURA, productoObjetivo);
            });

            Parent root = null;

            try {
                root = productoDlgFxmlLoader.load();
            } catch (IOException e) {
                // TODO: Manejar
                throw new RuntimeException(e);
            }

            Stage productoDlgStage = new Stage();

            ProductoDlgController productoDlgController = productoDlgFxmlLoader.getController();
            productoDlgController.setStage(productoDlgStage);

            Scene scene = new Scene(root);
            productoDlgStage.setMaximized(false);
            productoDlgStage.setResizable(false);
            productoDlgStage.setScene(scene);
            // NOTE: Cambiar segun operacion...
            productoDlgStage.setTitle("Detalles del producto");
            productoDlgStage.initModality(Modality.APPLICATION_MODAL);
            productoDlgStage.show();

        } catch (PersistenciaBazarException e) {
            // TODO: asasasas
            throw new RuntimeException(e);
        }
    }

    public void modificarProducto() {
        ProductoDTO productoObjetivo;

        if (productoSeleccionado == null) {
            return;
        }

        String codigoInterno = productoSeleccionado.getCodigoProperty().get();

        try {
            productoObjetivo = persistencia.consultarProductoPorCodigoInterno(codigoInterno);

            FXMLLoader productoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/ProductoDlg.fxml"));
            productoDlgFxmlLoader.setControllerFactory(c -> {
                return new ProductoDlgController(persistencia, DialogoOperacion.ACTUALIZAR, productoObjetivo);
            });

            Parent root = null;

            try {
                root = productoDlgFxmlLoader.load();
            } catch (IOException e) {
                // TODO: Manejar
                throw new RuntimeException(e);
            }

            Stage productoDlgStage = new Stage();

            ProductoDlgController productoDlgController = productoDlgFxmlLoader.getController();
            productoDlgController.setStage(productoDlgStage);

            Scene scene = new Scene(root);
            productoDlgStage.setMaximized(false);
            productoDlgStage.setResizable(false);
            productoDlgStage.setScene(scene);
            // NOTE: Cambiar segun operacion...
            productoDlgStage.setTitle("Modificar Producto");
            productoDlgStage.initModality(Modality.APPLICATION_MODAL);
            productoDlgStage.show();

            // TODO: Actualizar la tabla sin dicho producto...
        } catch (PersistenciaBazarException e) {
            // TODO: asasasas
            throw new RuntimeException(e);
        }
    }

    public void eliminarProducto() {
        ProductoDTO productoObjetivo;

        if (productoSeleccionado == null) {
            return;
        }

        String codigoInterno = productoSeleccionado.getCodigoProperty().get();

        try {
            productoObjetivo = persistencia.consultarProductoPorCodigoInterno(codigoInterno);

            FXMLLoader productoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/ProductoDlg.fxml"));
            productoDlgFxmlLoader.setControllerFactory(c -> {
                return new ProductoDlgController(persistencia, DialogoOperacion.ELIMINAR, productoObjetivo);
            });

            Parent root = null;

            try {
                root = productoDlgFxmlLoader.load();
            } catch (IOException e) {
                // TODO: Manejar
                throw new RuntimeException(e);
            }

            Stage productoDlgStage = new Stage();

            ProductoDlgController productoDlgController = productoDlgFxmlLoader.getController();
            productoDlgController.setStage(productoDlgStage);

            Scene scene = new Scene(root);
            productoDlgStage.setMaximized(false);
            productoDlgStage.setResizable(false);
            productoDlgStage.setScene(scene);
            // NOTE: Cambiar segun operacion...
            productoDlgStage.setTitle("Eliminar producto");
            productoDlgStage.initModality(Modality.APPLICATION_MODAL);
            productoDlgStage.show();

            // TODO: Actualizar la tabla sin dicho producto...
        } catch (PersistenciaBazarException e) {
            // TODO: asasasas
            throw new RuntimeException(e);
        }
    }

    /**
     * Sale del dialogo sin conservar el producto seleccionado
     */
    public void cerrarDialogo() {
        productoSeleccionado = null;
        this.stage.close();
    }
}
