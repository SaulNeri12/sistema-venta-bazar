package com.bazar.sistemabazar.controllers;

import com.bazar.sistemabazar.components.tables.ProductoVentaTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PanelVentaController implements Initializable {

    @FXML
    public TabPane ventasTabPane;
    public VBox tablaVentaPane;
    @FXML
    private AnchorPane controlVentaAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // la tabla principal de ventas se anade al panel de venta
        ProductoVentaTableView tablaVenta = new ProductoVentaTableView();
        tablaVentaPane.getChildren().add(tablaVenta);


    }
}
