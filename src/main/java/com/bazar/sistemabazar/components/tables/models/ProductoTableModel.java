package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;

public class ProductoTableModel {
    protected SimpleIntegerProperty productoId;
    protected SimpleStringProperty nombre;
    protected SimpleFloatProperty precio;
    protected ObjectProperty<LocalDateTime> fechaRegistro;

    public ProductoTableModel(int productoId, String nombre, float precio, LocalDateTime fechaRegistro) {
        this.productoId = new SimpleIntegerProperty(productoId);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleFloatProperty(precio);
        this.fechaRegistro = new SimpleObjectProperty<LocalDateTime>(fechaRegistro);
    }

    public void setId(int productoId) {
        this.productoId.set(productoId);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPrecio(float precio) { this.precio.set(precio); }

    private void setFechaRegistro(LocalDateTime fecha) { this.fechaRegistro.set(fecha); };

    public IntegerProperty getIdProperty() {
        return this.productoId;
    }

    public StringProperty getNombreProperty() {
        return this.nombre;
    }

    public FloatProperty getPrecioProperty() {
        return this.precio;
    }

    public ObjectProperty<LocalDateTime> getFechaRegistroProperty() { return this.fechaRegistro; };

    @Override
    public String toString() {
        return String.format(
                "Producto(%d, %s, %.2f)",
                productoId.get(),
                nombre.get(),
                precio.get()
        );
    }
}
