package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import objetosNegocio.Venta;

public class VentaTableModel {
    private SimpleLongProperty id;
    private SimpleStringProperty nombreCliente;
    private SimpleStringProperty metodoPago;
    private SimpleFloatProperty montoTotal;

    public VentaTableModel(Venta venta) {
        this.id = new SimpleLongProperty(venta.getId());
        this.nombreCliente = new SimpleStringProperty(venta.getNombreCliente() + " " + venta.getApellidoCliente());
        this.metodoPago = new SimpleStringProperty(venta.getMetodoPago().toString());
        this.montoTotal = new SimpleFloatProperty(venta.getMontoTotal());
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public SimpleLongProperty getIdProperty() {
        return this.id;
    }

    public void setNombreCliente(String nombreApellido) {
        this.nombreCliente.set(nombreApellido);
    }

    public SimpleStringProperty getNombreClienteProperty() {
        return this.nombreCliente;
    }

    public void setMetodoPago(Venta.MetodoPago metodoPago) {
        this.metodoPago.set(metodoPago.toString());
    }

    public SimpleStringProperty getMetodoPagoProperty() {
        return this.metodoPago;
    }

    public void setMontoTotal(Float montoTotal) {
        this.montoTotal.set(montoTotal);
    }

    public SimpleFloatProperty getMontoTotalProperty() {
        return this.montoTotal;
    }
}
