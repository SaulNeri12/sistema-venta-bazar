package com.bazar.sistemabazar.controllers.dialogs;

import com.bazar.sistemabazar.components.tables.VentasTableView;
import com.bazar.sistemabazar.components.tables.models.VentaTableModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import objetosNegocio.Usuario;
import objetosNegocio.Venta;
import persistencia.IPersistenciaBazar;
import persistencia.excepciones.PersistenciaBazarException;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BuscarVentaDlgController implements Initializable {

    private IPersistenciaBazar persistencia;
    private VentasTableView tablaVentas;
    private Usuario usuario;
    private Stage stage;

    @FXML
    public VBox panelTablaVentas;
    public Button botonVerDetalles;

    public BuscarVentaDlgController(IPersistenciaBazar persistencia, Usuario usuario) {
        this.persistencia = persistencia;
        this.usuario = usuario;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaVentas = new VentasTableView();

        this.panelTablaVentas.getChildren().add(tablaVentas);

        try {
            List<Venta> ventasEnSistema = persistencia.consultarVentasTodas();

            for (Venta venta: ventasEnSistema) {
                VentaTableModel filaVenta = new VentaTableModel(venta);
                tablaVentas.getItems().add(filaVenta);
            }

        } catch (PersistenciaBazarException e) {
            throw new RuntimeException(e);
            // TODO: MANEJAR ERROR CON ALERTA SI ES NECESARIO...
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void desplegarDialogoDetalleVenta() {
        // TODO: FALTARA CREAR EL DIALOGO VentaDlg Y DESPUES IMPLEMENTAR ESTE METODO
    }

    @FXML
    public void cerrarDialogo() {
        this.stage.close();
    }


}

