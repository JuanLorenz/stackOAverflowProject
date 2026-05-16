package screens.dashboard;

import data.equipment.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import utilities.service.EquipmentService;

import java.util.List;
import java.util.Objects;

public class DashboardAdminController {

    @FXML public Pane paneOverlayShadow;
    @FXML public Pane paneAddEquipmentContainer;
    @FXML public AddEquipmentPopupController paneAddEquipmentContainerController;
    @FXML public Pane paneUpdateEquipmentContainer;
    @FXML public UpdateEquipmentPopupController paneUpdateEquipmentContainerController;

    @FXML public Button buttonAddEquipment;
    @FXML public TilePane equipmentContainer;
    @FXML public TextField textfieldSearchEquipment;

    private String imagePath;
    private ObservableList<Equipment> items = FXCollections.observableArrayList();
    private FilteredList<Equipment> filteredList;
    private final EquipmentService equipmentService = new EquipmentService();

    public void initialize(){
        if(paneAddEquipmentContainerController != null){
            paneAddEquipmentContainerController.setMaincontroller(this);
        }
        if(paneUpdateEquipmentContainerController != null){
            paneUpdateEquipmentContainerController.setMaincontroller(this);
        }

        items.addAll(equipmentService.getAllEquipment());
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
        });
        filteredList.addListener((ListChangeListener<Equipment>) c -> refreshList(filteredList));

        refreshList(items);
    }

    private void refreshList(List<Equipment> equipments) {
        equipmentContainer.getChildren().clear();

        for(Equipment item : equipments) {
            Button btn = new Button();
            btn.getStyleClass().add("equipment-card");
            btn.setPrefSize(180,220);

            Image lowres;
            try {
                String path = item.getImagePath();
                lowres = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm(), 100, 100, true, true, true);
            } catch (Exception e) {
                lowres = new Image(Objects.requireNonNull(getClass().getResource("/images/placeholder-equipment.png")).toExternalForm(), 100, 100, true, true, true);
            }

            ImageView image = new ImageView(lowres);
            image.setFitWidth(100);
            image.setPreserveRatio(true);

            Label name = new Label(item.getEquipmentName());
            name.setStyle("-fx-font-size: 14px; -fx-font-family: 'Segoe UI Semibold';");
            name.setWrapText(true);
            name.setAlignment(Pos.CENTER);

            VBox card = new VBox(15, image, name); //5
            card.setAlignment(Pos.CENTER);

            btn.setGraphic(card);
            btn.setOnAction(event -> handleButtonClick(item));
            equipmentContainer.getChildren().add(btn);
        }
    }

    private void handleButtonClick(Equipment item) {
        paneUpdateEquipmentContainerController.display(item);
        paneOverlayShadow.setVisible(true);
        paneUpdateEquipmentContainer.setVisible(true);
    }

    public void onAddEquipmentClicked(ActionEvent actionEvent) {
        paneAddEquipmentContainerController.initialize();
        paneOverlayShadow.setVisible(true);
        paneAddEquipmentContainer.setVisible(true);
    }

    private void handleSearch() {
        String searched = textfieldSearchEquipment.getText().toLowerCase().trim();
        if(searched.isBlank()) {
            refreshList(items);
            return;
        }

        List<Equipment> results = items.stream()
                .filter(item -> item.getEquipmentName().toLowerCase().contains(searched))
                .toList();

        refreshList(results);
    }


    public void onMouseClickExitOverlay(MouseEvent mouseEvent) {
        paneOverlayShadow.setVisible(false);
        paneAddEquipmentContainer.setVisible(false);
        paneUpdateEquipmentContainer.setVisible(false);
    }

    public void popUpScreenExit(){
        paneOverlayShadow.setVisible(false);
        paneAddEquipmentContainer.setVisible(false);
        paneUpdateEquipmentContainer.setVisible(false);
    }
}
