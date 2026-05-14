package app;

import data.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.manager.SerializeManager;

import java.io.IOException;

public class EquipLinkApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        User currentUser = SerializeManager.deserializeUser();

        try {
            FXMLLoader loader;

            if(currentUser == null)
                 loader = new FXMLLoader(getClass().getResource("/screens/login/Login.fxml"));
            else if(currentUser.getUserType().equals("admin"))
                loader = new FXMLLoader(getClass().getResource("/screens/dashboard/dashboardAdmin.fxml"));
            else
                loader = new FXMLLoader(getClass().getResource("/screens/home/HomeUser.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("EquipLink");
            primaryStage.setScene(scene);

            // It's good practice to keep minimum dimensions
            primaryStage.setMinWidth(1066);
            primaryStage.setMinHeight(600);

            primaryStage.setMaximized(true);
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