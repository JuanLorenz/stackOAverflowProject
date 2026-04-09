package dashboardAndComponents;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardController {

    // All the elements from the MainView.fxml
    public Button buttonEquipment1;
    public Button buttonEquipment2;
    public Button buttonEquipment3;
    public Button buttonEquipment4;
    public Button buttonEquipment5;
    public Button buttonEquipment6;
    public Button buttonEquipment7;
    public Button buttonEquipment8;
    public Button buttonEquipment9;
    public Button buttonEquipment10;
    public Button buttonEquipment11;
    public Button buttonEquipment12;
    public Button buttonEquipment13;
    public Button buttonEquipment14;
    public Button buttonEquipment15;
    public Pane paneOverlayShadow;
    public Pane paneMainContainer;
    public Label labelEquipmentDetails;
    public Button buttonEquipment16;
    public Button buttonEquipment17;
    public Button buttonEquipment18;
    public Button buttonEquipment19;
    public Button buttonEquipment20;
    public Button buttonExitOverlay;

    // private fields for efficiency
    private final List<Button> eqpmtButtons = new ArrayList<>();

    public void initialize(){
        // populate the list;
        eqpmtButtons.add(buttonEquipment1);
        eqpmtButtons.add(buttonEquipment2);
        eqpmtButtons.add(buttonEquipment3);
        eqpmtButtons.add(buttonEquipment4);
        eqpmtButtons.add(buttonEquipment5);
        eqpmtButtons.add(buttonEquipment6);
        eqpmtButtons.add(buttonEquipment7);
        eqpmtButtons.add(buttonEquipment8);
        eqpmtButtons.add(buttonEquipment9);
        eqpmtButtons.add(buttonEquipment10);
        eqpmtButtons.add(buttonEquipment11);
        eqpmtButtons.add(buttonEquipment12);
        eqpmtButtons.add(buttonEquipment13);
        eqpmtButtons.add(buttonEquipment14);
        eqpmtButtons.add(buttonEquipment15);
        eqpmtButtons.add(buttonEquipment16);
        eqpmtButtons.add(buttonEquipment17);
        eqpmtButtons.add(buttonEquipment18);
        eqpmtButtons.add(buttonEquipment19);
        eqpmtButtons.add(buttonEquipment20);
    }

    public void onActionEquipmentButton(MouseEvent mouseEvent) {
        Button src = (Button) mouseEvent.getSource();
        labelEquipmentDetails.setText(Objects.requireNonNull(createDetails(src)).toString());
        paneOverlayShadow.setVisible(true);
        paneMainContainer.setVisible(true);
    }

    private StringBuilder createDetails(Button src){
       StringBuilder sb = new StringBuilder();

       sb.append("Equipment Name : ");
       sb.append(eqpmtButtons.get(eqpmtButtons.indexOf(src)).getText());
       sb.append("\n\n");
       sb.append("Availability: [would depend on a static boolean map \n from the view that handles equipment borrowing]\n\n");
       sb.append("Borrow Fee: [would depend on a static double map \n from the view that handles equipment borrowing]\n\n");
       sb.append("Further details to be added");

       return sb;
    }

    public void onMouseClickExitOverlay(MouseEvent mouseEvent) {
        paneOverlayShadow.setVisible(false);
        paneMainContainer.setVisible(false);
    }
}
