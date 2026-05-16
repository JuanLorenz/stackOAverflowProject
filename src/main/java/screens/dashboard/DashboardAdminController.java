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

public class DashboardAdminController {

    @FXML public Button buttonAddEquipment;
    @FXML public TilePane equipmentContainer;
    @FXML public TextField textfieldSearchEquipment;

    private ObservableList<Equipment> items = FXCollections.observableArrayList();
    private FilteredList<Equipment> filteredList;
    private final EquipmentService equipmentService = new EquipmentService();

    public void initialize(){
        equipmentContainer.getChildren().clear();
        Task<List<Equipment>> loadTask = new Task<>() {
            @Override
            protected List<Equipment> call() {
                // This runs on a separate thread (The "Chef" cooking in the back)
                return equipmentService.getAllEquipment();
            }
        };

        loadTask.setOnSucceeded(e -> {
            items.setAll(loadTask.getValue());
            filteredList = new FilteredList<>(items, p -> true);

            //add listener to the search field
            textfieldSearchEquipment.textProperty().addListener((observable, oldValue, newValue) -> {
                //everytime user types a letter, restart the timer
                filteredList.setPredicate(item -> {
                    if (newValue == null || newValue.isBlank()) return true;

                    String lowerCaseFilter = newValue.toLowerCase();
                    return item.getEquipmentName().toLowerCase().contains(lowerCaseFilter) ||
                            item.getModelNo().toLowerCase().contains(lowerCaseFilter);
                });
                renderGrid(items);
            });

            renderGrid(items);
        });

        new Thread(loadTask).start();
    }

    private void renderGrid(List<Equipment> equipments) {
        equipmentContainer.getChildren().clear();
        List<Button> cards = new ArrayList<>();
        for (Equipment item : filteredList) {
            Button card = EquipmentCardFactory.createCard(item, this::handleButtonClick);
            cards.add(card);
        }

        Platform.runLater(() -> equipmentContainer.getChildren().setAll(cards));
    }

    private void handleButtonClick(Equipment item) {
        SceneManager.showOverlay("/screens/popup/UpdateEquipmentPopup.fxml", item);
    }

    public void onAddEquipmentClicked(ActionEvent actionEvent) {
        SceneManager.showOverlay("/screens/popup/AddEquipmentPopup.fxml", null);
    }
}
