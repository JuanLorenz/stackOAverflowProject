package screens.popup;

import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utilities.manager.DataReceiver;
import utilities.manager.SceneManager;
import utilities.manager.SerializeManager;
import utilities.service.RegisterService;

public class AddAdminPopupController implements DataReceiver<Runnable> {

    @FXML private PasswordField txtfldPassword;
    @FXML private TextField txtfldName, txtfldEmail;
    @FXML private Label lblError;

    private Runnable refreshCallback; // The "Job" to do after adding
    private final RegisterService registerService = new RegisterService();

    @Override
    public void setData(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    public void handleAddAdmin(ActionEvent event) {
        User currUser = SerializeManager.deserializeUser();
        if (currUser == null) return;

        lblError.setText("");

        if (txtfldEmail.getText().isEmpty() || txtfldName.getText().isEmpty() || txtfldPassword.getText().isEmpty()) {
            lblError.setText("Some fields are empty");
            return;
        }

        if (currUser.getEmail().equals(txtfldEmail.getText())) {
            lblError.setText("Email is already taken");
            return;
        }

        // Register the user
        int success = registerService.register(txtfldName.getText(), txtfldEmail.getText(), txtfldPassword.getText(), "admin");

        if (success == 1) {
            // Run the refresh logic in the Home screen
            if (refreshCallback != null) refreshCallback.run();
            // Close the global overlay
            SceneManager.closeOverlay();
        } else {
            lblError.setText("Registration failed.");
        }
    }

    @FXML
    public void handleClose() {
        SceneManager.closeOverlay();
    }
}