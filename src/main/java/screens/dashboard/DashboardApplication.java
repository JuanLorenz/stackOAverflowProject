package screens.dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DashboardApplication.class.getResource("DashboardUser.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 420);
        stage.setTitle("WILDInv");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
