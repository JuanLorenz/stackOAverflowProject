package screens.appshell;

import data.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import screens.settings.SettingsController;
import utilities.database.UserDAO;
import utilities.manager.ImageManager;
import utilities.manager.SceneManager;
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

    // Make sure you have fx:id="btnLogout" on your logout button in SceneBuilder!
    @FXML private Button btnLogout;

    private boolean isSidebarVisible = true;
    private String userType;

    public void initialize() {
        try {
            User currentUser = SerializeManager.deserializeUser();
            userType = currentUser != null ? currentUser.getUserType() : "user";

            // Populate the shell UI with the initial user data on load
            updateProfileUI(currentUser);

            // Load initial view
            showHome();

            // --- NEW: Handle the 'X' (Window Close) button ---
            Platform.runLater(() -> {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                if (stage != null) {
                    stage.setOnCloseRequest(event -> {
                        performLogoutLogic();
                        Platform.exit();
                        System.exit(0);
                    });
                }
            });

        } catch (Exception e) {
            System.out.println("Failed to initialize shell.");
            e.printStackTrace();
        }
    }

    // --- NEW: Centralized Logout & Database Sync Logic ---
    private void performLogoutLogic() {
        try {
            User currentUser = SerializeManager.deserializeUser();

            if (currentUser != null) {
                UserDAO userDAO = new UserDAO();

                // 1. Sync the profile picture path to the database
                boolean profileSaved = userDAO.changeAccountProfilePath(
                        currentUser.getId(),
                        currentUser.getProfilePhotoPath()
                );

                // 2. Sync account details to the database
                // IMPORTANT WARNING: Because your DAO hashes the password, make sure
                // currentUser.getPassword() holds the RAW password here, otherwise it will double-hash!
                boolean detailsSaved = userDAO.changeAccountDetails(
                        currentUser.getId(),
                        currentUser.getEmail(),
                        currentUser.getName(),
                        currentUser.getPassword()
                );

                System.out.println("DB Sync on Exit -> Details: " + detailsSaved + " | Profile: " + profileSaved);
            }

            // 3. Clear the local session (change this method name if your SerializeManager uses something else)
            SerializeManager.clearSession();
            System.out.println("Local session cleared.");

        } catch (Exception e) {
            System.out.println("Error during logout cleanup.");
            e.printStackTrace();
        }
    }

    // --- NEW: Action for the Logout Button ---
    @FXML
    public void onClickLogout(ActionEvent event) {
        // 1. Run the database sync and session clear
        performLogoutLogic();

        // 2. Seamlessly swap the root back to the Login screen!
        SceneManager.switchScene(event, "/screens/login/Login.fxml");
    }

    public void updateProfileUI(User user) {
        if (user != null) {
            labelUserName.setText(user.getName());

            String photoPath = user.getProfilePhotoPath();

            if (photoPath != null && !photoPath.isEmpty() && !photoPath.contains("placeholder")) {
                String cleanPath = photoPath.startsWith("/") ? photoPath : "/" + photoPath;

                try {
                    File imageFile = new File("src/main/resources" + cleanPath);
                    if (imageFile.exists()) {
                        ivProfilePic.setImage(new Image(imageFile.toURI().toString()));
                    } else {
                        var resource = getClass().getResource(cleanPath);
                        if (resource != null) {
                            ivProfilePic.setImage(new Image(resource.toExternalForm()));
                        } else {
                            loadPlaceholderImage();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Could not load user profile image.");
                    loadPlaceholderImage();
                }
            } else {
                loadPlaceholderImage();
            }
        }
    }

    private void loadPlaceholderImage() {
        try {
            String placeholderPath = "/" + ImageManager.TYPE_PROFILE + "placeholder-profile.png";
            var res = getClass().getResource(placeholderPath);
            if (res != null) {
                ivProfilePic.setImage(new Image(res.toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Could not find the placeholder image either!");
        }
    }

    @FXML
    public void toggleSidebar() {
        double targetWidth = isSidebarVisible ? 0 : 240;

        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.millis(250),
                        new javafx.animation.KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new javafx.animation.KeyValue(sidebar.minWidthProperty(), targetWidth)
                )
        );

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.setHeight(1080);
        clip.widthProperty().bind(sidebar.widthProperty());
        sidebar.setClip(clip);

        timeline.setOnFinished(e -> {
            if (!isSidebarVisible) {
                sidebar.setClip(null);
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
        if(clickedButton != null) clickedButton.getStyleClass().add("menu-button-active");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Parent view = loader.load();

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