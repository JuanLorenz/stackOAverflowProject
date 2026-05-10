package screens.home;

import data.Equipment;
import data.Transaction;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Date;

public class HomeController {

    @FXML private TableView<Transaction> borrowedTable;
    @FXML private TableView<Transaction> upcomingTable;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadAndFilterData();
    }

    // not yet sure what to put in the table
    private void setupTableColumns() {
        // --- Borrowed Table ---
        TableColumn<Transaction, String> bItemCol = (TableColumn<Transaction, String>) borrowedTable.getColumns().get(0);
        bItemCol.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));

        TableColumn<Transaction, LocalDate> bDateCol = (TableColumn<Transaction, LocalDate>) borrowedTable.getColumns().get(1);
        bDateCol.setCellValueFactory(new PropertyValueFactory<>("dateBorrowed"));

        // --- Upcoming Table ---
        TableColumn<Transaction, String> uItemCol = (TableColumn<Transaction, String>) upcomingTable.getColumns().get(0);
        uItemCol.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));

        TableColumn<Transaction, LocalDate> uDateCol = (TableColumn<Transaction, LocalDate>) upcomingTable.getColumns().get(1);
        uDateCol.setCellValueFactory(new PropertyValueFactory<>("dateBorrowed"));
    }


    private void loadAndFilterData() {
    }
}