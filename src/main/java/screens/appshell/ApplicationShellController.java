package screens.appshell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class ApplicationShellController {

    @FXML private BorderPane rootPane;
    @FXML private VBox sidebar;
    @FXML private StackPane contentArea;

    private boolean isSidebarVisible = true;

    public void initialize() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/screens/dashboard/DashboardUser.fxml")); //this should be the dashboard later
            contentArea.getChildren().setAll(loginView);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not find the FXML file!");
        }
    }
    @FXML
    public void toggleSidebar() {
        // Determine where we are going
        double targetWidth = isSidebarVisible ? 0 : 200;

        // Create the animation
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.millis(200),
                        // Animate BOTH properties at once
                        new javafx.animation.KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new javafx.animation.KeyValue(sidebar.minWidthProperty(), targetWidth)
                )
        );

        // OPTIONAL: If the sidebar contains text/buttons that "overflow"
        // while shrinking, this line hides the parts that stick out.
        if (isSidebarVisible) {
            sidebar.setClip(new javafx.scene.shape.Rectangle(sidebar.getWidth(), sidebar.getHeight()));
        } else {
            sidebar.setClip(null);
        }

        timeline.play();
        isSidebarVisible = !isSidebarVisible;
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void showHome() { loadView("/screens/home/HomeUser.fxml"); }
    @FXML public void showDashboard() { loadView("/screens/dashboard/DashboardUser.fxml"); }
    @FXML public void showSettings() { loadView("/screens/settings/Settings.fxml"); }
    @FXML public void showHistory() { loadView("/screens/history/History.fxml"); }
}
