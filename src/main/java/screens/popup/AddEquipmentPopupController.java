package screens.popup;

import data.equipment.Equipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import data.equipment.EquipmentBuilder;
import screens.dashboard.DashboardAdminController;
import screens.dashboard.DashboardAdminViewModel;
import utilities.database.EquipmentDAO;
import utilities.manager.ImageManager;
import utilities.manager.SceneManager;
import utilities.service.EquipmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AddEquipmentPopupController {

    @FXML private TextField equipmentName;
    @FXML private TextField equipmentCategory;
    @FXML private TextField equipmentModelNo;
    @FXML private TextField equipmentSerialNo;
    @FXML private TextField equipmentCondition;
    @FXML private TextField equipmentTotalQty;

    @FXML private ImageView equipmentImage;
    @FXML private Button uploadImage;
    @FXML private Button back;
    @FXML private Button add;


    private final EquipmentService equipmentService = new EquipmentService();
    private File selectedImage;

    public void onUploadImgClicked(ActionEvent actionEvent) {
        selectedImage = ImageManager.chooseImage(uploadImage);
        if (selectedImage != null) {
            equipmentImage.setImage(new Image(selectedImage.toURI().toString()));
        }
    }

    public void onAddClicked(ActionEvent actionEvent) {
        String imagePath = ImageManager.saveImage(selectedImage, ImageManager.TYPE_EQUIPMENT);

        Equipment newEquipment = EquipmentBuilder.start(equipmentCategory.getText())
                .setInfo(0, equipmentName.getText(), equipmentModelNo.getText())
                .setDetails(equipmentSerialNo.getText(), equipmentCondition.getText(), imagePath)
                .setInventory(Integer.parseInt(equipmentTotalQty.getText()), Integer.parseInt(equipmentTotalQty.getText()))
                .build();

        if (equipmentService.addNewEquipment(newEquipment)) {
            showAlert("Success!", "Equipment added successfully!");
            DashboardAdminViewModel.addEquipmentToCache(newEquipment);
            SceneManager.closeOverlay();
        } else {
            showAlert("Error", "Equipment not added successfully.");
        }
    }

    public void onXClicked(ActionEvent actionEvent) {;
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