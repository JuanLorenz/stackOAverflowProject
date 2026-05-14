package screens.home;

import data.User;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.*;
import utilities.manager.SerializeManager;
import utilities.service.RegisterService;

import java.io.IOException;

public class AddAdminPopupController {


    public PasswordField txtfldPassword;
    public TextField txtfldName;
    public TextField txtfldEmail;
    public Button btnAdd;
    public Label lblError;

    public void handleAddAdmin(ActionEvent event) throws IOException {
        User currUser = SerializeManager.deserializeUser();
        assert currUser != null;

        lblError.setText("");

        if (!txtfldEmail.getText().isEmpty() && !txtfldName.getText().isEmpty() && !txtfldPassword.getText().isEmpty()){
            if (!currUser.getEmail().equals(txtfldEmail.getText())){

                RegisterService registerService = new RegisterService();

                registerService.register(txtfldName.getText(),txtfldEmail.getText(),txtfldPassword.getText(),"admin");

                //handle listview changes here

                handleClose(event);
            }else{
                lblError.setText("Email is already taken");
            }
        }else{
            lblError.setText("Some fields are empty");
        }


    }

    public void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
