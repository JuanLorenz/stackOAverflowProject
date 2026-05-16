package screens.popup;

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
import screens.dashboard.DashboardAdminController;
import utilities.database.EquipmentDAO;
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
    private String imagePath;
    private File selectedImage;
    private DashboardAdminController mainController;

    public void initialize(){
        equipmentName.setText("");
        equipmentCategory.setText("");
        equipmentModelNo.setText("");
        equipmentSerialNo.setText("");
        equipmentCondition.setText("");
        equipmentTotalQty.setText("");
        equipmentImage.setImage(null);
    }

    public void setMaincontroller(DashboardAdminController dac){
        mainController = dac;
    }

    public void onUploadImgClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) uploadImage.getScene().getWindow();
        File tempFile = fileChooser.showOpenDialog(stage);

        if (tempFile != null) {
            selectedImage = tempFile;
            // Show a preview immediately from the local disk
            equipmentImage.setImage(new Image(selectedImage.toURI().toString()));
        }
    }

    public void onAddClicked(ActionEvent actionEvent) {
        // A copy will only be created and saved in the system once the add is clicked
        if(selectedImage != null){
            try{
                File imageDestination = new File("src/main/resources/media/images/equipmentImages/", selectedImage.getName());

                Files.copy(selectedImage.toPath(), imageDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(imageDestination.toURI().toString());
                equipmentImage.setImage(image);

                imagePath = "media/images/equipmentImages/" + selectedImage.getName();
                System.out.println("Successfully copies & added the image in equipmentImage folder");
            }catch(IOException e){
                System.out.println("Failure in saving image");
            }
        }

        if (equipmentService.addNewEquipment(equipmentName.getText(), equipmentCategory.getText(),
                equipmentModelNo.getText(), equipmentSerialNo.getText(), equipmentCondition.getText(),
                Integer.parseInt(equipmentTotalQty.getText()), imagePath)){
            System.out.println("Successfully added a new equipment");
        }
    }

    public void onXClicked(ActionEvent actionEvent) {;
        mainController.popUpScreenExit();
    }

}