package screens.reserve;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utilities.manager.SceneManager;

public class ReserveController {
    @FXML private TextField equipmentName;
    @FXML private TextField purpose;
    @FXML private TextField affiliation;
    @FXML private TextField supervisor;
    @FXML private Button back;
    @FXML private Button reserve;


    public void onBackButtonClicked(ActionEvent actionEvent) {
        SceneManager.switchScene(actionEvent, "/screens/dashboard/DashboardUser.fxml");
    }

    public void onReserveButtonClicked(ActionEvent actionEvent) {

    }
}
