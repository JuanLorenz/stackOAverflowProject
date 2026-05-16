package screens.home;

import data.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import utilities.database.UserDAO;
import utilities.manager.SceneManager;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminController {
    @FXML private ListView<User> adminsListView;
    @FXML private Label lblEmptyAdminList;
    @FXML private TextField txtfldSearchField;

    private List<User> allAdmins = new ArrayList<>();
    private final ObservableList<User> filteredAdmins = FXCollections.observableArrayList();
    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        setupCellFactory();

        // Listen for search typing
        txtfldSearchField.textProperty().addListener((obs, old, newValue) -> handleSearch(newValue));

        loadAdmins();
    }

    private void setupCellFactory() {
        adminsListView.setCellFactory(param -> new ListCell<>() {
            private final HBox outer = new HBox(12);
            private final HBox card = new HBox();
            private final Label lblInfo = new Label();
            private final Button btnDelete = new Button("Delete Admin");

            {
                outer.setPadding(new Insets(5, 10, 5, 10));
                outer.setAlignment(Pos.CENTER_LEFT);

                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(10, 16, 10, 16));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
                HBox.setHgrow(card, Priority.ALWAYS);
                card.getChildren().add(lblInfo);

                btnDelete.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");
                outer.getChildren().addAll(card, btnDelete);
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setGraphic(null);
                } else {
                    lblInfo.setText(user.getName() + " (" + user.getEmail() + ")");
                    btnDelete.setOnAction(e -> handleDeleteAdmin(user.getId()));
                    setGraphic(outer);
                }
            }
        });
    }

    public void loadAdmins() {
        Task<List<User>> task = new Task<>() {
            @Override protected List<User> call() { return userDAO.getAllAdmins(); }
        };

        task.setOnSucceeded(e -> {
            allAdmins = task.getValue();
            filteredAdmins.setAll(allAdmins);
            adminsListView.setItems(filteredAdmins);
            lblEmptyAdminList.setVisible(allAdmins.isEmpty());
        });

        new Thread(task).start();
    }

    @FXML
    private void handleDeleteAdmin(int id) {
        if (userDAO.removeUser(id)) {
            loadAdmins();
        }
    }

    @FXML
    public void handleAddAdmin() {
        // We pass "this::loadAdmins" as data. 
        // The SceneManager will inject this Runnable into the Popup Controller!
        SceneManager.showOverlay("/screens/popup/AddAdminPopup.fxml", (Runnable) this::loadAdmins);
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
}