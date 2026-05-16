package screens.dashboard;

import data.equipment.Equipment;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

public class EquipmentCardFactory {

    /**
     * Creates a styled equipment card.
     * @param item The equipment data
     * @param onCardClicked The action to perform when the card is clicked
     */
    public static VBox createCard(Equipment item, Consumer<Equipment> onCardClicked) {
        VBox card = new VBox();
        card.getStyleClass().add("equipment-card");

        // 1. Apply category-specific border color
        String borderClass = "border-" + item.getCategory().toLowerCase().replace(" ", "");
        card.getStyleClass().add(borderClass);

        // 2. Setup Equipment Image
        ImageView iv = new ImageView();
        try {
            // Note: Use the Factory class to get the resource
            iv.setImage(new Image(EquipmentCardFactory.class.getResource(item.getImagePath()).toExternalForm()));
        } catch (Exception e) {
            iv.setImage(new Image(EquipmentCardFactory.class.getResource("/media/images/placeholder.png").toExternalForm()));
        }
        iv.setFitHeight(100);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);

        // 3. Setup Label
        Label nameLabel = new Label(item.getEquipmentName());
        nameLabel.getStyleClass().add("card-label");

        card.getChildren().addAll(iv, nameLabel);

        // 4. Handle Click Event via the Callback
        card.setOnMouseClicked(event -> {
            if (onCardClicked != null) {
                onCardClicked.accept(item);
            }
        });

        return card;
    }
}