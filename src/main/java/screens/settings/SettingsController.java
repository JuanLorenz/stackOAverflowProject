package screens.settings;

import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import utilities.daoRelated.UserDAO;
import utilities.serializationRelated.SerializeManager;
import utilities.sqlRelated.AuthService;

public class SettingsController {

    private Label lblError;
    @FXML
    private TextField txtfldName;

    @FXML
    private TextField txtfldEmail;

    @FXML
    private PasswordField txtfldNewPassword;

    @FXML
    private PasswordField txtfldConfirmPassword;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Button btnSaveChanges;

    @FXML
    private void onClickSaveChanges() {

        User currUser = SerializeManager.deserializeUser();
        AuthService authServ = new AuthService();

        String name = txtfldName.getText();
        String email = txtfldEmail.getText();
        String newPassword = txtfldNewPassword.getText();
        String confirmPassword = txtfldConfirmPassword.getText();

        if (name != null && email != null && newPassword != null && confirmPassword != null){
            if (authServ.login(currUser.getEmail(),currUser.getPassword()) != null) {
                lblError.setText("Passwords don't match");
            }else{
                UserDAO userDAO = new UserDAO();
                userDAO.changeUserDetails(currUser.getEmail(),email,name,confirmPassword);
                lblError.setTextFill(Color.web("#90EE90"));
                lblError.setText("Change successful.");
            }
        }else{
            lblError.setText("Some fields are empty");
        }
        System.out.println("Changes saved.");
    }

    public void onClickUploadNewProfile(ActionEvent actionEvent) {
    }

    public void onClickDeleteProfile(ActionEvent actionEvent) {
    }
}
