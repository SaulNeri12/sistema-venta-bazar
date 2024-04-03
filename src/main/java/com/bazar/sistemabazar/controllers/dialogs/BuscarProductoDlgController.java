package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.DetallesVentaTableView;
import com.bazar.sistemabazar.components.tables.ProductosTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.DetalleVentaTableModel;
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

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BuscarProductoDlgController implements Initializable {

    private ObservableList<ProductoTableModel> listaProductos;

    private Timer timerBusquedaProducto;

    private Stage stage;
    private ProductosTableView tablaProductos;

    private ProductoTableModel productoSeleccionado;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductosTableView tablaProductos = new ProductosTableView();

        int numColumnas = tablaProductos.getColumns().size();

        /*
        // removemos las columnas de la tabla ya que no se utilizaran...
        tablaProductos.getColumns().remove(--numColumnas); // se elimina la columna del total
        tablaProductos.getColumns().remove(--numColumnas); // se elimina la columna de la cantidad
        tablaProductos.getColumns().remove(--numColumnas); // se elimina la columna de la cantidad

         */

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
                        //System.out.println("CAMBIA!!!");
                    }
                }, 100);
            }
        });


        Instant fechaActual = Instant.now();
        LocalDateTime fechaLocal = LocalDateTime.ofInstant(fechaActual, ZoneId.systemDefault());


        this.listaProductos = FXCollections.observableArrayList(
                new ProductoTableModel("XA1010", "Jarra Plastico", 54.50f, fechaLocal),
                new ProductoTableModel("XA1234", "Vaso Vidrio", 35.0f, fechaLocal),
                new ProductoTableModel("SX1010", "Plato Vidrio Blanco", 40.0f, fechaLocal),
                new ProductoTableModel("T34121", "Plato Plastico", 32.0f,fechaLocal),
                new ProductoTableModel("SO1234", "Cesta ropa", 90.50f, fechaLocal),
                new ProductoTableModel("POP123", "Caja Madera 25x25cm", 56.5f, fechaLocal),
                new ProductoTableModel("PO1234", "Caja ordenadora madera 25x25", 97.0f, fechaLocal),
                new ProductoTableModel("TZ1234", "Taza Cafe Java", 50.0f, fechaLocal),
                new ProductoTableModel("TZ1334", "Taza Cafe Jurassic Park", 60.0f, fechaLocal),
                new ProductoTableModel("CA1111", "Calentadera Metal Azul", 125.0f, fechaLocal)
        );

        tablaProductos.getItems().addAll(this.listaProductos);
        tablaProductos.setEditable(false);

        this.tablaProductos = tablaProductos;

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
