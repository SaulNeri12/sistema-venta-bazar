package com.bazar.sistemabazar.components.tables;

//import com.bazar.sistemabazar.components.tables.models.ProductoVentaTableModel;
import com.bazar.sistemabazar.components.tables.models.ProveedorTableModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

// TODO: HACER QUE IMPLEMENTE ICONTROLTABLA
public class ProveedoresTableView extends TableView<ProveedorTableModel> {

    public void inicialiar() {
        TableColumn<ProveedorTableModel, Integer> columnaProveedorId = new TableColumn<>("ID");
        columnaProveedorId.setMaxWidth(100);
        columnaProveedorId.setMinWidth(80);
        columnaProveedorId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());

        TableColumn<ProveedorTableModel, String> columnaProveedorNombre = new TableColumn<>("Nombre");
        columnaProveedorNombre.setMaxWidth(300);
        columnaProveedorNombre.setMinWidth(250);
        columnaProveedorNombre.setCellValueFactory(cellData -> cellData.getValue().getNombreProperty());

        TableColumn<ProveedorTableModel, String> columnaProveedorTelefono = new TableColumn<>("Telefono");
        columnaProveedorTelefono.setMinWidth(150);
        columnaProveedorTelefono.setMaxWidth(200);
        columnaProveedorTelefono.setCellValueFactory(cellData -> cellData.getValue().getTelefonoProperty());

        /*
        TableColumn<ProveedorTableModel, String> columnaProveedorDesc = new TableColumn<>("Descripcion");
        columnaProveedorDesc.setMinWidth(300);
        columnaProveedorDesc.setMaxWidth(400);
        columnaProveedorDesc.setCellValueFactory(cellData -> cellData.getValue().getDescripcionProperty());
        */

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.getColumns().addAll(
                columnaProveedorId,
                columnaProveedorNombre,
                columnaProveedorTelefono
        );

        //this.getColumns().remove(this.getColumns().size() - 1);
        this.setPlaceholder(null);
    }

    public ProveedoresTableView() {
        super();
        inicialiar();
    }


}
