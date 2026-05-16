package screens.dashboard;

import data.equipment.Equipment;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utilities.manager.ImageManager;
import java.util.function.Consumer;

public class EquipmentCardFactory {

    public static Button createCard(Equipment item, Consumer<Equipment> onClickAction) {
        int WIDTH = 150;
        int HEIGHT = 150;

        Button btn = new Button();
        btn.getStyleClass().add("equipment-card");
        btn.setPrefSize(180, 220);

        // Image Handling
        Image lowres = ImageManager.getSafeImage(item.getImagePath(), WIDTH, HEIGHT);
        ImageView image = new ImageView(lowres);
        image.setFitWidth(WIDTH);
        image.setFitHeight(HEIGHT);
        image.setPreserveRatio(true);

        // Text Handling
        Label name = new Label(item.getEquipmentName());
        name.setStyle("-fx-font-size: 14px; -fx-font-family: 'Segoe UI Semibold';");
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);

        VBox card = new VBox(15, image, name);
        card.setAlignment(Pos.CENTER);

        btn.setGraphic(card);

        // Pass the equipment object back to the controller's specific logic
        btn.setOnAction(event -> onClickAction.accept(item));

        return btn;
    }
}