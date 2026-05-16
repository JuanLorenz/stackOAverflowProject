package screens.appshell;

import data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import screens.settings.SettingsController; // Imported SettingsController
import utilities.manager.SerializeManager;

import java.io.File;
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
    private String userType;

    public void initialize() {
        try {
            User currentUser = SerializeManager.deserializeUser();
            //assert currentUser != null;
            userType = currentUser.getUserType();

            // Populate the shell UI with the initial user data on load
            updateProfileUI(currentUser);

            // Load initial view
            showHome();
        } catch (Exception e) {
            System.out.println("Failed to initialize shell.");
            e.printStackTrace();
        }
    }

    // --- NEW: Dedicated method to refresh the UI when data changes ---
    public void updateProfileUI(User user) {
        if (user != null) {
            labelUserName.setText(user.getName());

            String photoPath = user.getProfilePhotoPath();
            if (photoPath != null && !photoPath.equals("/images/placeholder.png") && !photoPath.isEmpty()) {
                try {
                    ivProfilePic.setImage(new Image(new File(photoPath).toURI().toString()));
                } catch (Exception e) {
                    System.out.println("Could not load user profile image.");
                }
            } else {
                ivProfilePic.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/placeholder-equipment.png")).toExternalForm()));
            }
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

    // --- MODIFIED: Used FXMLLoader instance to pass the shell reference ---
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent view = loader.load();

            // Check if the loaded view is Settings, and if so, pass 'this'
            Object controller = loader.getController();
            if (controller instanceof SettingsController) {
                ((SettingsController) controller).setAppShellController(this);
            }

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            System.out.println("Failed to load " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML public void showHome() {
        if(userType.equals("admin")) loadView("/screens/home/HomeAdmin.fxml");
        else loadView("/screens/home/user/HomeUser.fxml");
        setActiveButton(btnHome);
    }

    @FXML public void showDashboard() {
        if(userType.equals("admin")) loadView("/screens/dashboard/DashboardAdmin.fxml");
        else loadView("/screens/dashboard/DashboardUser.fxml");
        setActiveButton(btnDashboard);
    }

    @FXML public void showSettings() {
        loadView("/screens/settings/Settings.fxml");
        setActiveButton(btnSettings);
    }

    @FXML public void showRecords() {
        if(userType.equals("admin")) loadView("/screens/history/BorrowRecords.fxml");
        else loadView("/screens/history/HistoryRecords.fxml");
        setActiveButton(btnRecords);
    }
}