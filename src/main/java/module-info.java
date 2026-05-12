module com.example.stackoaverflowproject {

    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires java.sql;

    // OPEN your UI folders so JavaFX can read your @FXML tags
    opens screens.login to javafx.fxml;
    opens screens.register to javafx.fxml;
    opens screens.dashboard to javafx.fxml;
    opens screens.settings to javafx.fxml;
    opens screens.home to javafx.fxml;

    //EXPORT your folders so the application can run and the classes can talk to each other
    exports screens.login;
    exports screens.register;
    exports screens.dashboard;
    exports screens.settings;
    exports screens.home;
    exports app;
    exports utilities.sqlRelated;
    exports utilities.serializationRelated;
    exports data;
    exports utilities.javafxRelated;
}