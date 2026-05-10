package utilities.javafxRelated;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationUtils {

    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {

            // Now, you pass the exact path based on the feature (e.g., "/screens.auth/login/login.fxml")
            Parent root = FXMLLoader.load(NavigationUtils.class.getResource(fxmlPath));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Failed to load the FXML file at: " + fxmlPath);
            e.printStackTrace(); // Always print the error so you know exactly why it crashed
        }
    }
}