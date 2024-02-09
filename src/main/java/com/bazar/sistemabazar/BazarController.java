package com.bazar.sistemabazar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BazarController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}