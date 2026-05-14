package screens.home;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class HomeAdminController {
    public Button addAdminButton;
    public ListView adminsListView;
    public Label lblEmptyAdminList;
    public TextField txtfldSearchField;
    public ImageView btnMenu;

    public void handleAddAdmin(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/screens/home/AddAdminPopup.fxml"))
            );

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setTitle("Add Admin");

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);

            Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialog.initOwner(owner);

            dialog.showAndWait();

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Failed to load addAdmin.fxml");
            e.printStackTrace();
        }
    }

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleMenu(MouseEvent mouseEvent) {
    }
}
