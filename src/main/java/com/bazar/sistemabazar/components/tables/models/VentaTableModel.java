package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import objetosNegocio.Venta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentaTableModel {
    private SimpleLongProperty id;
    private SimpleStringProperty nombreCliente;
    private SimpleStringProperty metodoPago;
    private SimpleFloatProperty montoTotal;
    private SimpleStringProperty fechaVenta;
    private DateTimeFormatter formatoFecha;

    public VentaTableModel(Venta venta) {
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.id = new SimpleLongProperty(venta.getId());
        this.nombreCliente = new SimpleStringProperty(venta.getNombreCliente() + " " + venta.getApellidoCliente());
        this.metodoPago = new SimpleStringProperty(venta.getMetodoPago().toString());
        this.montoTotal = new SimpleFloatProperty(venta.getMontoTotal());
        this.fechaVenta = new SimpleStringProperty(venta.getFechaVenta().format(this.formatoFecha));
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

    public void setFechaVentaProperty(LocalDateTime fecha) {
        this.fechaVenta.set(fecha.format(this.formatoFecha));
    }

    public SimpleStringProperty getFechaVentaProperty() {
        return this.fechaVenta;
    }
}
