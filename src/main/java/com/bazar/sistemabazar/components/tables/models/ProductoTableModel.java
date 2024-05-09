package com.bazar.sistemabazar.components.tables.models;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.FloatProperty;
import objetosDTO.ProductoDTO;

import java.time.LocalDateTime;

public class ProductoTableModel {
    protected SimpleStringProperty codigo;
    protected SimpleStringProperty nombre;
    protected SimpleFloatProperty precio;
    protected ObjectProperty<LocalDateTime> fechaRegistro;

    public ProductoTableModel(String codigo, String nombre, float precio, LocalDateTime fechaRegistro) {
        this.codigo = new SimpleStringProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleFloatProperty(precio);
        this.fechaRegistro = new SimpleObjectProperty<LocalDateTime>(fechaRegistro);
    }

    public ProductoTableModel(ProductoDTO producto) {
        this.codigo = new SimpleStringProperty(producto.getCodigoInterno());
        this.nombre = new SimpleStringProperty(producto.getNombre());
        this.precio = new SimpleFloatProperty(producto.getPrecio());
        this.fechaRegistro = new SimpleObjectProperty<>(producto.getFechaRegistro());
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public SimpleStringProperty getCodigoProperty() {
        return this.codigo;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPrecio(float precio) {
        this.precio.set(precio);
    }

    public SimpleStringProperty getNombreProperty() {
        return this.nombre;
    }

    public SimpleFloatProperty getPrecioProperty() {
        return this.precio;
    }

    private void setFechaRegistro(LocalDateTime fecha) {
        this.fechaRegistro.set(fecha);
    }

    public ObjectProperty<LocalDateTime> getFechaRegistroProperty() {
        return this.fechaRegistro;
    }

    @Override
    public String toString() {
        return String.format(
                "Producto(%s, %s, %.2f)",
                codigo.get(),
                nombre.get(),
                precio.get()
        );
    }
}
