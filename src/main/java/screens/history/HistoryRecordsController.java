package screens.history;

import data.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import utilities.service.TransactionService;

public class HistoryRecordsController {
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
        // clear the table (header & contents)
        rowsContainer.getChildren().clear();
        tableHeader.getChildren().clear();

        // initialize the labels (depending on what screen is active)
        String lastColumn = showingActivelyBorrowed ? "Condition" : "Date Returned";
        String[] columns = {"Date Borrowed", "Borrower", "Equipment", lastColumn};

        // for header
        for (String col : columns) {
            Label l = new Label(col);
            l.setMaxWidth(Double.MAX_VALUE);
            l.setAlignment(Pos.CENTER);
            l.setStyle("-fx-font-weight: bold;");
            HBox.setHgrow(l, Priority.ALWAYS);
            tableHeader.getChildren().add(l);
        }

        // for each transaction row
        for (Transaction t : filteredData) {
            HBox row = new HBox();
            row.getStyleClass().add("history-row");
            row.setAlignment(Pos.CENTER);

            Label date = new Label(t.getDateBorrowed().toString());
            Label user = new Label(t.getUser().getName());
            Label equip = new Label(t.getEquipment().getEquipmentName());

            String lastVal = showingActivelyBorrowed ? t.getEquipment().getCondition(): t.getDateReturned().toString();
            Label last = new Label(lastVal);

            for (Label l : new Label[]{date, user, equip, last}) {
                l.setMaxWidth(Double.MAX_VALUE);
                l.setAlignment(Pos.CENTER);
                HBox.setHgrow(l, Priority.ALWAYS);
                row.getChildren().add(l);
            }
            rowsContainer.getChildren().add(row);
        }
    }
}
