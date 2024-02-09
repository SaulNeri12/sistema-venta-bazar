module com.bazar.sistemabazar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.bazar.sistemabazar to javafx.fxml;
    exports com.bazar.sistemabazar;
}