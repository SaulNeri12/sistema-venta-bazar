package com.bazar.sistemabazar.components.tables;

import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import objetosNegocio.Producto;

public class ProductosTableView extends TableView<ProductoTableModel> implements IControlTabla<Producto> {


    @Override
    public void inicializar() {

        TableColumn<ProductoTableModel, String> columnaProductoCodigo = new TableColumn<>("Codigo");
        columnaProductoCodigo.setMinWidth(80);
        columnaProductoCodigo.setMaxWidth(100);
        columnaProductoCodigo.setCellValueFactory(cellData -> cellData.getValue().getCodigoProperty());

        TableColumn<ProductoTableModel, String> columnaNombreProducto = new TableColumn<>("Nombre del producto");
        columnaNombreProducto.setMinWidth(200);
        columnaNombreProducto.setMaxWidth(250);
        columnaNombreProducto.setPrefWidth(200);
        columnaNombreProducto.setCellValueFactory(cellData -> cellData.getValue().getNombreProperty());

        TableColumn<ProductoTableModel, Float> columnaPrecioProducto = new TableColumn<>("Precio");
        columnaPrecioProducto.setPrefWidth(100);
        columnaPrecioProducto.setCellValueFactory(cellData -> cellData.getValue().getPrecioProperty().asObject());

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.setPlaceholder(null);

        this.getColumns().addAll(
                columnaProductoCodigo,
                columnaNombreProducto,
                columnaPrecioProducto
        );
    }

    @Override
    public Producto obtenerFilaObjetoSeleccionado() {
        ProductoTableModel seleccionado = this.getSelectionModel().getSelectedItem();

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
    public Producto obtenerFilaObjeto(Producto producto) {
        ProductoTableModel filaProducto = this.getItems()
                .stream()
                .filter(p -> p.getCodigoProperty().get().equals(producto.getCodigo()))
                .findFirst().get();

        if (filaProducto == null)
            return null;

        Producto productoEncontrado = new Producto();

        productoEncontrado.setCodigo(filaProducto.getCodigoProperty().get());
        productoEncontrado.setNombre(filaProducto.getNombreProperty().get());
        productoEncontrado.setPrecio(filaProducto.getPrecioProperty().get());
        productoEncontrado.setFechaRegistro(filaProducto.getFechaRegistroProperty().get());

        return productoEncontrado;
    }

    @Override
    public Producto obtenerFilaObjetoPorIndice(int indice) {
        ProductoTableModel filaProducto = this.getItems().get(indice);

        if (filaProducto == null)
            return null;

        Producto productoEncontrado = new Producto();

        productoEncontrado.setCodigo(filaProducto.getCodigoProperty().get());
        productoEncontrado.setNombre(filaProducto.getNombreProperty().get());
        productoEncontrado.setPrecio(filaProducto.getPrecioProperty().get());
        productoEncontrado.setFechaRegistro(filaProducto.getFechaRegistroProperty().get());

        return productoEncontrado;
    }

    @Override
    public void agregarFilaObjeto(Producto producto) {
        boolean productoEnTabla = this.getItems()
                .stream()
                .anyMatch(p -> p.getCodigoProperty().get().equals(producto.getCodigo()));

        if (productoEnTabla) return;

        ProductoTableModel filaProducto = new ProductoTableModel(
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getFechaRegistro()
        );

        this.getItems().add(filaProducto);
    }

    @Override
    public boolean eliminarFilaObjeto(Producto producto) {
        return this.getItems().removeIf(p -> p.getCodigoProperty().get().equals(producto.getCodigo());
    }

    @Override
    public boolean eliminarFilaObjetoPorIndice(int indice) {
        return this.getItems().remove(indice) != null;
    }

    @Override
    public boolean eliminarTodasLasFilas() {
        this.getItems().clear();
    }

    public ProductosTableView() {
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
