package com.bazar.sistemabazar.components.tables;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import javafx.scene.input.KeyCode;

public class ProductoVentaTableView extends TableView<ProductoVentaTableModel> {

    private void inicializar() {
        // se la asigna su estilo CSS
        this.getStyleClass().add("table-view-venta");

        // se crean las columnas necesarias de la tabla
        TableColumn<ProductoVentaTableModel, Integer> columnaProductoId = new TableColumn<>("ID");
        columnaProductoId.setMinWidth(80);
        columnaProductoId.setMaxWidth(100);
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

        // ajustes adicionales
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.setPlaceholder(null);

        TableColumn<ProductoVentaTableModel, Void> columnaControl = new TableColumn<>("");
        columnaControl.setPrefWidth(100);
        columnaControl.setMinWidth(100);
        columnaControl.setMaxWidth(100);
        columnaControl.setCellFactory(param -> new TableCell<>() {
            final Button btn = new Button("Eliminar");
            {
                btn.setOnAction(event -> {
                    ProductoVentaTableModel producto = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(producto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        // anade las columnas a la tabla
        this.getColumns().addAll(
                columnaProductoId,
                columnaNombreProducto,
                columnaPrecioProducto,
                columnaCantidadProducto,
                columnaTotalProducto,
                columnaControl
        );
    }

    public ProductoVentaTableView() {
        super();
        inicializar();

        /*
        ObservableList<ProductoVentaTableModel> listaProductos = FXCollections.observableArrayList(
                new ProductoVentaTableModel(12, "Ruffles Queso (100g)", 40.21f, 1),
                new ProductoVentaTableModel(13, "Galletas Emperador", 20.21f, 5)
        );


        this.getItems().addAll(listaProductos);

         */
    }

}
