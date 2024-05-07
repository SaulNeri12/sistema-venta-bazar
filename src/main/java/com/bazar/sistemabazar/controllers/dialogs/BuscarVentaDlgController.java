package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.VentasTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.VentaTableModel;
import com.bazar.sistemabazar.BazarApplication;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import objetosNegocio.UsuarioDTO;
import objetosNegocio.VentaDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BuscarVentaDlgController implements Initializable {

    public enum Filtro {
        TODOS,
        VENTAS_DEL_DIA,
        VENTAS_ULTIMA_HORA
    };

    private IPersistenciaBazar persistencia;
    private VentasTableView tablaVentas;
    private Stage stage;
    private UsuarioDTO usuario;
    private Filtro filtroPrincipal;

    private VentaTableModel ventaSeleccionada;

    private ObservableList<VentaTableModel> listaVentas;
    private Timer timerBusquedaVenta;

    @FXML
    public VBox panelTablaVentas;
    @FXML
    public Button botonVerDetalles;
    @FXML
    private DatePicker fechaInicioDatePicker;
    @FXML
    private TextField horaFechaInicioTextField;
    @FXML
    private DatePicker fechaFinDatePicker;
    @FXML
    private TextField horaFechaFinTextField;
    @FXML
    private TextField nombreClienteTextField;

    public BuscarVentaDlgController(IPersistenciaBazar persistencia, UsuarioDTO usuario, Filtro filtroPrincipal) {
        this.filtroPrincipal = filtroPrincipal;
        this.persistencia = persistencia;
        this.usuario = usuario;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaVentas = new VentasTableView();

        this.panelTablaVentas.getChildren().add(tablaVentas);

        tablaVentas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Obtener la fila seleccionada
                VentaTableModel ventaTablaSeleccionado = tablaVentas.getSelectionModel().getSelectedItem();

                if (ventaTablaSeleccionado == null) {
                    botonVerDetalles.setDisable(true);
                    return;
                }
                this.ventaSeleccionada = ventaTablaSeleccionado;
                botonVerDetalles.setDisable(false);
            }
        });

        // inicializa los datos de la tabla de ventas
        try {
            List<VentaDTO> ventasEnSistema = obtenerVentasFiltradas();

            listaVentas = FXCollections.observableArrayList();

            for (VentaDTO venta: ventasEnSistema) {
                VentaTableModel filaVenta = new VentaTableModel(venta);
                //tablaVentas.getItems().add(filaVenta);
                listaVentas.add(filaVenta);
            }

            tablaVentas.setItems(listaVentas);


        } catch (PersistenciaBazarException e) {
            //throw new RuntimeException(e);
            System.out.println(e.getMessage());
            // TODO: MANEJAR ERROR CON ALERTA SI ES NECESARIO...
        }

        timerBusquedaVenta = new Timer();

        // se encarga de detectar las pulsaciones de teclado en el campo de nombre del cliente
        nombreClienteTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String nuevoString) {
                // Cancelar el temporizador existente si hay uno
                timerBusquedaVenta.cancel();
                timerBusquedaVenta = new Timer();

                timerBusquedaVenta.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        filtrarVentasPorNombre(nuevoString.toLowerCase());
                    }
                }, 100);
            }
        });


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void desplegarDialogoDetalleVenta() {
        // TODO: ARROJAR EXCEPCION
        if (ventaSeleccionada == null) {
            return;
        }

        FXMLLoader detalleVentaDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/VentaDlg.fxml"));

        detalleVentaDlgFxmlLoader.setControllerFactory(c -> {
            return new VentaDlgController(persistencia, DialogoOperacion.LECTURA, ventaSeleccionada.getIdProperty().get());
        });

        Parent root = null;

        try {
            root = detalleVentaDlgFxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage ventasDlgStage = new Stage();

        VentaDlgController ventaDlgController = detalleVentaDlgFxmlLoader.getController();
        ventaDlgController.setStage(ventasDlgStage);

        Scene scene = new Scene(root);
        ventasDlgStage.setMaximized(false);
        ventasDlgStage.setResizable(false);
        ventasDlgStage.setScene(scene);
        ventasDlgStage.setTitle("Informacion Venta");

        ventasDlgStage.show();
    }

    public void filtrarVentasPorNombre(String nombreCliente) {
        ObservableList<VentaTableModel> ventas = listaVentas;

        if (nombreCliente == null || nombreCliente.isEmpty()) {
            //tablaVentas.getItems().clear();
            tablaVentas.getItems().remove(0, tablaVentas.getItems().size());
            // se agregan todos los elementos encontrados cuando la entrada del
            // nombre del producto es null o esta vacia...
            for (VentaTableModel venta: ventas) {
                tablaVentas.getItems().add(venta);
            }
            return;
        }

        // cada producto el cual contiene en su nombre, el texto almacenado en
        // 'nombreProducto' es anadido a la tabla ya que cumple con la busqueda
        for (VentaTableModel venta: ventas) {
            String nombreClienteVenta = venta.getNombreClienteProperty().get();
            if (!nombreClienteVenta.toLowerCase().contains(nombreCliente)) {
                tablaVentas.getItems().remove(venta);
            }
        }
    }

    public void actualizarTabla() {
        try {
            List<VentaDTO> ventasEnSistema = obtenerVentasFiltradas();

            tablaVentas.getItems().clear();

            for (VentaDTO venta: ventasEnSistema) {
                VentaTableModel filaVenta = new VentaTableModel(venta);
                tablaVentas.getItems().add(filaVenta);
            }

        } catch (PersistenciaBazarException e) {
            //throw new RuntimeException(e);
            System.out.println(e.getMessage());

            // TODO: MANEJAR ERROR CON ALERTA SI ES NECESARIO...
        }
    }

    public List<VentaDTO> obtenerVentasFiltradas() throws PersistenciaBazarException {

        List<VentaDTO> listaVentas = null;

        if (this.filtroPrincipal == Filtro.VENTAS_DEL_DIA) {
            LocalDateTime fechaHoraInicioDia = LocalDate.now().atStartOfDay();
            LocalDateTime fechaHoraActual = LocalDate.now().plusDays(1).atStartOfDay();
            listaVentas = persistencia.consultarVentasPorPeriodo(fechaHoraInicioDia, fechaHoraActual);
        }
        else if (this.filtroPrincipal == Filtro.VENTAS_ULTIMA_HORA) {
            LocalDateTime fechaHoraAntes = LocalDateTime.now().minusHours(1);
            //System.out.println(fechaHoraAntes);
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            listaVentas = persistencia.consultarVentasPorPeriodo(fechaHoraAntes, fechaHoraActual);
        } else {
            listaVentas = persistencia.consultarVentasTodas();
        }

        return listaVentas;
    }

    @FXML
    public void cerrarDialogo() {
        this.stage.close();
    }


}

