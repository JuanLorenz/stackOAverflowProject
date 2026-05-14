package screens.historyUser;

import data.Transaction;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import utilities.serializationRelated.SerializeManager;
import utilities.sqlRelated.TransactionService;

public class HistoryUserController {
    public ComboBox<String> cbCategory;
    public TextField tfSearchEquipment;
    public VBox tableWrapper;
    public HBox tableHeader;
    public VBox rowsContainer;

    private final User currentUser = SerializeManager.deserializeUser();
    private final TransactionService transactionService = new TransactionService();
    private final ObservableList<Transaction> masterData = FXCollections.observableArrayList();
    private FilteredList<Transaction> filteredData;

    public void initialize() {
        // retrieve the transactions of user (user should not be null)
        assert currentUser != null;
        masterData.addAll(transactionService.getUserHistory(currentUser.getId()));

        // set up FilteredList
        filteredData = new FilteredList<>(masterData, p -> true);

        // initialize ComboBox
        cbCategory.setItems(FXCollections.observableArrayList("All", "Engineering", "Chemistry", "Physical Education", "Multi Media", "Medical Sciences", "IT", "Architecture", "Agriculture"));
        cbCategory.setValue("All");

        // event listeners
        tfSearchEquipment.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        cbCategory.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        filteredData.addListener((ListChangeListener<Transaction>) c -> refreshTable());

        // show table
        refreshTable();
    }

    /**
     * Logic to decide which transactions stay in the list based on Search and Category
     */
    private void updateFilter() {
        String searchText = tfSearchEquipment.getText().toLowerCase().trim();
        String selectedCategory = cbCategory.getValue();

        filteredData.setPredicate(transaction -> {
            // Filter A: Search Text (Name or Model)
            boolean matchesSearch = searchText.isEmpty() ||
                    transaction.getEquipment().getEquipmentName().toLowerCase().contains(searchText) ||
                    transaction.getEquipment().getModelNo().toLowerCase().contains(searchText);

            // Filter B: Category
            boolean matchesCategory = (selectedCategory == null || selectedCategory.equals("All")) ||
                    transaction.getEquipment().getCategory().equalsIgnoreCase(selectedCategory);

            return matchesSearch && matchesCategory;
        });
    }

    /**
     * Clears the UI container and rebuilds the rows based on the current filtered data
     */
    private void refreshTable() {
        rowsContainer.getChildren().clear();

        for (Transaction t : filteredData) {
            rowsContainer.getChildren().add(createRow(t));
        }
    }

    /**
     * Helper to build a single "Pill" row HBox
     */
    private HBox createRow(Transaction t) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        row.setSpacing(10); // Matches FXML spacing if needed
        row.getStyleClass().add("history-row");

        Label[] labels = getLabels(t);
        for (Label l : labels) {
            l.setMaxWidth(Double.MAX_VALUE);
            l.setAlignment(Pos.CENTER);
            l.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
            HBox.setHgrow(l, Priority.ALWAYS);
            row.getChildren().add(l);
        }

        // You can add a CSS class here later for the "Pill" look
        row.getStyleClass().add("history-row");

        return row;
    }

    private Label[] getLabels(Transaction t) {
        // Column 1: Borrow Date
        Label dateBorrowed = new Label(t.getDateBorrowed().toString());

        // Column 2: Equipment Name
        Label name = new Label(t.getEquipment().getEquipmentName());

        // Column 3: Condition
        Label condition = new Label(t.getEquipment().getCondition());

        // Column 4: Date Returned (Check for null)
        String returnStr = (t.getDateReturned() == null) ? "Not Returned" : t.getDateReturned().toString();
        Label dateReturned = new Label(returnStr);

        // Apply Alignment and Growth to each label so they match the FXML Header
        Label[] labels = {dateBorrowed, name, condition, dateReturned};
        return labels;
    }
}
