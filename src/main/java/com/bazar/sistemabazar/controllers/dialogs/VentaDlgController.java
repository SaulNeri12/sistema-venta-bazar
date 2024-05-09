package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.DetallesVentaTableView;
import com.bazar.sistemabazar.components.tables.VentasTableView;
import com.bazar.sistemabazar.components.tables.models.DetalleVentaTableModel;
import com.bazar.sistemabazar.components.tables.models.VentaTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import objetosDTO.DetalleVentaDTO;
import objetosDTO.VentaDTO;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class VentaDlgController implements Initializable {

    private IPersistenciaBazar persistencia;
    private DialogoOperacion operacion;
    private VentaDTO venta;
    private DetallesVentaTableView tablaProductosVendidos;
    private ObservableList<DetalleVentaTableModel> listaProductosVendidos;

    private Stage stage;

    @FXML
    private Label tituloDialogoLabel;
    @FXML
    private TextField nombreClienteTextField;
    @FXML
    private TextField apellidoClienteTextField;
    @FXML
    private ChoiceBox<String> metodoPagoChoiceBox;
    @FXML
    private DatePicker fechaVentaDatePicker;
    @FXML
    private TextField horaVentaTextField;
    @FXML
    private VBox panelTablaDetalleVenta;
    @FXML
    private Button botonAceptar;
    @FXML
    private Button botonRestaurar;
    @FXML
    private Button botonCerrar;

    public VentaDlgController(IPersistenciaBazar persistencia, DialogoOperacion operacion, Long idVenta) {
        this.persistencia = persistencia;
        this.operacion = operacion;
        this.venta = new VentaDTO();
        this.venta.setId(idVenta);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablaProductosVendidos = new DetallesVentaTableView(this.operacion);

        this.panelTablaDetalleVenta.getChildren().add(tablaProductosVendidos);

        this.metodoPagoChoiceBox.setItems(FXCollections.observableArrayList(
                "EFECTIVO",
                "TARJETA"
        ));

        try {
            VentaDTO venta = persistencia.consultarVenta(this.venta.getId());

            this.venta = venta;

            listaProductosVendidos = FXCollections.observableArrayList();

            for (DetalleVentaDTO detalleVenta: venta.getProductosVendidos()) {
                DetalleVentaTableModel filaVenta = new DetalleVentaTableModel(detalleVenta.getProducto(), detalleVenta.getCantidad());
                listaProductosVendidos.add(filaVenta);
            }

            tablaProductosVendidos.getItems().addAll(listaProductosVendidos);

        } catch (PersistenciaBazarException e) {
            throw new RuntimeException(e);
            // TODO: MANEJAR ERROR CON ALERTA SI ES NECESARIO...
        }


        this.prepararInformacionVenta();
        this.prepararModoOperacion();
    }

    public void prepararModoOperacion() {
        this.tituloDialogoLabel.setText("Informacion de Venta");
        if (this.operacion == DialogoOperacion.LECTURA) {
            this.nombreClienteTextField.setEditable(false);
            this.apellidoClienteTextField.setEditable(false);
            this.metodoPagoChoiceBox.setDisable(true);
            this.fechaVentaDatePicker.setEditable(false);
            this.horaVentaTextField.setEditable(false);
            this.tablaProductosVendidos.setEditable(false);
            // botones
            this.botonRestaurar.setVisible(false);
            this.botonAceptar.setVisible(false);
        }
        else if (this.operacion == DialogoOperacion.ACTUALIZAR) {
            this.fechaVentaDatePicker.setEditable(false);
            this.horaVentaTextField.setEditable(false);
            // botones
            this.botonAceptar.setText("Guardar Cambios");
        }
        else if (this.operacion == DialogoOperacion.ELIMINAR) {
            this.nombreClienteTextField.setEditable(false);
            this.apellidoClienteTextField.setEditable(false);
            this.metodoPagoChoiceBox.setDisable(true);
            this.fechaVentaDatePicker.setEditable(false);
            this.horaVentaTextField.setEditable(false);
            this.tablaProductosVendidos.setEditable(false);
            // botones
            this.botonRestaurar.setVisible(false);
            this.botonAceptar.setText("Eliminar");
        }
    }

    public void prepararInformacionVenta() {
        if (this.venta != null) {
            this.nombreClienteTextField.setText(venta.getNombreCliente());
            this.apellidoClienteTextField.setText(venta.getApellidoCliente());
            this.metodoPagoChoiceBox.setValue(venta.getMetodoPago().toString());
            this.fechaVentaDatePicker.setValue(venta.getFechaVenta().toLocalDate());
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm");
            this.horaVentaTextField.setText(venta.getFechaVenta().format(formatoHora));
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
