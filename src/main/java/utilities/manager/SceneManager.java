package utilities.manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            // Pass the exact path based on the feature (e.g., "/screens/login/login.fxml")
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource(fxmlPath)));

            // 2. Get the current Scene from the button that was clicked
            Scene currentScene = ((Node) event.getSource()).getScene();

            // 3. THE FIX: Just replace the content inside the current scene!
            currentScene.setRoot(newRoot);

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Failed to load the FXML file at: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void pauseScene(int millis) {

    }
}