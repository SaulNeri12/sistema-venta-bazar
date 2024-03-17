package com.bazar.sistemabazar.components.tables;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;


public class ProductoVentaTableView extends TableView<ProductoVentaTableView.ProductoVentaTableModel> {

    public ProductoVentaTableView() {
        super();

        // se la asigna su estilo CSS
        this.getStyleClass().add("tabla-venta");

        // se crean las columnas necesarias de la tabla
        TableColumn<ProductoVentaTableModel, Long> columnaProductoId = new TableColumn<>("ID");
        columnaProductoId.setPrefWidth(100);

        TableColumn<ProductoVentaTableModel, String> columnaNombreProducto = new TableColumn<>("Nombre del producto");
        columnaNombreProducto.setPrefWidth(250);

        TableColumn<ProductoVentaTableModel, Float> columnaPrecioProducto = new TableColumn<>("Precio");
        columnaPrecioProducto.setPrefWidth(100);

        TableColumn<ProductoVentaTableModel, Integer> columnaCantidadProducto = new TableColumn<>("Cantidad");
        columnaCantidadProducto.setPrefWidth(100);

        TableColumn<ProductoVentaTableModel, Float> columnaTotalProducto = new TableColumn<>("Total");
        columnaTotalProducto.setPrefWidth(100);

        ObservableList<ProductoVentaTableModel> listaProductos = FXCollections.observableArrayList(
                new ProductoVentaTableModel((long) 100000, "Alice Akerman", 40.21f, 1, 40.21f),
                new ProductoVentaTableModel((long) 1000001, "Saul Armando Neri Escarcega", 20.21f, 5, 101.05f)
        );

        this.getItems().addAll(listaProductos);

        // anade las columnas a la tabla
        this.getColumns().addAll(
                columnaProductoId,
                columnaNombreProducto,
                columnaPrecioProducto,
                columnaCantidadProducto,
                columnaTotalProducto
        );




    }

    public static class ProductoVentaTableModel {
        private SimpleLongProperty productoId;
        private SimpleStringProperty nombre;
        private SimpleFloatProperty precio;
        private SimpleIntegerProperty cantidad;
        private SimpleFloatProperty total;

        public ProductoVentaTableModel(long productoId, String nombre, float precio, int cantidad, float total) {
            this.productoId = new SimpleLongProperty(productoId);
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleFloatProperty(precio);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            this.total = new SimpleFloatProperty(total);
        }

        public void setId(long productoId) {
            this.productoId.set(productoId);
        }

        public void setNombre(String nombre) {
            this.nombre.set(nombre);
        }

        public void setPrecio(float precio) {
            this.precio.set(precio);
        }

        public void setCantidad(int cantidad) {
            this.cantidad.set(cantidad);
        }

        public void setTotal(float total) {
            this.total.set(total);
        }

        public long getId() {
            return this.productoId.get();
        }

        public String getNombre() {
            return this.nombre.get();
        }

        public float getPrecio() {
            return this.precio.get();
        }

        public int getCantidad() {
            return this.cantidad.get();
        }

        public float getTotal() {
            int cantidad = this.cantidad.get();
            float precio = this.precio.get();
            return cantidad * precio;
        }
    }

}
