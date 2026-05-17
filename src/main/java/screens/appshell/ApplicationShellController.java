package screens.appshell;

import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import screens.settings.SettingsController;
import utilities.database.UserDAO;
import utilities.manager.ImageManager;
import utilities.manager.SceneManager;
import utilities.manager.SerializeManager;

import java.io.IOException;
import java.util.Objects;

public class ApplicationShellController {

    @FXML public ImageView ivProfilePic;
    @FXML public Label labelUserName;
    @FXML public Pane globalShadow;
    @FXML public StackPane globalOverlayHolder;
    @FXML public Label labelShellHeader;

    @FXML private StackPane shellRoot;
    @FXML private VBox sidebar;
    @FXML private VBox sidebarContent;
    @FXML private StackPane contentArea;

    @FXML private Button btnHome;
    @FXML private Button btnDashboard;
    @FXML private Button btnSettings;
    @FXML private Button btnRecords;
    @FXML public Button btnLogout;

    private boolean isSidebarVisible = true;
    private String userType;

    public void initialize() {
        try {
            ivProfilePic.setSmooth(true);
            ivProfilePic.setPreserveRatio(true);

            User currentUser = SerializeManager.deserializeUser();
            userType = currentUser != null ? currentUser.getUserType() : "user";

            updateProfileUI(currentUser);
            showHome();

            SceneManager.setOverlayComponents(globalOverlayHolder, globalShadow);
        } catch (Exception e) {
            System.out.println("Failed to initialize shell.");
            e.printStackTrace();
        }
    }


    public void updateProfileUI(User user) {
        if (user != null) {
            labelUserName.setText(user.getName());

            ivProfilePic.setImage(ImageManager.getSafeImage(
                    user.getProfilePhotoPath(),
                    300, 300
            ));
        }
    }

    private void performLogoutLogic() {
        try {
            User currentUser = SerializeManager.deserializeUser();

            if (currentUser != null) {
                UserDAO userDAO = new UserDAO();

                userDAO.changeAccountProfilePath(
                        currentUser.getId(),
                        currentUser.getProfilePhotoPath()
                );
            }

            SerializeManager.clearSession();
            System.out.println("Local session cleared.");

        } catch (Exception e) {
            System.out.println("Error during logout cleanup.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickLogout(ActionEvent event) {
        performLogoutLogic();
        SceneManager.switchScene(event, "/screens/login/Login.fxml");
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
        if(userType.equals("admin")) {
            loadView("/screens/home/HomeAdmin.fxml");
            labelShellHeader.setText("Welcome, Admin!");
        }
        else {
            loadView("/screens/home/user/HomeUser.fxml");
            labelShellHeader.setText("Home");
        }
        setActiveButton(btnHome);
    }

    @FXML public void showDashboard() {
        if(userType.equals("admin")) {
            loadView("/screens/dashboard/DashboardAdmin.fxml");
            labelShellHeader.setText("Equipments");
        }
        else {
            loadView("/screens/dashboard/DashboardUser.fxml");
            labelShellHeader.setText("Dashboard");
        }
        setActiveButton(btnDashboard);
    }

    @FXML public void showSettings() {
        loadView("/screens/settings/Settings.fxml");
        labelShellHeader.setText("Account Settings");
        setActiveButton(btnSettings);
    }

    @FXML public void showRecords() {
        if(userType.equals("admin")) {
            loadView("/screens/records/BorrowRecords.fxml");
            labelShellHeader.setText("Borrow Records");
        }
        else {
            loadView("/screens/records/HistoryRecords.fxml");
            labelShellHeader.setText("History Records");
        }
        setActiveButton(btnRecords);
    }


    @FXML public void onCloseOverlayRequest() {
        // This allows clicking the dark shadow to close the popup
        SceneManager.closeOverlay();
    }
}