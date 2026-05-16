package screens.appshell;

import data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import utilities.manager.SerializeManager;

import java.io.IOException;
import java.util.Objects;

public class ApplicationShellController {

    @FXML public ImageView ivProfilePic;
    @FXML public Label labelUserName;

    @FXML private BorderPane rootPane;
    @FXML private VBox sidebar;
    @FXML private VBox sidebarContent;
    @FXML private StackPane contentArea;

    @FXML private Button btnHome;
    @FXML private Button btnDashboard;
    @FXML private Button btnSettings;
    @FXML private Button btnRecords;

    private boolean isSidebarVisible = true;
    private String initialView;

    public void initialize() {
        try {
            User currentUser = SerializeManager.deserializeUser();
            assert currentUser != null;
            String userType = currentUser.getUserType();

            // Load initial view
            if (userType.equals("admin")) {
                initialView = "/screens/home/HomeAdmin.fxml";
            } else {
                initialView = "/screens/home/HomeUser.fxml";
            }

            setActiveButton(btnHome);
            loadView(initialView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void toggleSidebar() {
        double targetWidth = isSidebarVisible ? 0 : 240;

        // 1. Create the Animation
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.millis(250), // 250ms is usually the "sweet spot"
                        new javafx.animation.KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new javafx.animation.KeyValue(sidebar.minWidthProperty(), targetWidth)
                )
        );

        // 2. The "Modern" Clipping Logic
        // We create a rectangle that stays 240px wide but moves with the sidebar
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.setHeight(1080); // Large enough to cover any screen height
        clip.widthProperty().bind(sidebar.widthProperty()); // Clip width matches sidebar width
        sidebar.setClip(clip);

        timeline.setOnFinished(e -> {
            if (!isSidebarVisible) {
                sidebar.setClip(null); // Clean up clip when fully open
            }
        });

        timeline.play();
        isSidebarVisible = !isSidebarVisible;
    }

    private void setActiveButton(Button clickedButton) {
        btnHome.getStyleClass().remove("menu-button-active");
        btnDashboard.getStyleClass().remove("menu-button-active");
        btnSettings.getStyleClass().remove("menu-button-active");
        btnRecords.getStyleClass().remove("menu-button-active");

        clickedButton.getStyleClass().add("menu-button-active");
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void showHome() {
        loadView("/screens/home/HomeUser.fxml");
        setActiveButton(btnHome);
    }

    @FXML public void showDashboard() {
        loadView("/screens/dashboard/DashboardUser.fxml");
        setActiveButton(btnDashboard);
    }

    @FXML public void showSettings() {
        loadView("/screens/settings/Settings.fxml");
        setActiveButton(btnSettings);
    }

    @FXML public void showRecords() {
        loadView("/screens/history/HistoryRecords.fxml");
        setActiveButton(btnRecords);
    }
}
