package utilities.manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        //low-priority, probably won't get implemented
    }

    /**
     * Loads a popup FXML into an overlay container.
     * @param container The StackPane that holds the popups (e.g., paneMainContainer)
     * @param shadow The background dimming pane (e.g., paneOverlayShadow)
     * @param fxmlPath Path to the popup FXML
     * @param data The object to pass (e.g., the Equipment object)
     */
    public static <T> void showOverlay(StackPane container, Pane shadow, String fxmlPath, T data) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent popupNode = loader.load();

            // Check if the controller needs data
            Object controller = loader.getController();
            if (controller instanceof DataReceiver) {
                ((DataReceiver<T>) controller).setData(data);
            }

            // Inject into UI
            container.getChildren().setAll(popupNode);
            container.setVisible(true);
            shadow.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeOverlay(StackPane container, Pane shadow) {
        container.setVisible(false);
        shadow.setVisible(false);
        container.getChildren().clear(); // Free up memory
    }
}