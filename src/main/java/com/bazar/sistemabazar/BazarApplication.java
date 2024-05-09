package com.bazar.sistemabazar;

import com.bazar.sistemabazar.adaptador.principal.AdaptadorUsuarioGUICajero;
import com.bazar.sistemabazar.adaptador.principal.AdaptadorUsuarioGUIGerente;
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
import objetosDTO.UsuarioDTO;
import persistencia.IPersistenciaBazar;
import persistencia.PersistenciaBazar;

import java.io.IOException;

public class BazarApplication extends Application {

    private IPersistenciaBazar persistencia;

    public BazarApplication() {
        try {
            this.persistencia = new PersistenciaBazar();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
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


        // inicializacion del frame principal
        UsuarioDTO usuarioLogeado = loginController.obtenerUsuarioLogeado();

        /*** System.out.println(usuarioLogeado);*/

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

        BazarController controladorBazar = bazarFxmlLoader.getController();

        // adapta la interfaz
        switch (usuarioLogeado.getPuesto()) {
            case CAJERO -> {
                new AdaptadorUsuarioGUICajero().ejecutar(controladorBazar);
            }
            case GERENTE -> {
                new AdaptadorUsuarioGUIGerente().ejecutar(controladorBazar);
            }
            case ADMIN -> {
                // nada...
            }
        }

        stage.show(); // se muestra el frame principal
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
