        package screens.home;

        import data.User;

        import javafx.geometry.Insets;
        import javafx.geometry.Pos;

        import javafx.geometry.VPos;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.ListCell;
        import javafx.scene.layout.*;

        import java.util.*;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.Parent;
        import javafx.scene.control.ListView;
        import javafx.scene.control.TextField;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.KeyEvent;
        import javafx.scene.input.MouseEvent;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Scene;
        import javafx.scene.paint.Color;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import javafx.stage.StageStyle;
        import screens.popup.AddAdminPopupController;
        import utilities.database.UserDAO;

        import java.io.IOException;
        import java.util.Objects;

        public class HomeAdminController {
            public Button addAdminButton;
            public ListView adminsListView;
            public Label lblEmptyAdminList;
            public TextField txtfldSearchField;
            public ImageView btnMenu;
            private List<User> allAdmins = new ArrayList<>();
            private final ObservableList<User> filteredAdmins = FXCollections.observableArrayList();

            @FXML
            public void initialize() {

                adminsListView.setCellFactory(param -> new ListCell<User>() {

                    private final HBox outer = new HBox();
                    private final HBox card = new HBox();
                    private final Label lblInfo = new Label();
                    private final Region spacer = new Region();
                    private final Button btnDelete = new Button("Delete Admin");

                    {
                        outer.setPadding(new Insets(5, 10, 5, 10));
                        outer.setSpacing(12);
                        outer.setAlignment(Pos.CENTER_LEFT);
                        outer.setStyle("-fx-background-color: transparent;");
                        outer.setMaxWidth(Double.MAX_VALUE);

                        card.setAlignment(Pos.CENTER_LEFT);
                        card.setPadding(new Insets(10, 16, 10, 16));
                        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
                        HBox.setHgrow(card, Priority.ALWAYS); // card stretches, button stays fixed

                        card.getChildren().add(lblInfo);

                        btnDelete.setStyle(
                                "-fx-background-color: #ff4d4d;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 10;" +
                                        "-fx-padding: 6 18 6 18;" +
                                        "-fx-cursor: hand;"
                        );

                        outer.getChildren().addAll(card, btnDelete);
                    }

                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);

                        if (empty || user == null) {
                            setGraphic(null);
                            setText(null);
                            setStyle("-fx-background-color: transparent;");
                            setPrefHeight(0);
                            return;
                        }

                        lblInfo.setText(user.getName() + " - " + user.getEmail());
                        btnDelete.setOnAction(event -> handleDeleteAdmin(user.getId()));

                        setPrefHeight(60);
                        setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                        setGraphic(outer);
                    }
                });

                loadAdmins();
            }

            private void handleDeleteAdmin(int id) {
                UserDAO userDAO = new UserDAO();
                boolean success = userDAO.removeUser(id);

                if (success) {
                    loadAdmins();
                } else {
                    System.out.println("Failed to delete user");
                }
            }

            public void handleAddAdmin(ActionEvent event) {
                try {

                    FXMLLoader loader = new FXMLLoader(
                            Objects.requireNonNull(
                                    getClass().getResource("/screens/popup/AddAdminPopup.fxml")
                            )
                    );

                    Parent root = loader.load();

                    AddAdminPopupController popupController = loader.getController();

                    popupController.setHomeController(this);

                    Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initStyle(StageStyle.TRANSPARENT);
                    dialog.setTitle("Add Admin");

                    Scene scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);

                    dialog.setScene(scene);

                    Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    dialog.initOwner(owner);

                    dialog.showAndWait();

                    loadAdmins();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void handleSearch(KeyEvent keyEvent) {

                String query = txtfldSearchField.getText().toLowerCase().trim();

                if (query.isEmpty()) {
                    filteredAdmins.setAll(allAdmins);
                } else {
                    filteredAdmins.setAll(
                            allAdmins.stream()
                                    .filter(user ->
                                            user.getName().toLowerCase().contains(query) ||
                                                    user.getEmail().toLowerCase().contains(query)
                                    )
                                    .toList()
                    );
                }

                lblEmptyAdminList.setVisible(filteredAdmins.isEmpty());
            }

            public void handleMenu(MouseEvent mouseEvent) {
            }

            public void loadAdmins() {

                UserDAO userDAO = new UserDAO();

                allAdmins = userDAO.getAllAdmins();

                filteredAdmins.setAll(allAdmins);

                adminsListView.setItems(filteredAdmins);

                lblEmptyAdminList.setVisible(allAdmins.isEmpty());
            }
        }
