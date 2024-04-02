package com.bazar.sistemabazar.components.tables;

//import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import com.bazar.sistemabazar.components.tables.models.ProveedorTableModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProveedoresTableView extends TableView<ProveedorTableModel> {

    public void inicialiar() {
        TableColumn<ProveedorTableModel, Integer> columnaProveedorId = new TableColumn<>("ID");
        columnaProveedorId.setMaxWidth(100);
        columnaProveedorId.setMinWidth(80);
        columnaProveedorId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());

        TableColumn<ProveedorTableModel, String> columnaProveedorNombre = new TableColumn<>("Nombre");
        columnaProveedorNombre.setMaxWidth(100);
        columnaProveedorNombre.setMinWidth(80);
        columnaProveedorNombre.setCellValueFactory(cellData -> cellData.getValue().getNombreProperty());

        TableColumn<ProveedorTableModel, String> columnaProveedorTelefono = new TableColumn<>("Telefono");
        columnaProveedorTelefono.setMinWidth(100);
        columnaProveedorTelefono.setMaxWidth(120);
        columnaProveedorTelefono.setCellValueFactory(cellData -> cellData.getValue().getTelefonoProperty());

        TableColumn<ProveedorTableModel, String> columnaProveedorDesc = new TableColumn<>("Descripcion");
        columnaProveedorDesc.setMinWidth(100);
        columnaProveedorDesc.setMaxWidth(200);
        columnaProveedorDesc.setCellValueFactory(cellData -> cellData.getValue().getDescripcionProperty());

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.getColumns().addAll(
                columnaProveedorId,
                columnaProveedorNombre,
                columnaProveedorTelefono,
                columnaProveedorDesc
        );

        this.setPlaceholder(null);
    }

    public ProveedoresTableView() {
        super();
        inicialiar();
    }


}
