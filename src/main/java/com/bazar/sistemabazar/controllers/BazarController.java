package com.bazar.sistemabazar.controllers;

import com.bazar.sistemabazar.BazarApplication;
import com.bazar.sistemabazar.controllers.dialogs.BuscarProveedorDlgController;
import com.bazar.sistemabazar.controllers.dialogs.BuscarVentaDlgController;
import com.bazar.sistemabazar.controllers.dialogs.DialogoOperacion;
import com.bazar.sistemabazar.controllers.dialogs.ProductoDlgController;
import com.bazar.sistemabazar.controllers.panels.PanelVentaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import objetosNegocio.UsuarioDTO;
import persistencia.IPersistenciaBazar;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

public class BazarController implements Initializable {

    private IPersistenciaBazar persistencia;
    private UsuarioDTO usuario;

    @FXML
    private VBox panelVentaContainer;
    // menu ventas
    public Menu menuVentas;
    public MenuItem menuVentasConsultarTodos;
    public MenuItem menuVentasBuscarVenta;
    public MenuItem menuVentasModificarVenta;
    public MenuItem menuVentasEliminarVenta;
    // menu productos
    public Menu menuProductos;
    public MenuItem menuProductosBuscarProducto;
    public MenuItem menuProductosAgregarProducto;
    public MenuItem menuProductosModificarProducto;
    public MenuItem menuProductosEliminarProducto;
    // menu usuarios
    public Menu menuUsuarios;
    public MenuItem menuUsuariosBuscarUsuario;
    public MenuItem menuUsuariosAgregarUsuario;
    public MenuItem menuUsuariosModificarUsuario;
    public MenuItem menuUsuariosEliminarUsuario;
    // menu proveedores
    public Menu menuProveedores;
    public MenuItem menuProveedoresBuscarProveedor;
    public MenuItem menuProveedoresAgregarProveedor;
    public MenuItem menuProveedoresModificarProveedor;
    public MenuItem menuProveedoresEliminarProveedor;
    // menu inventario
    public Menu menuInventario;
    public MenuItem menuInventarioBuscarProducto;
    public MenuItem menuInventarioRegistrarExistenciaProducto;


    public BazarController(IPersistenciaBazar persistencia, UsuarioDTO usuario) {
        this.persistencia = persistencia;
        this.usuario = usuario;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader panelVentaLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/panels/PanelVenta.fxml"));

        // aqui se inyecta la dependencia de ventas
        panelVentaLoader.setControllerFactory(c -> {
            return new PanelVentaController(this.persistencia, this.usuario);
        });

        AnchorPane nuevoPanelVenta = null;

        try {
            nuevoPanelVenta = panelVentaLoader.load();
            panelVentaContainer.getChildren().add(nuevoPanelVenta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        buscarProveedorDlgStage.initModality(Modality.APPLICATION_MODAL);
        buscarProveedorDlgStage.showAndWait();
    }

    @FXML
    public void mostrarDialogoVentas() {
        FXMLLoader buscarVentaDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/BuscarVentaDlg.fxml"));

        buscarVentaDlgFxmlLoader.setControllerFactory(c -> {
            return new BuscarVentaDlgController(persistencia, usuario);
        });

        Parent root = null;

        try {
            root = buscarVentaDlgFxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage ventasDlgStage = new Stage();

        BuscarVentaDlgController ventaDlgController = buscarVentaDlgFxmlLoader.getController();
        ventaDlgController.setStage(ventasDlgStage);

        Scene scene = new Scene(root);
        ventasDlgStage.setMaximized(false);
        ventasDlgStage.setScene(scene);
        ventasDlgStage.setTitle("Ventas");

        ventasDlgStage.show();
    }

    public void registrarNuevoProducto(ActionEvent actionEvent) {
        FXMLLoader registrarProductoDlgFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/components/dialogs/ProductoDlg.fxml"));

        registrarProductoDlgFxmlLoader.setControllerFactory(c -> {
            return new ProductoDlgController(persistencia, DialogoOperacion.REGISTRAR);
        });

        Parent root = null;

        try {
            root = registrarProductoDlgFxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage registrarProductoDlgStage = new Stage();

        ProductoDlgController productoDlgController = registrarProductoDlgFxmlLoader.getController();
        productoDlgController.setStage(registrarProductoDlgStage);

        Scene scene = new Scene(root);
        registrarProductoDlgStage.setMaximized(false);
        registrarProductoDlgStage.setScene(scene);
        // NOTE: Cambiar segun operacion...
        registrarProductoDlgStage.setTitle("Registrar Producto");

        registrarProductoDlgStage.show();

    }

}