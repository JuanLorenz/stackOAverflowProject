package screens.userShell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/userShell/UserShell.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Capstone");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1066);
            primaryStage.setMinHeight(600);
            primaryStage.setMaxWidth(1920);
            primaryStage.setMaxHeight(1080);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Failed to load the starting FXML file.");
            e.printStackTrace();
        }
    }
}
