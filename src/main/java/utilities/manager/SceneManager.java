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
    private static StackPane globalOverlayContainer;
    private static Pane globalShadow;

    public static void setOverlayComponents(StackPane container, Pane shadow) {
        globalOverlayContainer = container;
        globalShadow = shadow;
    }

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

    public static <T> void showOverlay(String fxmlPath, T data) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent popupNode = loader.load();

            // Pass data if the popup implements DataReceiver (e.g., UpdatePopup)
            // this is if the popup needs data to be displayed (e.g., EquipmentName)
            if (data != null && loader.getController() instanceof DataReceiver) {
                ((DataReceiver<T>) loader.getController()).setData(data);
            }

            globalOverlayContainer.getChildren().setAll(popupNode);
            globalOverlayContainer.setVisible(true);
            globalShadow.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeOverlay() {
        if (globalOverlayContainer != null) {
            globalOverlayContainer.setVisible(false);
            globalShadow.setVisible(false);
            globalOverlayContainer.getChildren().clear();
        }
    }

    public static void pauseScene(int millis) {
        //low-priority, probably won't get implemented
    }
}