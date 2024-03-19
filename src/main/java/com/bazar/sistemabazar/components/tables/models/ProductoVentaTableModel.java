package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.property.*;

public class ProductoVentaTableModel extends ProductoTableModel {
    private SimpleIntegerProperty cantidad;
    private SimpleFloatProperty total;

    public ProductoVentaTableModel(int productoId, String nombre, float precio, int cantidad) {
        super(productoId, nombre, precio, null);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.total = new SimpleFloatProperty(precio * cantidad);
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
        this.calcularTotal();
    }

    public void setTotal(float total) {
        this.total.set(total);
    }

    private void calcularTotal() {
        float total = this.precio.get() * this.cantidad.get();
        this.total.set(total);
    }

    public IntegerProperty getCantidadProperty() {
        return this.cantidad;
    }

    public FloatProperty getTotalProperty() {
        return this.total;
    }
}
