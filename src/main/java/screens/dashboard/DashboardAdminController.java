package screens.dashboard;

import data.equipment.Equipment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import screens.popup.AddEquipmentPopupController;
import screens.popup.UpdateEquipmentPopupController;
import utilities.manager.SceneManager;
import utilities.service.EquipmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DashboardAdminController {

    @FXML public Button buttonAddEquipment;
    @FXML public TilePane equipmentContainer;
    @FXML public TextField textfieldSearchEquipment;

    private final DashboardAdminViewModel viewModel = new DashboardAdminViewModel();

    public void initialize(){
        textfieldSearchEquipment.textProperty().bindBidirectional(viewModel.searchQueryProperty());

        // We show a blank screen/loading state until ensureDataLoaded finishes
        viewModel.ensureDataLoaded(() -> {
            // This runs on the UI Thread once the background Task is done
            renderGrid();
            viewModel.getFilteredData().addListener((ListChangeListener<Equipment>) c -> renderGrid());
        });

        //update equipment quantity if user has returned the equipment
        viewModel.refreshDataQuietly();
    }

    private void renderGrid() {
        // We use viewModel.getFilteredData() here
        List<Button> cards = viewModel.getFilteredData().stream()
                .map(item -> EquipmentCardFactory.createCard(item, this::handleButtonClick))
                .collect(Collectors.toList());

        Platform.runLater(() -> equipmentContainer.getChildren().setAll(cards));
    }

    private void handleButtonClick(Equipment item) {
        SceneManager.showOverlay("/screens/popup/UpdateEquipmentPopup.fxml", item);
    }

    public void onAddEquipmentClicked(ActionEvent actionEvent) {
        SceneManager.showOverlay("/screens/popup/AddEquipmentPopup.fxml", null);
    }

}
