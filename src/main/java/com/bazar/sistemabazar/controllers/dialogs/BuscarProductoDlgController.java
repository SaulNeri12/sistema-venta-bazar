package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProductosVentaTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
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
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BuscarProductoDlgController implements Initializable {

    private ObservableList<ProductoVentaTableModel> listaProductos;

    private Timer timerBusquedaProducto;

    private Stage stage;
    private ProductosVentaTableView tablaProductos;

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
        ProductosVentaTableView tablaProductos = new ProductosVentaTableView();

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
                        System.out.println("CAMBIA!!!");
                    }
                }, 100);
            }
        });

        this.listaProductos = FXCollections.observableArrayList(
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

        this.productoSeleccionado.setCantidad((Integer) this.cantidadProductoSpinner.getValue());

        this.stage.close();
    }

    /**
     * Filtra los elementos de la tabla del dialogo por su nombre.
     * @param nombreProducto Nombre del producto a buscar
     */
    public void filtrarProductosPorNombre(String nombreProducto) {
        ObservableList<ProductoVentaTableModel> productos = listaProductos;

        if (nombreProducto == null || nombreProducto.isEmpty()) {
            tablaProductos.getItems().remove(0, tablaProductos.getItems().size());
            // se agregan todos los elementos encontrados cuando la entrada del
            // nombre del producto es null o esta vacia...
            for (ProductoVentaTableModel p: productos) {
                tablaProductos.getItems().add(p);
            }
            return;
        }

        // cada producto el cual contiene en su nombre, el texto almacenado en
        // 'nombreProducto' es anadido a la tabla ya que cumple con la busqueda
        for (ProductoVentaTableModel prdct: productos) {
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
