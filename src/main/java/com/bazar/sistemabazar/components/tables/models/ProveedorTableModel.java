package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.property.*;
import objetosNegocio.ProveedorDTO;

import java.time.LocalDateTime;

public class ProveedorTableModel {
    private SimpleLongProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty telefono;
    //private SimpleStringProperty email;
    private SimpleStringProperty descripcion;
    //private SimpleObjectProperty<LocalDateTime> fechaRegistro;

    public ProveedorTableModel(long id, String nombre, String telefono, String descripcion) {
        this.id = new SimpleLongProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public ProveedorTableModel(ProveedorDTO proveedor) {
        this.id = new SimpleLongProperty(proveedor.getId());
        this.nombre = new SimpleStringProperty(proveedor.getNombre());
        this.telefono = new SimpleStringProperty(proveedor.getTelefono());
        this.descripcion = new SimpleStringProperty(proveedor.getDescripcion());
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public LongProperty getIdProperty() {
        return this.id;
    }

    public StringProperty getNombreProperty() {
        return this.nombre;
    }

    public StringProperty getTelefonoProperty() {
        return this.telefono;
    }

    public StringProperty getDescripcionProperty() {
        return this.descripcion;
    }
}
