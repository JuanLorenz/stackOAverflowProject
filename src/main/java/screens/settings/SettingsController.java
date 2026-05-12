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
import utilities.daoRelated.UserDAO;
import utilities.javafxRelated.ImageService;
import utilities.serializationRelated.SerializeManager;
import utilities.sqlRelated.AuthService;

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
                UserDAO userDAO = new UserDAO();
                if (userDAO.changeUserDetails(currUser.getEmail(),email,name,confirmPassword)){
                    lblError.setTextFill(Color.web("#90EE90"));
                    lblError.setText("Change successful.");
                }else{
                    lblError.setText("Error: Change unsuccessful, contact admin");
                }
            }
        }else{
            lblError.setText("Some fields are empty");
        }
    }

    public void onClickUploadNewProfile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

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

            profileImagePath = ImageService.saveImage(selectedFile);

            imgProfile.setImage(
                    new Image(selectedFile.toURI().toString())
            );

            System.out.println("Saved image path: " + profileImagePath);

            //TODO: upload to database ang file path
        }
    }

    public void onClickDeleteProfile(ActionEvent actionEvent) {

        imgProfile.setImage(null);

        if (profileImagePath != null && !profileImagePath.equals("/images/placeholder.png")) {

            boolean deleted = ImageService.deleteImage(profileImagePath);

            System.out.println("Image deleted: " + deleted);
        }

        profileImagePath = null;

        imgProfile.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/placeholder-img.png")).toExternalForm()));

        // TODO: Update db probably if ma delete ang profile img
    }
}
