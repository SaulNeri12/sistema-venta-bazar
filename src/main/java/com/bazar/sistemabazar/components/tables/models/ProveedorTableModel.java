package com.bazar.sistemabazar.components.tables.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class ProveedorTableModel {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty telefono;
    /* TODO: private SimpleObjectProperty<Direccion> direccion; */
    //private SimpleStringProperty email;
    private SimpleStringProperty descripcion;
    //private SimpleObjectProperty<LocalDateTime> fechaRegistro;

    public ProveedorTableModel(int id, String nombre, String telefono, String descripcion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.descripcion = new SimpleStringProperty(descripcion);
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

    public IntegerProperty getIdProperty() {
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
