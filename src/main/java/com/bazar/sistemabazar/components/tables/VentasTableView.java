package com.bazar.sistemabazar.components.tables;

import com.bazar.sistemabazar.components.tables.models.ProveedorTableModel;
import com.bazar.sistemabazar.components.tables.models.VentaTableModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import objetosNegocio.Venta;

public class VentasTableView extends TableView<VentaTableModel> implements IControlTabla<Venta> {

    public Venta ventaSeleccionada;

    public VentasTableView() {
        super();
        inicializar();

        ventaSeleccionada = null;
    }

    @Override
    public void inicializar() {
        TableColumn<VentaTableModel, Long> colVentaId = new TableColumn<>("ID");
        colVentaId.setMaxWidth(100);
        colVentaId.setMinWidth(80);
        colVentaId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());

        TableColumn<VentaTableModel, String> colVentaNombreCliente = new TableColumn<>("Nombre del cliente");
        colVentaNombreCliente.setMaxWidth(250);
        colVentaNombreCliente.setMinWidth(150);
        colVentaNombreCliente.setCellValueFactory(cellData -> cellData.getValue().getNombreClienteProperty());

        TableColumn<VentaTableModel, String> colVentaMetodoPago = new TableColumn<>("Metodo pago");
        colVentaMetodoPago.setMaxWidth(100);
        colVentaMetodoPago.setMinWidth(100);
        colVentaMetodoPago.setCellValueFactory(cellData -> cellData.getValue().getMetodoPagoProperty());

        TableColumn<VentaTableModel, Float> colVentaMontoTotal = new TableColumn<>("Monto total");
        colVentaMontoTotal.setMaxWidth(100);
        colVentaMontoTotal.setMinWidth(100);
        colVentaMontoTotal.setCellValueFactory(cellData -> cellData.getValue().getMontoTotalProperty().asObject());

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.getColumns().addAll(
                colVentaId,
                colVentaNombreCliente,
                colVentaMetodoPago,
                colVentaMontoTotal
        );
    }

    @Override
    public Venta obtenerFilaObjetoSeleccionado() {
        VentaTableModel ventaSeleccionadaEnTabla = this.getSelectionModel().getSelectedItem();

        if (ventaSeleccionadaEnTabla == null) {
            return null;
        }

        ventaSeleccionada = new Venta();
        ventaSeleccionada.setId(ventaSeleccionadaEnTabla.getIdProperty().get());

        return ventaSeleccionada;
    }


    @Override
    public Venta obtenerFilaObjetoPorIndice(int indice) {
        VentaTableModel ventaEnTabla = this.getItems().get(indice);

        Venta venta = new Venta();
        venta.setId(ventaEnTabla.getIdProperty().get());

        return venta;
    }

    @Override
    public void agregarFilaObjeto(Venta venta) {
        if (venta == null) {
            return;
        }

        this.getItems().add(new VentaTableModel(venta));
    }

    @Override
    public boolean eliminarFilaObjeto(Venta venta) {
        return this.getItems().removeIf(v -> v.getIdProperty().get() == venta.getId());
    }

    @Override
    public boolean eliminarFilaObjetoPorIndice(int indice) {
        return (this.getItems().remove(indice) != null);
    }

    @Override
    public void eliminarTodasLasFilas() {
        this.getItems().clear();
    }
}