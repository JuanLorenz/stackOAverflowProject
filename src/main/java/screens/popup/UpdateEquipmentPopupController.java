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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    public void display(Equipment equipment){
        imagePath = equipment.getImagePath();
        editedEquipment = equipment;

        labelEquipmentName.setText(equipment.getEquipmentName());
        labelEquipmentModelNo.setText(equipment.getModelNo());
        labelEquipmentSerialNo.setText(equipment.getSerialNo());
        labelEquipmentCondition.setText(equipment.getCondition());
        labelEquipmentAvailable.setText(equipment.getAvailableQty() + "/" + equipment.getTotalQty());
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
        if(selectedImage != null){
            try{
                Files.copy(selectedImage.toPath(), Path.of(imagePath), StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(imagePath);
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
}
