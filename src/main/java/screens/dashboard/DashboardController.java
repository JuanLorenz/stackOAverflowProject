package screens.dashboard;

import data.Equipment;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import utilities.sqlRelated.EquipmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardController {

    public Pane paneOverlayShadow;
    public Pane paneMainContainer;
    public Label labelEquipmentDetails;
    public Button buttonExitOverlay;
    public TilePane equipmentContainer;
    private List<Equipment> items = new ArrayList<>();

    public void initialize(){
        //make sure to always refresh by clearing the UI
        equipmentContainer.getChildren().clear();
        items = EquipmentService.getAllEquipment();

        for(Equipment item : items) {
            //create buttons from equipment and add it to the TilePane
            Button btn = new Button();
            btn.setPrefSize(120,150);

            //create the image container (make it low-res first)
            Image lowres = new Image(Objects.requireNonNull(getClass().getResourceAsStream(item.getImagePath())), 100, 100, true, true);
            ImageView image = new ImageView(lowres);
            image.setFitWidth(80);
            image.setPreserveRatio(true);

            //create the label (using a bit of CSS)
            Label name = new Label(item.getEquipmentName());
            name.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

            //arrange image and label in a VBox
            VBox card = new VBox(5);
            card.setAlignment(Pos.CENTER);
            card.getChildren().addAll(image, name);

            btn.setGraphic(card);
            btn.setOnAction(event -> handleButtonClick(item));
            equipmentContainer.getChildren().add(btn);
        }
    }

    private void handleButtonClick(Equipment item) {
        int ID = item.getEquipmentID();
        String name = item.getEquipmentName();
        String model = item.getModelNo();
        String serial = item.getSerialNo();
        String condition = item.getCondition();
        int size = item.getAvailableQty();
        int cap = item.getTotalQty();

        labelEquipmentDetails.setText(
                "Item " + ID + ": " + name + "\n" +
                "Model: " + model + "\n" +
                "Serial: " + serial + "\n" +
                "Quantity: [" + size + "/" + cap + "]\n\n" +
                "Condition: " + condition
        );

        //no logic for showing image yet

        paneOverlayShadow.setVisible(true);
        paneMainContainer.setVisible(true);
    }

    //DON'T DELETE THESE YET
//    public void onActionEquipmentButton(MouseEvent mouseEvent) {
//        Button src = (Button) mouseEvent.getSource();
//        labelEquipmentDetails.setText(Objects.requireNonNull(createDetails(src)).toString());
//        paneOverlayShadow.setVisible(true);
//        paneMainContainer.setVisible(true);
//    }

//    private StringBuilder createDetails(Button src){
//       StringBuilder sb = new StringBuilder();
//
//       sb.append("Equipment Name : ");
//       sb.append(eqpmtButtons.get(eqpmtButtons.indexOf(src)).getText());
//       sb.append("\n\n");
//       sb.append("Availability: [would depend on a static boolean map \n from the view that handles equipment borrowing]\n\n");
//       sb.append("Borrow Fee: [would depend on a static double map \n from the view that handles equipment borrowing]\n\n");
//       sb.append("Further details to be added");
//
//       return sb;
//    }

    public void onMouseClickExitOverlay(MouseEvent mouseEvent) {
        paneOverlayShadow.setVisible(false);
        paneMainContainer.setVisible(false);
    }
}
