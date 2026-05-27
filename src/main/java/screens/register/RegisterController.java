package screens.register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utilities.service.RegisterService;
import utilities.manager.SceneManager;

public class RegisterController {

    @FXML private TextField tfName;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private Label lStatus;
    @FXML private ImageView leftSideImage;
    @FXML private StackPane imageContainer;

    private final RegisterService registerService = new RegisterService();

    @FXML
    public void initialize() {
        leftSideImage.fitWidthProperty().bind(imageContainer.widthProperty());
        leftSideImage.fitHeightProperty().bind(imageContainer.heightProperty());
    }

    @FXML
    public void onClickRegister(ActionEvent event) {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText();

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            showStatus(Color.RED, "All fields are required.");
            return;
        }

        int result = registerService.register(name, email, password, "user");

        if (result == 1) {
            showStatus(Color.GREEN, "Registration successful!");
            SceneManager.switchScene(event, "/screens/login/Login.fxml");
        } else if (result == 0) {
            showStatus(Color.RED, "Email is already taken.");
        } else {
            showStatus(Color.RED, "A database error occurred.");
        }
    }

    @FXML
    public void onClickBackToLogin(ActionEvent event) {
        SceneManager.switchScene(event, "/screens/login/Login.fxml");
    }

    private void showStatus(Color color, String message) {
        lStatus.setTextFill(color);
        lStatus.setText(message);
    }
}