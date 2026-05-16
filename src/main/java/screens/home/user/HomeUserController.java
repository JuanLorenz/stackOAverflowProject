package screens.home.user;

import data.Transaction;
import data.User;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import screens.home.CategoryCardFactory;
import screens.home.user.HomeUserViewModel;
import utilities.manager.SerializeManager;

import java.util.Map;

public class HomeUserController {

    private HomeUserViewModel viewModel;
    private FilteredList<Transaction> filteredData;

    @FXML private ScrollPane categoryScrollPane;
    @FXML private HBox cardsContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryComboBox;

    @FXML private TableView<Transaction> borrowedTable;
    @FXML private TableColumn<Transaction, String> itemColumn;
    @FXML private TableColumn<Transaction, Void> actionColumn;

    @FXML private Button leftArrowBtn;
    @FXML private Button rightArrowBtn;

    @FXML
    public void initialize() {
        viewModel = new HomeUserViewModel();
        viewModel.loadData(); // Fetches counts, active items, and checks overdue logic

        populateCategoryCards();
        setupTableColumns();

        // Setup Search & Filter
        filteredData = new FilteredList<>(viewModel.getBorrowedItems(), b -> true);
        SortedList<Transaction> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(borrowedTable.comparatorProperty());
        borrowedTable.setItems(sortedData);
        setupSearchAndFilter();

        // Setup Resizing & Scrolling
        setupDynamicCardResizing();
        setupArrowVisibilityLogic();

        // Check if user got blocked upon loading
        User currentUser = SerializeManager.deserializeUser();
        if (currentUser != null && currentUser.isBlocked()) {
            showBlockedWarning();
        }
    }

    private void populateCategoryCards() {
        Map<String, Integer> counts = viewModel.getHistoryCounts();

        cardsContainer.getChildren().addAll(
                CategoryCardFactory.createCard("Engineering", counts.getOrDefault("Engineering", 0), "#c074cc", this::handleViewHistory),
                CategoryCardFactory.createCard("Chemistry", counts.getOrDefault("Chemistry", 0), "#ffb74d", this::handleViewHistory),
                CategoryCardFactory.createCard("Physical Education", counts.getOrDefault("Physical Education", 0), "#ff6b6b", this::handleViewHistory),
                CategoryCardFactory.createCard("Multi Media", counts.getOrDefault("Multi Media", 0), "#9575cd", this::handleViewHistory),
                CategoryCardFactory.createCard("Medical Sciences", counts.getOrDefault("Medical Sciences", 0), "#4dd0e1", this::handleViewHistory),
                CategoryCardFactory.createCard("Agriculture", counts.getOrDefault("Agriculture", 0), "#81c784", this::handleViewHistory),
                CategoryCardFactory.createCard("Architecture", counts.getOrDefault("Architecture", 0), "#f06292", this::handleViewHistory),
                CategoryCardFactory.createCard("IT", counts.getOrDefault("IT", 0), "#3f51b5", this::handleViewHistory)
        );
    }

    private void setupTableColumns() {
        itemColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEquipment().getEquipmentName())
        );

        Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> cellFactory = param -> new TableCell<>() {
            private final Button returnBtn = new Button("Return");
            {
                returnBtn.getStyleClass().add("btn-return");
                returnBtn.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    viewModel.returnEquipment(transaction);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox centeredPane = new HBox(returnBtn);
                    centeredPane.setStyle("-fx-alignment: center;");
                    setGraphic(centeredPane);
                }
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void setupSearchAndFilter() {
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "All", "Engineering", "Chemistry", "Physical Education", "Multi Media", "Medical Sciences", "Agriculture", "Architecture", "IT"
        ));
        categoryComboBox.getSelectionModel().selectFirst();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        String selectedCategory = categoryComboBox.getValue();

        filteredData.setPredicate(transaction -> {
            boolean matchesCategory = selectedCategory.equals("All") ||
                    transaction.getEquipment().getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesSearch = transaction.getEquipment().getEquipmentName().toLowerCase().contains(searchText);
            return matchesCategory && matchesSearch;
        });
    }

    private void showBlockedWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Account Blocked");
        alert.setHeaderText("You have overdue equipment!");
        alert.setContentText("Your account has been temporarily blocked. Please return your overdue equipment to restore access.");
        alert.showAndWait();
    }

    private void handleViewHistory(ActionEvent event) {
        System.out.println("Redirecting to History Screen...");
    }

    // --- Dynamic Resizing & Scrolling ---
    private void setupDynamicCardResizing() {
        categoryScrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double targetWidth = (newVal.getWidth() - 55.0) / 4.0;
            double finalWidth = Math.max(targetWidth, 200.0);
            if (cardsContainer != null) {
                for (Node node : cardsContainer.getChildren()) {
                    if (node instanceof VBox) ((VBox) node).setPrefWidth(finalWidth);
                }
            }
            Platform.runLater(this::updateArrowVisibility);
        });
    }

    private void setupArrowVisibilityLogic() {
        leftArrowBtn.setVisible(false);
        categoryScrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> updateArrowVisibility());
        categoryScrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> updateArrowVisibility());
        Platform.runLater(this::updateArrowVisibility);
    }

    private void updateArrowVisibility() {
        double current = categoryScrollPane.getHvalue();
        double max = categoryScrollPane.getHmax();
        if (max == 0) {
            leftArrowBtn.setVisible(false);
            rightArrowBtn.setVisible(false);
        } else {
            leftArrowBtn.setVisible(current > 0.01);
            rightArrowBtn.setVisible(current < 0.99);
        }
    }

    @FXML public void handleScrollLeft() { scrollByPage(-1); }
    @FXML public void handleScrollRight() { scrollByPage(1); }

    private void scrollByPage(int direction) {
        double viewW = categoryScrollPane.getViewportBounds().getWidth();
        double contentW = cardsContainer.getWidth();
        if (contentW <= viewW) return;

        double step = viewW / (contentW - viewW);
        double target = categoryScrollPane.getHvalue() + (step * direction);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(350),
                new KeyValue(categoryScrollPane.hvalueProperty(), Math.max(0.0, Math.min(1.0, target)), Interpolator.EASE_BOTH)));
        timeline.play();
    }
}