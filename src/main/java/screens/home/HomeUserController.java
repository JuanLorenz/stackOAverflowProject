package screens.home;

import data.equipment.Equipment;
import data.equipment.MultimediaEquipment;
import data.Transaction;
import data.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import utilities.manager.SerializeManager;

import java.time.LocalDate;

public class HomeUserController {

    // --- UI Elements ---
    @FXML private Label lblName;
    @FXML private Label lblEmail;

    @FXML private TableView<Transaction> borrowedTable;
    @FXML private TableColumn<Transaction, String> itemColumn;
    @FXML private TableColumn<Transaction, Void> actionColumn;

    @FXML
    public void initialize() {
        setupUserProfile();
        setupTableColumns();
        loadBorrowedData();
    }

    /**
     * Sets the user banner at the top of the screen.
     */
    private void setupUserProfile() {
        //deserialize User
        User currentUser = SerializeManager.deserializeUser();

        lblName.setText(currentUser.getName());
        lblEmail.setText("Email: " + currentUser.getEmail());
    }

    /**
     * Configures how the table reads data and generates the Return buttons.
     */
    private void setupTableColumns() {
        // 1. Setup Item Column (Extracting the equipment name dynamically)
        itemColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEquipment().getEquipmentName())
        );

        // 2. Setup Action Column (Generating the Red "Return" Buttons)
        Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Transaction, Void> call(final TableColumn<Transaction, Void> param) {
                return new TableCell<>() {

                    // Create the button
                    private final Button returnBtn = new Button("Return");

                    {
                        // Apply your red CSS class
                        returnBtn.getStyleClass().add("btn-return");

                        // Define what happens when clicked
                        returnBtn.setOnAction((event) -> {
                            // Get the specific transaction for the row that was clicked
                            Transaction transactionToReturn = getTableView().getItems().get(getIndex());

                            System.out.println("Returning item: " + transactionToReturn.getEquipment().getEquipmentName());

                            // TODO: Call your SQL Database logic here to mark it as returned!
                            // databaseService.returnItem(transactionToReturn.getTransactionID());

                            // Visually remove it from the table immediately
                            getTableView().getItems().remove(transactionToReturn);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Center the button inside the cell using an HBox
                            HBox centeredPane = new HBox(returnBtn);
                            centeredPane.setStyle("-fx-alignment: center;");
                            setGraphic(centeredPane);
                        }
                    }
                };
            }
        };

        // Apply the button factory to the column
        actionColumn.setCellFactory(cellFactory);
    }

    /**
     * Loads the items currently borrowed by the user.
     */
    private void loadBorrowedData() {

        //Mock
        User currentUser = SerializeManager.deserializeUser();
        Equipment eq1 = new MultimediaEquipment(101, "Topcon Total Station", "TS-100", "SN1", "Good", 5, 4, "") {};

        LocalDate today = LocalDate.now();
        ObservableList<Transaction> borrowedList = FXCollections.observableArrayList(
                new Transaction(1, currentUser, eq1, today)
//                new Transaction(2, user, eq2, today, today),
//                new Transaction(3, user, eq3, today, today)
        );
//
        borrowedTable.setItems(borrowedList);
    }
}