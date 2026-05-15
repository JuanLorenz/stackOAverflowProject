package screens.dashboard;

import data.equipment.Equipment;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import utilities.manager.ImageManager;

public class DashboardUserController {

    @FXML public TilePane equipmentContainer;
    @FXML public TextField tfSearchField;

    // Logic for your existing overlays (to be moved to popups later)
    @FXML public Pane paneOverlayShadow;
    @FXML public VBox paneMainContainer;
    @FXML public Label labelEquipmentName;
    @FXML public ImageView equipmentImage;
    @FXML private Label labelEquipmentDetails;

    private DashboardViewModel viewModel;

    public void initialize() {
        viewModel = new DashboardViewModel();

        // 1. Listen for Search changes
        tfSearchField.textProperty().addListener((obs, old, newVal) -> {
            viewModel.filter(newVal);
            renderGrid();
        });

        // 2. Initial Render
        renderGrid();
    }

    private void renderGrid() {
        equipmentContainer.getChildren().clear();

        for (Equipment item : viewModel.getFilteredData()) {
            // Use the Factory to build the UI
            // Pass 'this::handleButtonClick' as the action
            Button card = EquipmentCardFactory.createCard(item, this::handleButtonClick);
            equipmentContainer.getChildren().add(card);
        }
    }

    private void handleButtonClick(Equipment item) {
        // User-specific logic (e.g., show details/reservation)
        equipmentImage.setImage(ImageManager.getSafeImage(item.getImagePath(), 200, 200));
        labelEquipmentName.setText(item.getEquipmentName());
        labelEquipmentDetails.setText(
                "ID: " + item.getEquipmentID() + "\n" +
                "Model: " + item.getModelNo() + "\n" +
                "Serial: " + item.getSerialNo() + "\n" +
                "Quantity: [" + item.getAvailableQty() + "/" + item.getTotalQty() + "]" + "\n" +
                "Condition: " + item.getCondition()
        );

        paneOverlayShadow.setVisible(true);
        paneMainContainer.setVisible(true);
    }

    public void onMouseClickExitOverlay() {
        paneOverlayShadow.setVisible(false);
        paneMainContainer.setVisible(false);
    }
}