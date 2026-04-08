module com.example.stackoaverflowproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.stackoaverflowproject to javafx.fxml;
    exports com.example.stackoaverflowproject;
}