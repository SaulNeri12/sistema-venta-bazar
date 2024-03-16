package com.bazar.sistemabazar.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PanelVentaController implements Initializable {

    @FXML
    private AnchorPane controlVentaAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.controlVentaAnchorPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
