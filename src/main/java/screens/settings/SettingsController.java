package screens.settings;

import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.mindrot.jbcrypt.BCrypt;
import screens.appshell.ApplicationShellController;
import utilities.database.UserDAO;
import utilities.manager.ImageManager;
import utilities.manager.SerializeManager;
import utilities.service.AuthService;

import java.io.File;

public class SettingsController {

    public Label lblError;

    @FXML private TextField txtfldName;
    @FXML private TextField txtfldEmail;
    @FXML private PasswordField txtfldNewPassword;
    @FXML private PasswordField txtfldConfirmPassword;
    @FXML private ImageView imgProfile;
    @FXML private Button btnSaveChanges;

    private String profileImagePath;

    private ApplicationShellController appShellController;

    public void setAppShellController(ApplicationShellController appShellController) {
        this.appShellController = appShellController;
    }

    // --- RECORRECTED: Initialize method to load current user data ---
    @FXML
    public void initialize() {
        User currUser = SerializeManager.deserializeUser();

        if (currUser != null) {
            txtfldName.setText(currUser.getName());
            txtfldEmail.setText(currUser.getEmail());

            profileImagePath = currUser.getProfilePhotoPath();

            if (profileImagePath != null && !profileImagePath.isEmpty() && !profileImagePath.contains("placeholder")) {
                String cleanPath = profileImagePath.startsWith("/") ? profileImagePath : "/" + profileImagePath;
                try {
                    File imageFile = new File("src/main/resources" + cleanPath);
                    if (imageFile.exists()) {
                        imgProfile.setImage(new Image(imageFile.toURI().toString()));
                    } else {
                        var resource = getClass().getResource(cleanPath);
                        if (resource != null) imgProfile.setImage(new Image(resource.toExternalForm()));
                        else loadPlaceholderImage();
                    }
                } catch (Exception e) {
                    loadPlaceholderImage();
                }
            } else {
                loadPlaceholderImage();
            }
        }
    }

    private void loadPlaceholderImage() {
        try {
            String placeholderPath = "/" + ImageManager.TYPE_EQUIPMENT + "placeholder-equipment.png";
            var res = getClass().getResource(placeholderPath);
            if (res != null) imgProfile.setImage(new Image(res.toExternalForm()));
        } catch (Exception e) {
            System.out.println("Could not find placeholder in Settings.");
        }
    }

    @FXML
    private void onClickSaveChanges(ActionEvent actionEvent) {
        User currUser = SerializeManager.deserializeUser();
        UserDAO userDAO = new UserDAO();

        assert currUser != null;

        String name = txtfldName.getText();
        String email = txtfldEmail.getText();
        String newPassword = txtfldNewPassword.getText();
        String confirmPassword = txtfldConfirmPassword.getText();

        lblError.setTextFill(Color.web("#ff0000"));

        if (!name.isEmpty() && !email.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()){
            if (!confirmPassword.equals(newPassword)) {
                lblError.setText("Passwords don't match");
            } else {
                if (userDAO.passwordVerify(currUser.getId(),confirmPassword)){
                    lblError.setText("Password did not change");
                }else {
                    currUser.setName(name);
                    currUser.setPassword(confirmPassword);
                    currUser.setEmail(email);
                    SerializeManager.serializeUser(currUser);

                    userDAO.changeAccountDetails(currUser.getId(), email, name, confirmPassword);

                    lblError.setTextFill(Color.web("#90EE90"));
                    lblError.setText("Change successful.");

                    if (appShellController != null) {
                        appShellController.updateProfileUI(currUser);
                    }
                }
            }
        } else {
            lblError.setText("Some fields are empty");
        }
    }

    public void onClickUploadNewProfile(ActionEvent actionEvent) {
        // USING YOUR NEW CENTRALIZED FILE CHOOSER!
        File selectedFile = ImageManager.chooseImage(btnSaveChanges);

        if (selectedFile != null) {
            User currUser = SerializeManager.deserializeUser();
            assert currUser != null;

            // USING YOUR CENTRALIZED UPDATE LOGIC!
            profileImagePath = ImageManager.updateImage(currUser.getProfilePhotoPath(), selectedFile, ImageManager.TYPE_PROFILE);
            imgProfile.setImage(new Image(selectedFile.toURI().toString()));

            currUser.setProfilePhotoPath(profileImagePath);
            SerializeManager.serializeUser(currUser);

            if (appShellController != null && currUser != null) {
                appShellController.updateProfileUI(currUser);
            }

            System.out.println("Saved image path: " + profileImagePath);
        }
    }

    public void onClickDeleteProfile(ActionEvent actionEvent) {
        User currUser = SerializeManager.deserializeUser();
        assert currUser != null;

        if (profileImagePath != null && !profileImagePath.contains("placeholder")) {
            boolean deleted = ImageManager.deleteImage(profileImagePath);
            System.out.println("Image deleted: " + deleted);

            currUser.setProfilePhotoPath(null);
            SerializeManager.serializeUser(currUser);
        }

        loadPlaceholderImage();

        if (appShellController != null) {
            appShellController.updateProfileUI(currUser);
        }
    }
}