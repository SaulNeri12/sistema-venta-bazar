package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.property.*;
import objetosDTO.ProductoDTO;

public class DetalleVentaTableModel extends ProductoTableModel {
    private SimpleIntegerProperty cantidad;
    private SimpleFloatProperty total;

    public DetalleVentaTableModel(String codigo, String nombre, float precio, int cantidad) {
        super(codigo, nombre, precio, null);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.total = new SimpleFloatProperty(precio * cantidad);
    }

    public DetalleVentaTableModel(ProductoDTO producto, int cantidad) {
        super(
                producto.getCodigoInterno(),
                producto.getNombre(),
                producto.getPrecio(),
                null
        );

        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.total = new SimpleFloatProperty(producto.getPrecio() * cantidad);
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

    public SimpleIntegerProperty getCantidadProperty() {
        return this.cantidad;
    }

    public SimpleFloatProperty getTotalProperty() {
        return this.total;
    }

    @Override
    public String toString() {
        return String.format(
                "ProductoVenta(%s, %s, %.2f, %d)",
                codigo.get(),
                nombre.get(),
                precio.get(),
                cantidad.get()
        );
    }
}
