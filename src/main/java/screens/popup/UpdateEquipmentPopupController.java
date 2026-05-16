package screens.popup;

import data.equipment.Equipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import screens.dashboard.DashboardAdminController;
import utilities.manager.SceneManager;
import utilities.service.EquipmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

public class UpdateEquipmentPopupController {

    @FXML public Label labelEquipmentName;
    @FXML public Label labelEquipmentModelNo;
    @FXML public Label labelEquipmentSerialNo;
    @FXML public Label labelEquipmentCondition;
    @FXML public Label labelEquipmentAvailable;
    @FXML public ImageView EquipmentImage;
    @FXML private TextField updateEquipmentCondition;
    @FXML private TextField updateEquipmentTotalQty;
    @FXML private Button buttonUpdateImage;
    @FXML private Button buttonUpdate;
    private String imagePath;
    private File selectedImage;
    private Equipment editedEquipment;
    private final EquipmentService equipmentService = new EquipmentService();
    private DashboardAdminController mainController;

    public void initialize(){
        updateEquipmentCondition.setText("");
        updateEquipmentTotalQty.setText("");
        EquipmentImage.setImage(null);
    }

    public void setMaincontroller(DashboardAdminController dac){
        mainController = dac;
    }

    public void display(Equipment equipment){
        imagePath = equipment.getImagePath();
        editedEquipment = equipment;

        labelEquipmentName.setText(labelEquipmentName.getText() + equipment.getEquipmentName());
        labelEquipmentModelNo.setText(labelEquipmentModelNo.getText() + equipment.getModelNo());
        labelEquipmentSerialNo.setText(labelEquipmentSerialNo.getText() + equipment.getSerialNo());
        labelEquipmentCondition.setText(labelEquipmentCondition.getText() + equipment.getCondition());
        labelEquipmentAvailable.setText(labelEquipmentAvailable.getText() + equipment.getAvailableQty() + "/" + equipment.getTotalQty());
        EquipmentImage.setImage(new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm()));
    }

    public void onUpdateImgClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) buttonUpdateImage.getScene().getWindow();
        File tempFile = fileChooser.showOpenDialog(stage);

        if (tempFile != null) {
            selectedImage = tempFile;
            // Show a preview immediately from the local disk
            EquipmentImage.setImage(new Image(selectedImage.toURI().toString()));
        }
    }

    public void onUpdateClicked(ActionEvent actionEvent) {
        // A copy will only be created and saved in the system once the update is clicked
        if(selectedImage != null){
            try{

                Path toOverride = Path.of(imagePath).toAbsolutePath();
                EquipmentImage.setImage(null);

                Files.copy(selectedImage.toPath(), toOverride, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(toOverride.toUri().toString() + "?" + System.currentTimeMillis());
                EquipmentImage.setImage(image);

                System.out.println("Successfully overrode the past image in equipmentImage folder");
            }catch(IOException e){
                System.out.println("Failure in saving image");
            }
        }
        int originalTotalQty = editedEquipment.getTotalQty();
        int newTotalQty = Integer.parseInt(updateEquipmentTotalQty.getText());
        int difference = newTotalQty - originalTotalQty;
        int originalAvailQty = editedEquipment.getAvailableQty();

        editedEquipment.setCondition((updateEquipmentCondition.getText().isEmpty())? editedEquipment.getCondition() : updateEquipmentCondition.getText());
        editedEquipment.setTotalQty((newTotalQty == 0) ? originalTotalQty : newTotalQty);
        editedEquipment.setAvailableQty(originalAvailQty + difference);

        System.out.println("Successfully updated equipment");
    }

    public void onXClicked(ActionEvent actionEvent) {
        mainController.popUpScreenExit();
    }
}
