package screens.popup;

import data.equipment.Equipment;
import data.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import utilities.manager.SerializeManager;
import utilities.service.TransactionService;

public class BorrowEquipmentPopupController {

    @FXML private Label lblEquipmentName, lblModelNo, lblSerialNo, lblCategory, lblCondition, lblAvailableQty;
    @FXML private ImageView ivEquipmentImage;
    @FXML private Button btnReserve, btnBack;
    private StackPane overlayPane;

    private Equipment selectedEquipment;
    private final TransactionService transactionService = new TransactionService();
    private final User currentUser = SerializeManager.deserializeUser();

    public void setOverlayPane(StackPane pane) {
        this.overlayPane = pane;
    }

    public void setEquipment(Equipment item) {
        this.selectedEquipment = item;

        // Fill Labels
        lblEquipmentName.setText(item.getEquipmentName());
        lblModelNo.setText("Model No: " + item.getModelNo());
        lblSerialNo.setText("Serial No: " + item.getSerialNo());
        lblCategory.setText("Category: " + item.getCategory());
        lblCondition.setText("Condition: " + item.getCondition());
        lblAvailableQty.setText("Available: " + item.getAvailableQty());

        // Set Image
        try {
            ivEquipmentImage.setImage(new Image(getClass().getResource(item.getImagePath()).toExternalForm()));
        } catch (Exception e) {
            ivEquipmentImage.setImage(new Image(getClass().getResource("/media/images/placeholder.png").toExternalForm()));
        }

        // Fixed color as requested
        btnReserve.setStyle("-fx-background-color: #FF4946 !important;");
    }

    @FXML
    private void handleBack() {
        closePopup();
    }

    @FXML
    private void handleReserve() {
        if (selectedEquipment.getAvailableQty() <= 0) {
            showAlert("Out of Stock", "This item is currently unavailable.");
            return;
        }

        // Call TransactionService to handle DB logic and stock update
        boolean success = transactionService.processBorrow(selectedEquipment, currentUser);

        if (success) {
            showAlert("Success!", "Equipment reserved successfully.");
            closePopup();
        } else {
            showAlert("Error", "Could not complete reservation.");
        }
    }

    private void closePopup() {
        if (overlayPane != null) {
            overlayPane.setVisible(false);
            overlayPane.getChildren().clear();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}