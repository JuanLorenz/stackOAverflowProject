package screens.dashboard;

import data.equipment.Equipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import data.equipment.EquipmentBuilder;
import utilities.service.EquipmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
    private String imagePath;

    public void onUploadImgClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) uploadImage.getScene().getWindow();
        File selectedImage = fileChooser.showOpenDialog(stage);

        if(selectedImage != null){
            try{
                File imageDestination = new File("src/main/resources/images/equipment/", selectedImage.getName());

                Files.copy(selectedImage.toPath(), imageDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(imageDestination.toURI().toString());
                equipmentImage.setImage(image);

                imagePath = "images/equipment/" + selectedImage.getName();
            }catch(IOException e){
                System.out.println("Failure in saving image");
            }
        }
    }

    public void onAddClicked(ActionEvent actionEvent) {
        Equipment addedEquipment = EquipmentBuilder.start(equipmentCategory.getText())
                .setInfo(equipmentService.getAllEquipment().size(),
                        equipmentName.getText(),
                        equipmentModelNo.getText())
                .setDetails(equipmentSerialNo.getText(),
                        equipmentCondition.getText(),
                        imagePath)
                .setInventory(Integer.parseInt(equipmentTotalQty.getText()),
                        Integer.parseInt(equipmentTotalQty.getText()))
                .build();

        //implement adding this to the list of equipments
    }

    public void onBackClicked(ActionEvent actionEvent) {
        // maybe tangtangon ni???? mayhaps
    }
}