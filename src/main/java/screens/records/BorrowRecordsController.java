package screens.records;

import data.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import utilities.service.TransactionService;

public class BorrowRecordsController {
    public Button btnActive;
    public Button btnReturned;
    public Label lblStatusHeader;
    public TextField tfSearch;
    public VBox tableWrapper;
    public HBox tableHeader;
    public VBox rowsContainer;

    private final TransactionService transactionService = new TransactionService();
    private final ObservableList<Transaction> masterData = FXCollections.observableArrayList();
    private FilteredList<Transaction> filteredData;

    private boolean showingActivelyBorrowed = true;

    public void initialize() {
        masterData.addAll(transactionService.getAllTransactions());
        filteredData = new FilteredList<>(masterData, p -> true);
        handleShowActive();
    }

    /**
     * Called when an Admin clicks on "Actively Borrowed" tab.
     */
    @FXML
    private void handleShowActive() {
        showingActivelyBorrowed = true;

        btnActive.getStyleClass().add("button-active");
        btnReturned.getStyleClass().remove("button-active");

        updateState();
    }

    @FXML
    private void handleShowReturned() {
        showingActivelyBorrowed = false;

        btnReturned.getStyleClass().add("button-active");
        btnActive.getStyleClass().remove("button-active");

        updateState();
    }

    private void updateState() {
        lblStatusHeader.setText(showingActivelyBorrowed ? "Actively Borrowed" : "Returned");
        filteredData.setPredicate(t -> {
            if (showingActivelyBorrowed) return t.getDateReturned() == null;
            else return t.getDateReturned() != null;
        });
        renderTable();
    }

    private void renderTable() {
        rowsContainer.getChildren().clear();
        tableHeader.getChildren().clear();

        String lastColumn = showingActivelyBorrowed ? "Condition" : "Date Returned";
        String[] columns = {"Date Borrowed", "Borrower", "Equipment", lastColumn};

        // 1. Setup Header
        for (String col : columns) {
            tableHeader.getChildren().add(createColumnLabel(col, true));
        }

        // 2. Setup Rows
        for (Transaction t : filteredData) {
            HBox row = new HBox();
            row.getStyleClass().add("history-row");
            row.setAlignment(Pos.CENTER);

            // Match the spacing of your header if it has any (e.g., tableHeader.getSpacing())
            row.setSpacing(tableHeader.getSpacing());

            String lastVal = showingActivelyBorrowed ?
                    t.getEquipment().getCondition() :
                    t.getDateReturned().toString();

            row.getChildren().addAll(
                    createColumnLabel(t.getDateBorrowed().toString(), false),
                    createColumnLabel(t.getUser().getName(), false),
                    createColumnLabel(t.getEquipment().getEquipmentName(), false),
                    createColumnLabel(lastVal, false)
            );

            rowsContainer.getChildren().add(row);
        }
    }

    /**
     * HELPER METHOD: Creates a label with fixed growth constraints
     */
    private Label createColumnLabel(String text, boolean isHeader) {
        Label label = new Label(text);

        // Set prefWidth to 0 and HGrow to ALWAYS.
        // This forces HBox to divide the total width into exactly equal segments.
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefWidth(0);
        HBox.setHgrow(label, Priority.ALWAYS);

        label.setAlignment(Pos.CENTER);

        if (isHeader) {
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");
        } else {
            label.setWrapText(true); // Ensures long text doesn't break the layout
            label.setTextAlignment(TextAlignment.CENTER);
        }

        return label;
    }
}
