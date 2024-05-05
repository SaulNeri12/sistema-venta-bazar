package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProductosTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import objetosNegocio.ProductoDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BuscarProductoVentaDlgController implements Initializable {

    private ObservableList<ProductoTableModel> listaProductos;

    private IPersistenciaBazar persistencia;

    private Timer timerBusquedaProducto;

    private Stage stage;
    private ProductosTableView tablaProductos;

    private ProductoTableModel productoSeleccionado;
    private ProductoDTO objetoProducto;

    private Integer cantidadProductos;

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

    public BuscarProductoVentaDlgController(IPersistenciaBazar persistencia) {
        this.persistencia = persistencia;
        objetoProducto = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductosTableView tablaProductos = new ProductosTableView();

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
                productoSeleccionadoTextField.setText(
                        String.format(
                                "Producto(Codigo: %s, Nombre: %s)",
                                productoSeleccionado.getCodigoProperty().get(),
                                productoSeleccionado.getNombreProperty().get()
                        )
                );

                try {
                    objetoProducto = persistencia.consultarProductoPorCodigoInterno(productoSeleccionado.getCodigoProperty().get());
                } catch (PersistenciaBazarException e) {
                    objetoProducto = null;
                    // TODO: MOSTRAR ALERTA
                }
            }
        });

        // se le asigna la cantidad maxima para un producto
        this.cantidadProductoSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1,
                        100
                )
        );

        timerBusquedaProducto = new Timer();

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

        // cargar todos los productos en la tabla desde la persistencia...
        this.listaProductos = FXCollections.observableArrayList();

        try {
            List<ProductoDTO> productosEnSistema = persistencia.consultarProductosTodos();

            for (ProductoDTO producto: productosEnSistema) {
                ProductoTableModel productoFilaTabla = new ProductoTableModel(producto);
                this.listaProductos.add(productoFilaTabla);
            }

        } catch (PersistenciaBazarException e) {
            throw new RuntimeException(e);
            // TODO: EMITIR ALERTA
        }

        tablaProductos.getItems().addAll(this.listaProductos);
        tablaProductos.setEditable(false);

        this.tablaProductos = tablaProductos;

        panelTablaProductos.getChildren().add(tablaProductos);
    }

    public ProductoDTO getProductoSeleccionado() {
        return this.objetoProducto;
    }

    /**
     * Cierra el dialogo conservando el producto seleccionado
     * TODO: CAMBIAR NOMBRE
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

        this.cantidadProductos = (Integer) this.cantidadProductoSpinner.getValue();

        this.stage.close();
    }

    public int getCantidadProductos() {
        return this.cantidadProductos;
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

    /**
     * Sale del dialogo sin conservar el producto seleccionado
     */
    public void cerrarDialogo() {
        productoSeleccionado = null;
        this.stage.close();
    }


}
