module com.example.stackoaverflowproject {

    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;

    // OPEN your UI folders so JavaFX can read your @FXML tags
    opens screens.login to javafx.fxml;
    opens screens.register to javafx.fxml;
    opens screens.dashboard to javafx.fxml;
    opens screens.settings to javafx.fxml;
    opens screens.home to javafx.fxml;
    opens screens.history to javafx.fxml;
    opens screens.appshell to javafx.fxml;


    //EXPORT your folders so the application can run and the classes can talk to each other
    exports app;
    exports data;
    exports data.equipment;
    exports screens.login;
    exports screens.register;
    exports screens.dashboard;
    exports screens.settings;
    exports screens.home;
    exports screens.appshell;
    exports utilities.database;
    exports utilities.manager;
    exports utilities.service;
    exports screens.history;
    exports screens.popup;
    opens screens.popup to javafx.fxml;
}