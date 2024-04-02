module com.bazar.sistemabazar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires ObjetosNegocioBazar;

    opens com.bazar.sistemabazar to javafx.fxml;
    exports com.bazar.sistemabazar;
    exports com.bazar.sistemabazar.controllers;
    opens com.bazar.sistemabazar.controllers to javafx.fxml;
    exports com.bazar.sistemabazar.controllers.panels;
    opens com.bazar.sistemabazar.controllers.panels to javafx.fxml;
    exports com.bazar.sistemabazar.controllers.dialogs;
    opens com.bazar.sistemabazar.controllers.dialogs to javafx.fxml;
}