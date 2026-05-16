package screens.home;

import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import utilities.database.UserDAO;
import utilities.manager.SceneManager;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminController {
    @FXML public ListView<User> adminsListView;
    @FXML public Label lblEmptyAdminList;
    @FXML public TextField txtfldSearchField;

    private List<User> allAdmins = new ArrayList<>();
    private final ObservableList<User> filteredAdmins = FXCollections.observableArrayList();
    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        setupListView();
        loadAdmins();
        adminsListView.setSelectionModel(new NoSelectionModel<>());
        // Reactive search listener
        txtfldSearchField.textProperty().addListener((obs, old, newValue) -> handleSearch(newValue));
    }

    private void setupListView() {
        adminsListView.setCellFactory(param -> new ListCell<>() {
            private final HBox outer = new HBox(12);
            private final HBox card = new HBox();
            private final Label lblInfo = new Label();
            private final Button btnDelete = new Button("Delete Admin");

            {
                // 1. The 'outer' container acts as the row.
                // We add bottom padding here to create the 'GAP' between rows.
                outer.setPadding(new Insets(0, 0, 15, 0)); // 15px gap below every row
                outer.setAlignment(Pos.CENTER_LEFT);

                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(12, 20, 12, 20));
                card.getStyleClass().add("admin-list-card");

                // 2. The 'card' is what actually looks like the row (White background)
                card.getStyleClass().add("admin-list-card");
                HBox.setHgrow(card, Priority.ALWAYS);
                card.getChildren().add(lblInfo);

                btnDelete.getStyleClass().add("delete-admin-button");
                btnDelete.setFocusTraversable(false);

                outer.getChildren().addAll(card, btnDelete);

                // 3. Make the actual cell background disappear completely
                setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setGraphic(null);
                } else {
                    lblInfo.setText(user.getName() + " - " + user.getEmail());
                    btnDelete.setOnAction(e -> handleDeleteAdmin(user.getId()));
                    setGraphic(outer);
                }
            }
        });
    }

    public void loadAdmins() {
        Task<List<User>> fetchTask = new Task<>() {
            @Override protected List<User> call() { return userDAO.getAllAdmins(); }
        };

        fetchTask.setOnSucceeded(e -> {
            allAdmins = fetchTask.getValue();
            filteredAdmins.setAll(allAdmins);
            adminsListView.setItems(filteredAdmins);
            lblEmptyAdminList.setVisible(allAdmins.isEmpty());
        });

        new Thread(fetchTask).start();
    }

    @FXML
    public void handleAddAdmin() {
        // Use the centralized SceneManager!
        // We pass "this::loadAdmins" (a Runnable) as the data.
        SceneManager.showOverlay("/screens/popup/AddAdminPopup.fxml", (Runnable) this::loadAdmins);
    }

    private void handleDeleteAdmin(int id) {
        if (userDAO.removeUser(id)) {
            loadAdmins();
        }
    }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            filteredAdmins.setAll(allAdmins);
        } else {
            String lower = query.toLowerCase().trim();
            filteredAdmins.setAll(allAdmins.stream()
                    .filter(u -> u.getName().toLowerCase().contains(lower) || u.getEmail().toLowerCase().contains(lower))
                    .toList());
        }
        lblEmptyAdminList.setVisible(filteredAdmins.isEmpty());
    }

    //this is to make the listview items unclickable
    public static class NoSelectionModel<T> extends MultipleSelectionModel<T> {
        @Override public ObservableList<Integer> getSelectedIndices() { return FXCollections.emptyObservableList(); }
        @Override public ObservableList<T> getSelectedItems() { return FXCollections.emptyObservableList(); }
        @Override public void selectIndices(int index, int... indices) {}
        @Override public void selectAll() {}
        @Override public void selectFirst() {}
        @Override public void selectLast() {}
        @Override public void clearAndSelect(int index) {}
        @Override public void select(int index) {}
        @Override public void select(T obj) {}
        @Override public void clearSelection(int index) {}
        @Override public void clearSelection() {}
        @Override public boolean isSelected(int index) { return false; }
        @Override public boolean isEmpty() { return true; }
        @Override public void selectPrevious() {}
        @Override public void selectNext() {}
    }
}