package screens.home;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CategoryCardFactory {

    public static VBox createCard(String title, int borrowedCount, String hexColor, EventHandler<ActionEvent> onAction) {
        VBox card = new VBox();
        card.setMinWidth(200.0);
        card.setPrefHeight(100.0);
        card.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 15; -fx-padding: 15;");

        VBox textContainer = new VBox();
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label lblSubtitle = new Label("Total Borrowed");
        lblSubtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
        textContainer.getChildren().addAll(lblTitle, lblSubtitle);

        Label lblCount = new Label(String.valueOf(borrowedCount));
        lblCount.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topHalf = new HBox(textContainer, spacer, lblCount);
        topHalf.setAlignment(Pos.CENTER_LEFT);

        Button btnView = new Button("View history");
        btnView.setMaxWidth(Double.MAX_VALUE);
        btnView.setStyle("-fx-background-color: #33b5e5; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold; -fx-cursor: hand;");
        VBox.setMargin(btnView, new Insets(10, 0, 0, 0));
        btnView.setOnAction(onAction);

        card.getChildren().addAll(topHalf, btnView);
        return card;
    }
}