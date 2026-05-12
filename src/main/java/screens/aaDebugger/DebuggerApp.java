package screens.aaDebugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import screens.dashboard.DashboardAdminApplication;

import java.io.IOException;

public class DebuggerApp extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(DebuggerApp.class.getResource("/screens/settings/settings.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 640);
        stage.setTitle("WILDInv");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }
}
