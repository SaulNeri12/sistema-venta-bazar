package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.VentasTableView;
import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.VentaTableModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import objetosNegocio.UsuarioDTO;
import objetosNegocio.VentaDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

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

        // inicializa los datos de la tabla de ventas
        try {
            List<VentaDTO> ventasEnSistema = obtenerVentasFiltradas();

            for (VentaDTO venta: ventasEnSistema) {
                VentaTableModel filaVenta = new VentaTableModel(venta);
                tablaVentas.getItems().add(filaVenta);
            }

            listaVentas = tablaVentas.getItems();

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
        // TODO: FALTARA CREAR EL DIALOGO VentaDlg Y DESPUES IMPLEMENTAR ESTE METODO
    }

    public void filtrarVentasPorNombre(String nombreCliente) {
        ObservableList<VentaTableModel> ventas = listaVentas;

        if (nombreCliente == null || nombreCliente.isEmpty()) {
            tablaVentas.getItems().clear();
            // se agregan todos los elementos encontrados cuando la entrada del
            // nombre del producto es null o esta vacia...
            tablaVentas.setItems(listaVentas);
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
            LocalDateTime fechaHoraActual = LocalDateTime.now();
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

