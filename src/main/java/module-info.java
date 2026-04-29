module com.example.stackoaverflowproject {

    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires java.sql; // hashing algorithm

    // OPEN your UI folders so JavaFX can read your @FXML tags
    opens auth.Login to javafx.fxml;
    opens auth.Register to javafx.fxml;
    opens dashboardAndComponents to javafx.fxml;

    //EXPORT your folders so the application can run and the classes can talk to each other
    exports auth.Login;
    exports auth.Register;
    exports dashboardAndComponents;
    exports shared;
    exports app;
}