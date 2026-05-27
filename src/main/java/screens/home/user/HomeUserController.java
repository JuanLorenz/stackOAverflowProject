package screens.home.user;

import data.Transaction;
import data.User;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import screens.home.user.CategoryCardFactory;
import utilities.manager.SerializeManager;

import java.time.LocalDate;
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
    @FXML private TableColumn<Transaction, LocalDate> dueDateColumn;
    @FXML private TableColumn<Transaction, Void> actionColumn;

    @FXML private Button leftArrowBtn;
    @FXML private Button rightArrowBtn;

    private User currentUser;

    @FXML
    public void initialize() {
        viewModel = new HomeUserViewModel();
        viewModel.loadData();

        // Force initial card draw onto the JavaFX UI Thread
        Platform.runLater(this::populateCategoryCards);

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
        currentUser = SerializeManager.deserializeUser();
        if (currentUser != null && currentUser.isBlocked()) {
            // 2. Queue the alert to show AFTER the screen is visible
            Platform.runLater(() -> {
                showBlockedWarning();
            });
        }
    }

    private void populateCategoryCards() {
        Map<String, Integer> counts = viewModel.getHistoryCounts();

        cardsContainer.getChildren().addAll(
                CategoryCardFactory.createCard("Engineering", counts.getOrDefault("Engineering", 0), "#c074cc", event -> handleViewHistory("Engineering")),
                CategoryCardFactory.createCard("Chemistry", counts.getOrDefault("Chemistry", 0), "#ffb74d", event -> handleViewHistory("Chemistry")),
                CategoryCardFactory.createCard("Physical Education", counts.getOrDefault("Physical Education", 0), "#ff6b6b", event -> handleViewHistory("Physical Education")),
                CategoryCardFactory.createCard("Multi Media", counts.getOrDefault("Multi Media", 0), "#9575cd", event -> handleViewHistory("Multi Media")),
                CategoryCardFactory.createCard("Medical Sciences", counts.getOrDefault("Medical Sciences", 0), "#4dd0e1", event -> handleViewHistory("Medical Sciences")),
                CategoryCardFactory.createCard("Agriculture", counts.getOrDefault("Agriculture", 0), "#81c784", event -> handleViewHistory("Agriculture")),
                CategoryCardFactory.createCard("Architecture", counts.getOrDefault("Architecture", 0), "#f06292", event -> handleViewHistory("Architecture")),
                CategoryCardFactory.createCard("IT", counts.getOrDefault("IT", 0), "#3f51b5", event -> handleViewHistory("IT"))
        );
    }

    private void handleViewHistory(String category) {
        System.out.println("Redirecting to History Screen for: " + category);
        // Use globally accessible Singleton shell instance to jump views and set selection filters
        screens.appshell.ApplicationShellController.instance.goToHistoryWithFilter(category);
    }

    private void setupTableColumns() {
        // 1. Details column
        itemColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEquipment().getEquipmentName())
        );

        // 2. Due Date Column with Overdue Highlighting Check
        dueDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDueDate())
        );
        dueDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate dueDate, boolean empty) {
                super.updateItem(dueDate, empty);
                if (empty || dueDate == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(dueDate.toString());

                    if (LocalDate.now().isAfter(dueDate)) {
                        setStyle("-fx-text-fill: #ff4d4d; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // 3. Action returning panel button
        Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> cellFactory = param -> new TableCell<>() {
            private final Button returnBtn = new Button("Return");
            {
                returnBtn.getStyleClass().add("btn-return");
                returnBtn.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    viewModel.returnEquipment(transaction);
                    refreshCategoryCards();
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

    private void refreshCategoryCards() {
        Platform.runLater(() -> {
            cardsContainer.getChildren().clear();
            populateCategoryCards();

            double targetWidth = (categoryScrollPane.getViewportBounds().getWidth() - 55.0) / 4.0;
            double finalWidth = Math.max(targetWidth, 200.0);
            for (Node node : cardsContainer.getChildren()) {
                if (node instanceof VBox) {
                    ((VBox) node).setPrefWidth(finalWidth);
                }
            }

            cardsContainer.requestLayout();
            cardsContainer.layout();
            updateArrowVisibility();
        });
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
        alert.setTitle("Warning");
        alert.setHeaderText("Account Temporarily Blocked");
        alert.setContentText("You were blocked due to an overdue equipment.");
        alert.showAndWait();
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