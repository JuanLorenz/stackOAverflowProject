package screens.popup;

import data.User;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.*;
import screens.home.HomeAdminController;
import utilities.manager.DataReceiver;
import utilities.manager.SceneManager;
import utilities.manager.SerializeManager;
import utilities.service.RegisterService;

import java.io.IOException;

public class AddAdminPopupController implements DataReceiver<Runnable> {


    @FXML private PasswordField txtfldPassword;
    @FXML private TextField txtfldName, txtfldEmail;
    @FXML private Label lblError;

    private Runnable onRefreshCallback;
    private final RegisterService registerService = new RegisterService();

    @Override
    public void setData(Runnable callback) {
        this.onRefreshCallback = callback;
    }

    @FXML
    public void handleAddAdmin(ActionEvent event) {
        User currUser = SerializeManager.deserializeUser();
        if (currUser == null) return;

        lblError.setText("");

        String name = txtfldName.getText();
        String email = txtfldEmail.getText();
        String password = txtfldPassword.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            lblError.setText("Some fields are empty");
            return;
        }

        if (currUser.getEmail().equalsIgnoreCase(email)) {
            lblError.setText("Email is already taken");
            return;
        }

        // Logic to register
        boolean success = registerService.register(name, email, password, "admin");

        if (success) {
            // 1. Tell the home screen to refresh (if callback exists)
            if (onRefreshCallback != null) onRefreshCallback.run();
            // 2. Close the global overlay
            SceneManager.closeOverlay();
        } else {
            lblError.setText("Failed to register admin.");
        }
    }

    public void handleClose() {
        SceneManager.closeOverlay();
    }
}
