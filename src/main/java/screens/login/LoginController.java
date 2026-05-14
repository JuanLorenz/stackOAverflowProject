package screens.login;


import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utilities.manager.SerializeManager;
import utilities.service.AuthService;
import utilities.manager.SceneManager;

public class LoginController {

    // FOR TESTING
    // email : admin@cit.edu
    // pass : 123

    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private Label lStatus;
    @FXML private ImageView rightSideImage;
    @FXML private StackPane imageContainer;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Keeps image fitted to the pane without breaking layout
        rightSideImage.fitWidthProperty().bind(imageContainer.widthProperty());
        rightSideImage.fitHeightProperty().bind(imageContainer.heightProperty());
    }
    // "view" when login is clicked
    public void onLoginClicked(ActionEvent event) {
        String email = tfEmail.getText();
        String password = tfPassword.getText();

        if (email.isBlank() || password.isBlank()) {
            showStatus(Color.RED, "Please enter both email and password.");
            return;
        }

        // 2. Delegate the database check to your AuthService
        User authenticatedUser = authService.login(email, password);

        if (authenticatedUser != null) {
            // 3. Success! Save the session using your SerializeManager
            SerializeManager.serializeUser(authenticatedUser);

            showStatus(Color.GREEN, "Login successful!");

            // 4. Switch to the dashboard
            // Note: Make sure you actually have a dashboard.fxml created in your resources folder!
            SceneManager.switchScene(event, "/screens/home/HomeUser.fxml");

        } else {
            // Fail: Clear password field and show error
            tfPassword.clear();
            showStatus(Color.RED, "Invalid email or password.");
        }
    }

    public void onClickToRegister(ActionEvent event) {
        SceneManager.switchScene(event,"/screens/register/Register.fxml" );
    }

    /**
     * UI helper. <br>
     * Sets status label display depending on the result of action invoked.
     */
    private void showStatus(Color color, String message) {
        lStatus.setTextFill(color);
        lStatus.setText(message);
    }
}