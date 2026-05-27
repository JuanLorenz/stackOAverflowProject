package screens.popup;

import data.equipment.Equipment;
import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import utilities.manager.DataReceiver;
import utilities.manager.ImageManager;
import utilities.manager.SceneManager;
import utilities.manager.SerializeManager;
import utilities.service.TransactionService;

public class BorrowEquipmentPopupController implements DataReceiver<Equipment> {

    @FXML private Label lblEquipmentName, lblModelNo, lblSerialNo, lblCategory, lblCondition, lblAvailableQty;
    @FXML private ImageView ivEquipmentImage;
    @FXML private Button btnBorrow, btnBack;

    private Equipment selectedEquipment;
    private final TransactionService transactionService = new TransactionService();
    private final User currentUser = SerializeManager.deserializeUser();

    @Override
    public void setData(Equipment item) {
        this.selectedEquipment = item;

        // Fill Labels
        lblEquipmentName.setText(item.getEquipmentName());
        lblModelNo.setText("Model No: " + item.getModelNo());
        lblSerialNo.setText("Serial No: " + item.getSerialNo());
        lblCategory.setText("Category: " + item.getCategory());
        lblCondition.setText("Condition: " + item.getCondition());
        lblAvailableQty.setText("Available: " + item.getAvailableQty());

        // Set Image
        ivEquipmentImage.setImage(ImageManager.getSafeImage(item.getImagePath(), 250, 250));

        // Fixed color as requested
        btnBorrow.setStyle("-fx-background-color: #FF4946 !important;");
    }

    @FXML
    private void handleBorrow() {
        if (selectedEquipment.getAvailableQty() <= 0) {
            showAlert("Out of Stock", "This item is currently unavailable.");
            return;
        }

        // Call TransactionService to handle DB logic and stock update
        boolean success = transactionService.processBorrow(selectedEquipment, currentUser);

        if (success) {
            showAlert("Success!", "Equipment reserved successfully.");
            SceneManager.closeOverlay();
        } else {
            showAlert("Error", "Could not complete reservation.");
        }
    }

    public void onXClicked(ActionEvent actionEvent) {
        SceneManager.closeOverlay();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}