package screens.popup;

import data.equipment.Equipment;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import screens.dashboard.DashboardAdminController;
import screens.dashboard.DashboardAdminViewModel;
import utilities.manager.DataReceiver;
import utilities.manager.ImageManager;
import utilities.manager.SceneManager;
import utilities.service.EquipmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

public class UpdateEquipmentPopupController implements DataReceiver<Equipment> {

    @FXML public Label labelEquipmentName;
    @FXML public Label labelEquipmentModelNo;
    @FXML public Label labelEquipmentSerialNo;
    @FXML public Label labelEquipmentCategory;
    @FXML public Label labelEquipmentCondition;
    @FXML public Label labelEquipmentAvailable;


    @FXML public ImageView EquipmentImage;
    @FXML private TextField updateEquipmentCondition;
    @FXML private TextField updateEquipmentTotalQty;

    @FXML private Button buttonUpdateImage;
    @FXML private Button buttonUpdate;

    private String imagePath;
    private File newImageFile;
    private Equipment equipment;
    private final EquipmentService equipmentService = new EquipmentService();

    @Override
    public void setData(Equipment data) {
        this.equipment = data;

        labelEquipmentName.setText(data.getEquipmentName());
        labelEquipmentModelNo.setText("Model No: " + data.getModelNo());
        labelEquipmentSerialNo.setText("Serial No: " + data.getSerialNo());
        labelEquipmentCategory.setText("Category: " + data.getCategory());
        labelEquipmentCondition.setText("Condition: " + data.getCondition());
        labelEquipmentAvailable.setText("Available: " + data.getAvailableQty() + "/" + data.getTotalQty());

        EquipmentImage.setImage(ImageManager.getSafeImage(data.getImagePath(), 200, 200));
    }

    public void onUpdateImgClicked(ActionEvent event) {
        newImageFile = ImageManager.chooseImage((Button)event.getSource());
        if (newImageFile != null) {
            EquipmentImage.setImage(new Image(newImageFile.toURI().toString()));
        }
    }

    public void onUpdateClicked(ActionEvent actionEvent) {
        if (newImageFile != null) {
            equipment.setImagePath(ImageManager.updateImage(equipment.getImagePath(), newImageFile, ImageManager.TYPE_EQUIPMENT));
        }
        String newCondition = updateEquipmentCondition.getText();
        String newTotal = updateEquipmentTotalQty.getText();

        if (!newCondition.isEmpty()) {
            equipment.setCondition(newCondition);
        }//18/20
        if (!newTotal.isEmpty()) {
            int val = Integer.parseInt(newTotal); //25
            int add = val - equipment.getTotalQty(); //25-20 = 5
            equipment.setAvailableQty(equipment.getAvailableQty() + add); //18 + 5 = 23
            equipment.setTotalQty(val); //25
        }

        boolean result = equipmentService.updateEquipment(equipment);

        if (result) {
            showAlert("Success","Successfully updated equipment.");
            DashboardAdminController dac = DashboardAdminController.getInstance();
            if(dac != null){
                dac.renderSpecificCard(equipment);
            }
            SceneManager.closeOverlay();
        } else {
            showAlert("Error", "Could not update equipment.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void onXClicked(ActionEvent actionEvent) {
        SceneManager.closeOverlay();
    }

}
