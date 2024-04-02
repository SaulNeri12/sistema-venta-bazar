package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.ProductoVentaTableView;
import com.bazar.sistemabazar.components.tables.ProveedoresTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import com.bazar.sistemabazar.components.tables.models.ProveedorTableModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BuscarProveedorDlgController implements Initializable {

    private ObservableList<ProveedorTableModel> listaProveedores;

    // se encarga de tener un retardo en cada nueva busqueda de coincidencias
    private Timer timerBusquedaProveedor;

    private Stage stage;
    private ProveedoresTableView tablaProveedores;
    private ProveedorTableModel proveedorSeleccionado;

    @FXML
    public Button botonAgregarProveedor;
    @FXML
    public VBox panelTablaProveedores;
    @FXML
    public TextField nombreProveedorTextField;
    //@FXML
    public TextField proveedorSeleccionadoTextField;

    public void setStage(Stage stage) { this.stage = stage; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProveedoresTableView tablaProveedores = new ProveedoresTableView();

        int numColumnas = tablaProveedores.getColumns().size();

        /*
        // removemos las columnas de la tabla ya que no se utilizaran...
        tablaProveedores.getColumns().remove(--numColumnas); // se elimina la columna del total
        tablaProveedores.getColumns().remove(--numColumnas); // se elimina la columna de la cantidad
        tablaProveedores.getColumns().remove(--numColumnas); // se elimina la columna de la cantidad
        */

        // proveedor seleccionado en la tabla
        proveedorSeleccionado = null;

        // cuando se hace click en la tabla
        tablaProveedores.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Obtener la fila seleccionada
                ProveedorTableModel proveedorTablaSeleccionado = tablaProveedores.getSelectionModel().getSelectedItem();

                if (proveedorTablaSeleccionado == null) {
                    botonAgregarProveedor.setDisable(true);
                    return;
                }

                botonAgregarProveedor.setDisable(false);
                proveedorSeleccionado = proveedorTablaSeleccionado;

                proveedorSeleccionadoTextField.setText(
                        String.format(
                                "ProveedorID: %d, Nombre: %s, Telefono: %s",
                                proveedorSeleccionado.getIdProperty().get(),
                                proveedorSeleccionado.getNombreProperty().get(),
                                proveedorSeleccionado.getTelefonoProperty().get()

                        )
                );

            }
        });



        timerBusquedaProveedor = new Timer();

        nombreProveedorTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String nuevoString) {
                // Cancelar el temporizador existente si hay uno
                timerBusquedaProveedor.cancel();
                timerBusquedaProveedor = new Timer();

                timerBusquedaProveedor.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        filtrarProveedoresPorNombre(nuevoString.toLowerCase());
                        System.out.println("CAMBIA!!!");
                    }
                }, 100);
            }
        });

        this.listaProveedores = FXCollections.observableArrayList();

        tablaProveedores.getItems().addAll(listaProveedores);
        tablaProveedores.setEditable(false);

        this.tablaProveedores = tablaProveedores;

        panelTablaProveedores.getChildren().add(tablaProveedores);

        this.proveedorSeleccionadoTextField.setText("");
    }

    public ProveedorTableModel getProveedorSeleccionado() {
        return this.proveedorSeleccionado;
    }

    /**
     * Cierra el dialogo conservando el producto seleccionado
     */
    public void agregarProveedor() {
        if (this.proveedorSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Advertencia - Agregar Proveedor");
            alert.setContentText("Primero selecciona un proveedor");
            alert.showAndWait();
            return;
        }

        this.stage.close();
    }

    /**
     * Filtra los elementos de la tabla del dialogo por su nombre.
     * @param nombreProveedor Nombre del proveedor a buscar
     */
    public void filtrarProveedoresPorNombre(String nombreProveedor) {
        ObservableList<ProveedorTableModel> proveedores = listaProveedores;

        if (nombreProveedor == null || nombreProveedor.isEmpty()) {
            tablaProveedores.getItems().remove(0, tablaProveedores.getItems().size());
            // se agregan todos los elementos encontrados cuando la entrada del
            // nombre del proveedor es null o esta vacia...
            for (ProveedorTableModel proveedor: proveedores) {
                tablaProveedores.getItems().add(proveedor);
            }
            return;
        }

        // cada proveedor el cual contiene en su nombre, el texto almacenado en
        // 'nombreProveedor' es anadido a la tabla ya que cumple con la busqueda
        for (ProveedorTableModel proveedor: proveedores) {
            if (!proveedor.getNombreProperty().get().toLowerCase().contains(nombreProveedor)) {
                tablaProveedores.getItems().remove(proveedor);
            }
        }
    }

    /**
     * Sale del dialogo sin conservar el proveedor seleccionado
     */
    public void cerrarDialogo() {
        proveedorSeleccionado = null;
        this.stage.close();
    }
}
