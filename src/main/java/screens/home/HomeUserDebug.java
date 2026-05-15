package screens.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeUserDebug extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeUser.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("HomeUser Debug");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(960);
            primaryStage.setMinHeight(640);
            primaryStage.setMaxWidth(1920);
            primaryStage.setMaxHeight(1080);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Failed to load the starting FXML file.");
            e.printStackTrace();
        }
    }
}
