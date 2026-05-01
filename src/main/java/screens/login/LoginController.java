package screens.login;


import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import utilities.serializationRelated.SerializeManager;
import utilities.sqlRelated.AuthService;
import utilities.javafxRelated.NavigationUtils;

public class LoginController {

    // FOR TESTING
    // email : admin@cit.edu
    // pass : 123

    @FXML
    private TextField tfEmail;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private Label lStatus;

    @FXML
    private Button buttonLogin;

    @FXML
    void login(ActionEvent event) {
        String email = tfEmail.getText();
        String password = tfPassword.getText();

        if (email.isBlank() || password.isBlank()) {
            lStatus.setTextFill(Color.RED);
            lStatus.setText("Please enter both email and password.");
            return;
        }

        // 2. Delegate the database check to your AuthService
        User authenticatedUser = AuthService.authenticate(email, password);

        if (authenticatedUser != null) {
            // 3. Success! Save the session using your SerializeManager
            SerializeManager.SerializeUser(authenticatedUser);

            lStatus.setTextFill(Color.GREEN);
            lStatus.setText("Login successful!");

            // 4. Switch to the dashboard
            // Note: Make sure you actually have a dashboard.fxml created in your resources folder!
            NavigationUtils.switchScene(event, "/screens/dashboard/MainView.fxml");

        } else {
            // Fail: Clear password field and show error
            tfPassword.clear();
            lStatus.setTextFill(Color.RED);
            lStatus.setText("Invalid username or password.");
        }
    }

    public void onClickToRegister(ActionEvent event) {
        NavigationUtils.switchScene(event,"/screens/register/Register.fxml" );
    }
}