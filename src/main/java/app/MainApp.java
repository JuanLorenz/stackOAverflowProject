package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
// import shared.DatabaseInitializer; // Uncomment if you are using the DB setup class!

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/login/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Capstone");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Failed to load the starting FXML file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}