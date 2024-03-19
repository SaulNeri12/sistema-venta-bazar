package com.bazar.sistemabazar.components.tables;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;


public class ProductoVentaTableView extends TableView<ProductoVentaTableView.ProductoVentaTableModel> {

    private void inicializar() {
        // se la asigna su estilo CSS
        this.getStyleClass().add("table-view-venta");

        // se crean las columnas necesarias de la tabla
        TableColumn<ProductoVentaTableModel, Integer> columnaProductoId = new TableColumn<>("ID");
        columnaProductoId.setPrefWidth(100);
        columnaProductoId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());

        TableColumn<ProductoVentaTableModel, String> columnaNombreProducto = new TableColumn<>("Nombre del producto");
        columnaNombreProducto.setPrefWidth(250);
        columnaNombreProducto.setCellValueFactory(cellData -> cellData.getValue().getNombreProperty());

        TableColumn<ProductoVentaTableModel, Float> columnaPrecioProducto = new TableColumn<>("Precio");
        columnaPrecioProducto.setPrefWidth(100);
        columnaPrecioProducto.setCellValueFactory(cellData -> cellData.getValue().getPrecioProperty().asObject());

        TableColumn<ProductoVentaTableModel, Integer> columnaCantidadProducto = new TableColumn<>("Cantidad");
        columnaCantidadProducto.setPrefWidth(100);
        columnaCantidadProducto.setCellValueFactory(cellData -> cellData.getValue().getCantidadProperty().asObject());

        TableColumn<ProductoVentaTableModel, Float> columnaTotalProducto = new TableColumn<>("Total");
        columnaTotalProducto.setPrefWidth(100);
        columnaTotalProducto.setCellValueFactory(cellData -> cellData.getValue().getTotalProperty().asObject());

        // anade las columnas a la tabla
        this.getColumns().addAll(
                columnaProductoId,
                columnaNombreProducto,
                columnaPrecioProducto,
                columnaCantidadProducto,
                columnaTotalProducto
        );
    }

    public ProductoVentaTableView() {
        super();
        inicializar();

        ObservableList<ProductoVentaTableModel> listaProductos = FXCollections.observableArrayList(
                new ProductoVentaTableModel(12, "Ruffles Queso (100g)", 40.21f, 1, 40.21f),
                new ProductoVentaTableModel(13, "Galletas Emperador", 20.21f, 5, 101.05f)
        );

        this.getItems().addAll(listaProductos);
    }

    public static class ProductoVentaTableModel {
        private SimpleIntegerProperty productoId;
        private SimpleStringProperty nombre;
        private SimpleFloatProperty precio;
        private SimpleIntegerProperty cantidad;
        private SimpleFloatProperty total;

        public ProductoVentaTableModel(int productoId, String nombre, float precio, int cantidad, float total) {
            this.productoId = new SimpleIntegerProperty(productoId);
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleFloatProperty(precio);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            this.total = new SimpleFloatProperty(total);
        }

        public void setId(int productoId) {
            this.productoId.set(productoId);
        }

        public void setNombre(String nombre) {
            this.nombre.set(nombre);
        }

        public void setPrecio(float precio) {
            this.precio.set(precio);
            this.calcularTotal();
        }

        private void calcularTotal() {
            float total = this.precio.get() * this.cantidad.get();
            this.total.set(total);
        }

        public void setTotal(float total) {
            calcularTotal();
            this.total.set(total);
        }

        public IntegerProperty getIdProperty() {
            return this.productoId;
        }

        public StringProperty getNombreProperty() {
            return this.nombre;
        }

        public FloatProperty getPrecioProperty() {
            return this.precio;
        }

        public IntegerProperty getCantidadProperty() {
            return this.cantidad;
        }

        public FloatProperty getTotalProperty() {
            return this.total;
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
            return this.total.get();
        }
    }

}
