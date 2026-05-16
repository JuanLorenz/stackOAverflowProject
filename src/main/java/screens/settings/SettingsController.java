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
import utilities.database.UserDAO;
import utilities.manager.ImageManager;
import utilities.manager.SerializeManager;
import utilities.service.AuthService;

import java.io.File;
import java.util.Objects;

public class SettingsController {


    public Label lblError;

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

    private String profileImagePath;

    @FXML
    private void onClickSaveChanges(ActionEvent actionEvent) {

        User currUser = SerializeManager.deserializeUser();
        AuthService authServ = new AuthService();

        String name = txtfldName.getText();
        String email = txtfldEmail.getText();
        String newPassword = txtfldNewPassword.getText();
        String confirmPassword = txtfldConfirmPassword.getText();

        lblError.setTextFill(Color.web("#ff0000"));

        if (!name.isEmpty() && !email.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()){
            assert currUser != null;
            if (!confirmPassword.equals(newPassword)) {
                lblError.setText("Passwords don't match");
            }else{
                    currUser.setName(name);
                    currUser.setPassword(confirmPassword);
                    currUser.setEmail(email);
                    SerializeManager.serializeUser(currUser);
                    lblError.setTextFill(Color.web("#90EE90"));
                    lblError.setText("Change successful.");
            }
        }else{
            lblError.setText("Some fields are empty");
        }
    }

    public void onClickUploadNewProfile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

        User currUser = SerializeManager.deserializeUser();
        assert currUser != null;
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            profileImagePath = ImageManager.saveImage(selectedFile,"media/profiles/");

            imgProfile.setImage(
                    new Image(selectedFile.toURI().toString())
            );


            currUser.setProfilePhotoPath(profileImagePath);
            SerializeManager.serializeUser(currUser);

            System.out.println("Saved image path: " + profileImagePath);


        }
    }

    public void onClickDeleteProfile(ActionEvent actionEvent) {

        imgProfile.setImage(null);

        User currUser = SerializeManager.deserializeUser();
        assert currUser != null;

        if (profileImagePath != null && !profileImagePath.equals("/images/placeholder.png")) {

            boolean deleted = ImageManager.deleteImage(profileImagePath);

            System.out.println("Image deleted: " + deleted);

            currUser.setProfilePhotoPath(null);
            SerializeManager.serializeUser(currUser);
        }

        imgProfile.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/placeholder-equipment.png")).toExternalForm()));

    }
}
