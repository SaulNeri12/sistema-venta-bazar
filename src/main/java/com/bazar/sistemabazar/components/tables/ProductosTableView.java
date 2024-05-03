package com.bazar.sistemabazar.components.tables;

import com.bazar.sistemabazar.components.tables.models.ProductoTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import objetosNegocio.ProductoDTO;

import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.Optional;

public class ProductosTableView extends TableView<ProductoTableModel> implements IControlTabla<ProductoDTO> {


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

        //this.setPlaceholder(null);

        this.getColumns().addAll(
                columnaProductoCodigo,
                columnaNombreProducto,
                columnaPrecioProducto
        );
    }

    @Override
    public ProductoDTO obtenerFilaObjetoSeleccionado() {
        ProductoTableModel seleccionado = this.getSelectionModel().getSelectedItem();

        if (seleccionado == null)
            return null;

        ProductoDTO producto = new ProductoDTO();

        producto.setCodigoBarras(null);
        producto.setCodigoInterno(seleccionado.getCodigoProperty().get());
        producto.setNombre(seleccionado.getNombreProperty().get());
        producto.setPrecio(seleccionado.getPrecioProperty().get());
        producto.setFechaRegistro(seleccionado.getFechaRegistroProperty().get());

        return producto;
    }

    @Override
    public ProductoDTO obtenerFilaObjetoPorIndice(int indice) {
        ProductoTableModel filaProducto = this.getItems().get(indice);

        if (filaProducto == null)
            return null;

        ProductoDTO productoEncontrado = new ProductoDTO();

        productoEncontrado.setCodigoInterno(filaProducto.getCodigoProperty().get());
        productoEncontrado.setNombre(filaProducto.getNombreProperty().get());
        productoEncontrado.setPrecio(filaProducto.getPrecioProperty().get());
        productoEncontrado.setFechaRegistro(filaProducto.getFechaRegistroProperty().get());

        return productoEncontrado;
    }



    @Override
    public void agregarFilaObjeto(ProductoDTO producto) {
        boolean productoEnTabla = this.getItems()
                .stream()
                .anyMatch(p -> p.getCodigoProperty().get().equals(producto.getCodigoInterno()));

        if (productoEnTabla) return;

        ProductoTableModel filaProducto = new ProductoTableModel(
                producto.getCodigoInterno(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getFechaRegistro()
        );

        this.getItems().add(filaProducto);
    }

    @Override
    public boolean eliminarFilaObjeto(ProductoDTO producto) {
        return this.getItems().removeIf(p -> p.getCodigoProperty().get().equals(producto.getCodigoInterno()));
    }

    @Override
    public boolean eliminarFilaObjetoPorIndice(int indice) {
        return this.getItems().remove(indice) != null;
    }

    @Override
    public void eliminarTodasLasFilas() {
        this.getItems().clear();
    }

    public ProductosTableView() {
        super();
        inicializar();
    }

}
