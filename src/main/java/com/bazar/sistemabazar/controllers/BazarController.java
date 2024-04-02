package com.bazar.sistemabazar.controllers;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.controllers.dialogs.BuscarProveedorDlgController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BazarController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private AnchorPane panelVenta;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onMenuProveedoresBuscarProveedorClick() {
        FXMLLoader buscarProveedorDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/BuscarProveedorDlg.fxml"));
        Parent root = null;

        try {
            root = buscarProveedorDlgFxmlLoader.load();
        } catch (IOException e) {
            // TODO: mostrar mensaje de error
            throw new RuntimeException(e);
        }

        // configuracion del dialogo...
        Stage buscarProveedorDlgStage = new Stage();
        BuscarProveedorDlgController buscarProveedorController = buscarProveedorDlgFxmlLoader.getController();
        buscarProveedorController.setStage(buscarProveedorDlgStage);
        buscarProveedorDlgStage.setScene(new Scene(root));
        buscarProveedorDlgStage.showAndWait();



    }

}