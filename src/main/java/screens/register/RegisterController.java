package screens.register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import utilities.sqlRelated.RegisterService;
import utilities.javafxRelated.NavigationUtils;

public class RegisterController {

    @FXML private TextField tfName;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private Label lStatus;
    @FXML private Button buttonRegister;

    private final RegisterService registerService = new RegisterService();

    @FXML
    void onClickRegister(ActionEvent event) {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText();

        // 1. Basic Validation
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            showStatus(Color.RED, "All fields are required.");
            return;
        }

        // 2. Pass data to the Service layer
        int result = registerService.register(name, email, password);
        System.out.println("Test");
        // 3. Handle the result
        if (result == 1) {
            showStatus(Color.GREEN, "Registration successful!");

            // Switch back to the login screen so the user can log in
            NavigationUtils.switchScene(event, "/screens/login/Login.fxml");

        } else if (result == 0) {
            showStatus(Color.RED, "Username is already taken.");
        } else {
            showStatus(Color.RED, "A database error occurred.");
        }
    }

    public void onClickBackToLogin(ActionEvent event) {
        NavigationUtils.switchScene(event,"/screens/login/Login.fxml" );
    }

    /**
     * UI helper.
     * Sets status label display depending on the result of action invoked.
     */
    private void showStatus(Color color, String message) {
        lStatus.setTextFill(color);
        lStatus.setText(message);
    }
}