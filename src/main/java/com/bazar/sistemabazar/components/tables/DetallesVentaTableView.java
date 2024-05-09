package com.bazar.sistemabazar.components.tables;

import com.bazar.sistemabazar.controllers.dialogs.DialogoOperacion;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;

import com.bazar.sistemabazar.components.tables.models.DetalleVentaTableModel;
import objetosDTO.DetalleVentaDTO;
import objetosDTO.ProductoDTO;

import java.util.Optional;


public class DetallesVentaTableView extends TableView<DetalleVentaTableModel> implements IControlTabla<DetalleVentaDTO> {

    private DialogoOperacion operacion;

    public DetallesVentaTableView(DialogoOperacion operacion) {
        super();
        inicializar();
        this.operacion = operacion;
    }

    @Override
    public void inicializar() {
        TableColumn<DetalleVentaTableModel, String> columnaProductoCodigo = new TableColumn<>("ID");
        /*
        columnaProductoCodigo.setMinWidth(80);
        columnaProductoCodigo.setMaxWidth(100);

         */
        columnaProductoCodigo.setCellValueFactory(cellData -> cellData.getValue().getCodigoProperty());

        TableColumn<DetalleVentaTableModel, String> columnaNombreProducto = new TableColumn<>("Nombre del producto");
        /*
        columnaNombreProducto.setPrefWidth(300);
        columnaNombreProducto.setMinWidth(100);
        columnaNombreProducto.setMaxWidth(300);
        */

        columnaNombreProducto.setCellValueFactory(cellData -> cellData.getValue().getNombreProperty());

        TableColumn<DetalleVentaTableModel, Float> columnaPrecioProducto = new TableColumn<>("Precio");
        /*
        columnaPrecioProducto.setPrefWidth(25);
        columnaPrecioProducto.setMinWidth(25);
        columnaPrecioProducto.setMaxWidth(30);

         */
        columnaPrecioProducto.setCellValueFactory(cellData -> cellData.getValue().getPrecioProperty().asObject());

        TableColumn<DetalleVentaTableModel, Integer> columnaCantidadProducto = new TableColumn<>("Cantidad");
        /*
        columnaCantidadProducto.setPrefWidth(25);
        columnaCantidadProducto.setMinWidth(25);
        columnaCantidadProducto.setMaxWidth(25);

         */
        columnaCantidadProducto.setCellValueFactory(cellData -> cellData.getValue().getCantidadProperty().asObject());


        TableColumn<DetalleVentaTableModel, Float> columnaTotalProducto = new TableColumn<>("Total");
        /*
        columnaTotalProducto.setPrefWidth(25);
        columnaTotalProducto.setMaxWidth(25);
        columnaTotalProducto.setCellValueFactory(cellData -> cellData.getValue().getTotalProperty().asObject());


         */
        // ajustes adicionales
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.setPlaceholder(null);

        TableColumn<DetalleVentaTableModel, Void> columnaControl = new TableColumn<>("");
        columnaControl.setPrefWidth(100);
        columnaControl.setMinWidth(100);
        columnaControl.setMaxWidth(100);
        columnaControl.setCellFactory(param -> new TableCell<>() {
            final Button btn = new Button("Eliminar");
            {
                btn.setOnAction(event -> {
                    DetalleVentaTableModel producto = getTableView().getItems().get(getIndex());
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

        if (this.operacion != DialogoOperacion.ACTUALIZAR) {
            this.getColumns().remove(this.getColumns().size() - 1);
        }
        //
    }

    @Override
    public DetalleVentaDTO obtenerFilaObjetoSeleccionado() {
        DetalleVentaTableModel seleccionado = this.getSelectionModel().getSelectedItem();

        if (seleccionado == null)
            return null;

        ProductoDTO producto = new ProductoDTO();

        //producto.setCodigoBarras(null);
        producto.setCodigoInterno(seleccionado.getCodigoProperty().get());
        producto.setNombre(seleccionado.getNombreProperty().get());
        producto.setPrecio(seleccionado.getPrecioProperty().get());
        //producto.setFechaRegistro(null);
        producto.setPrecio(seleccionado.getPrecioProperty().get());

        DetalleVentaDTO productoVenta = new DetalleVentaDTO();

        productoVenta.setCantidad(seleccionado.getCantidadProperty().get());
        productoVenta.setPrecioProducto(producto.getPrecio());
        productoVenta.setProducto(producto);

        return productoVenta;
    }



    @Override
    public DetalleVentaDTO obtenerFilaObjetoPorIndice(int indice) {
        return null;
    }

    @Override
    public void agregarFilaObjeto(DetalleVentaDTO productoVenta) {
        Optional<DetalleVentaTableModel> productoEnTabla = this.getItems()
                .stream()
                .filter(p -> p.getCodigoProperty().get().equals(productoVenta.getProducto().getCodigoInterno()))
                .findFirst();

        if (productoEnTabla.isPresent()) {
            productoEnTabla
                    .get()
                    .setCantidad(
                            productoEnTabla.get().getCantidadProperty().get() + productoVenta.getCantidad()
                    );
            return;
        }

        DetalleVentaTableModel filaProducto = new DetalleVentaTableModel(
                productoVenta.getProducto().getCodigoInterno(),
                productoVenta.getProducto().getNombre(),
                productoVenta.getPrecioProducto(),
                productoVenta.getCantidad()
        );

        this.getItems().add(filaProducto);
    }

    @Override
    public boolean eliminarFilaObjeto(DetalleVentaDTO productoVenta) {
        return this.getItems().removeIf(p -> p.getCodigoProperty().get().equals(productoVenta.getProducto().getCodigoInterno()));
    }

    @Override
    public boolean eliminarFilaObjetoPorIndice(int indice) {
        return this.getItems().remove(indice) != null;
    }

    @Override
    public void eliminarTodasLasFilas() {
        this.getItems().clear();
    }

    public DetallesVentaTableView() {
        super();
        this.operacion = DialogoOperacion.ACTUALIZAR;
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
