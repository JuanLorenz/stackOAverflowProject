module com.example.stackoaverflowproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens dashboardAndComponents to javafx.fxml;
    exports dashboardAndComponents;
}