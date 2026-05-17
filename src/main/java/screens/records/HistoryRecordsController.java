package screens.records;

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
import javafx.scene.text.TextAlignment;
import utilities.manager.SerializeManager;
import utilities.service.TransactionService;

public class HistoryRecordsController {
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
        setupHeader();
        refreshTable();
    }

    private void setupHeader() {
        tableHeader.getChildren().clear();
        String[] columns = {"Date Borrowed", "Equipment", "Condition", "Date Returned"};

        for (String col : columns) {
            tableHeader.getChildren().add(createColumnLabel(col, true));
        }
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

    private HBox createRow(Transaction t) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        row.setSpacing(tableHeader.getSpacing());
        row.getStyleClass().add("history-row");

        String returnStr = (t.getDateReturned() == null) ? "Not Returned" : t.getDateReturned().toString();

        row.getChildren().addAll(
                createColumnLabel(t.getDateBorrowed().toString(), false),
                createColumnLabel(t.getEquipment().getEquipmentName(), false),
                createColumnLabel(t.getEquipment().getCondition(), false),
                createColumnLabel(returnStr, false)
        );

        return row;
    }

    /**
     * Helper to create a Label with standardized alignment constraints.
     */
    private Label createColumnLabel(String text, boolean isHeader) {
        Label label = new Label(text);

        // 1. Fill the horizontal space
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefWidth(0);
        HBox.setHgrow(label, Priority.ALWAYS);

        label.setAlignment(Pos.CENTER);

        if (isHeader) {
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");
        } else {
            label.setWrapText(true); // Prevents long names from breaking the table
            label.setTextAlignment(TextAlignment.CENTER);
        }

        return label;
    }
}
