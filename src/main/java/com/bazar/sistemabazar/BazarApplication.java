package com.bazar.sistemabazar;

import com.bazar.sistemabazar.controllers.BazarController;
import com.bazar.sistemabazar.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Screen;
import objetosNegocio.Usuario;
import persistencia.IPersistenciaBazar;
import persistencia.PersistenciaBazarListas;

import java.io.IOException;

public class BazarApplication extends Application {

    private IPersistenciaBazar persistencia;

    public BazarApplication() {
        this.persistencia = new PersistenciaBazarListas();
    }

    @Override
		// cambio
    public void start(Stage stage) throws IOException {



        // se mostrara el login
        FXMLLoader loginFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/Login.fxml"));

        // se le asigna el subsistema usuarios...
        loginFxmlLoader.setControllerFactory(c -> {
            return new LoginController(persistencia);
        });

        Parent root = loginFxmlLoader.load();

        LoginController loginController = loginFxmlLoader.getController();

        Stage loginStage = new Stage();

        //LoginController loginController = loginFxmlLoader.getController();
        loginController.setStage(loginStage);

        // si se cierra la ventana del login, no se abre la ventana de la aplicacion
        // y se termina el programa
        loginStage.setOnCloseRequest(event -> {
            loginStage.close();
            System.exit(0);
        });

        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(root));

        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.showAndWait();
        centerStage(loginStage);


        Usuario usuarioLogeado = loginController.obtenerUsuarioLogeado();

        /***/ System.out.println(usuarioLogeado);

        // se mostrara el login
        FXMLLoader bazarFxmlLoader = new FXMLLoader(BazarApplication.class.getResource("fxml/app.fxml"));

        bazarFxmlLoader.setControllerFactory(c -> {
            return new BazarController(persistencia, usuarioLogeado);
        });

        Parent bazarRoot = bazarFxmlLoader.load();
        Scene scene = new Scene(bazarRoot);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("Sistema Bazar");


        stage.show();
    }

    private void centerStage(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        double centerX = (primaryScreenBounds.getWidth() - stageWidth) / 2;
        double centerY = (primaryScreenBounds.getHeight() - stageHeight) / 2;

        stage.setX(centerX);
        stage.setY(centerY);
    }

    public static void main(String[] args) {
        launch();
    }
}
