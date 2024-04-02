package com.bazar.sistemabazar.components.tables;

import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;

import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import objetosNegocio.Producto;

public class ProductosVentaTableView extends TableView<ProductoVentaTableModel> implements IControlTabla<De> {

    public void inicializar() {
        TableColumn<ProductoVentaTableModel, String> columnaProductoCodigo = new TableColumn<>("ID");
        columnaProductoCodigo.setMinWidth(80);
        columnaProductoCodigo.setMaxWidth(100);
        columnaProductoCodigo.setCellValueFactory(cellData -> cellData.getValue().getCodigoProperty());

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
                columnaProductoCodigo,
                columnaNombreProducto,
                columnaPrecioProducto,
                columnaCantidadProducto,
                columnaTotalProducto,
                columnaControl
        );
    }

    @Override
    public ProductoVentaTableModel obtenerFilaObjetoSeleccionado() {
        ProductoVentaTableModel seleccionado = this.getSelectionModel().getSelectedItem();

        if (seleccionado == null)
            return null;

        Producto producto = new Producto();

        producto.setId(null);
        producto.setCodigo(seleccionado.getCodigoProperty().get());
        producto.setNombre(seleccionado.getNombreProperty().get());
        producto.setPrecio(seleccionado.getPrecioProperty().get());
        producto.setFechaRegistro(seleccionado.getFechaRegistroProperty().get());

        return producto;
    }

    @Override
    public ProductoVentaTableModel obtenerFilaObjeto(ProductoVentaTableModel producto) {
        return null;
    }

    @Override
    public ProductoVentaTableModel obtenerFilaObjetoPorIndice(int indice) {
        return null;
    }

    @Override
    public void agregarFilaObjeto(ProductoVentaTableModel producto) {

    }

    @Override
    public boolean eliminarFilaObjeto(ProductoVentaTableModel producto) {
        return false;
    }

    @Override
    public boolean eliminarFilaObjetoPorIndice(int indice) {
        return false;
    }

    @Override
    public boolean eliminarTodasLasFilas() {
        return false;
    }

    public ProductosVentaTableView() {
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
