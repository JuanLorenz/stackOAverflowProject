package screens.dashboard;

import data.User;
import data.equipment.Equipment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import utilities.manager.SceneManager;
import utilities.manager.SerializeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardUserController {

    @FXML private StackPane overlayPane;
    @FXML private TextField tfSearch;
    @FXML private HBox categoryPill;
    @FXML private ImageView ivCategoryIcon;
    @FXML private ComboBox<String> cbCategory;
    @FXML private TilePane equipmentGrid;

    // Maps to keep track of UI themes per category
    private final DashboardUserViewModel viewModel = new DashboardUserViewModel();
    private final Map<String, String> categoryColors = new HashMap<>();
    private final Map<String, String> categoryIcons = new HashMap<>();

    @FXML
    public void initialize() {
        setupCategoryMapping();
        setupComboBox();

        // 1. Bind UI properties to ViewModel
        tfSearch.textProperty().bindBidirectional(viewModel.searchQueryProperty());
        cbCategory.valueProperty().bindBidirectional(viewModel.selectedCategoryProperty());

        // 2. Multithreaded Data Loading
        // We show a blank screen/loading state until ensureDataLoaded finishes
        viewModel.ensureDataLoaded(() -> {
            // This runs on the UI Thread once the background Task is done
            renderGrid();

            // Re-render when the filter changes the results
            viewModel.getFilteredData().addListener((ListChangeListener<Equipment>) c -> renderGrid());

            // Update the Pill colors when category changes
            viewModel.selectedCategoryProperty().addListener((obs, old, newVal) -> updateCategoryUI(newVal));

            // Initial UI update
            updateCategoryUI(viewModel.selectedCategoryProperty().get());
        });

        //update equipment quantity if user has returned the equipment
        viewModel.refreshDataQuietly();
    }

    private void renderGrid() {
        // We use viewModel.getFilteredData() here
        List<Button> cards = viewModel.getFilteredData().stream()
                .map(item -> EquipmentCardFactory.createCard(item, this::openBorrowPopup))
                .collect(Collectors.toList());

        Platform.runLater(() -> equipmentGrid.getChildren().setAll(cards));
    }

    private void setupComboBox() {
        cbCategory.setItems(FXCollections.observableArrayList(
                "All Equipment", "Engineering", "Chemistry", "Physical Education",
                "Multi Media", "Medical Sciences", "Information Technology", "Architecture", "Agriculture"
        ));
        cbCategory.setValue("All Equipment");
    }

    private void updateCategoryUI(String category) {
        // Change Pill Color
        String color = categoryColors.getOrDefault(category, "#45D8C9");
        categoryPill.setStyle("-fx-background-color: " + color + ";");

        // Change Pill Icon
        String iconPath = "/media/icons/" + categoryIcons.getOrDefault(category, "icon-equipment.png");
        try {
            ivCategoryIcon.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }
    }

    private void openBorrowPopup(Equipment item) {
        User currentUser = SerializeManager.deserializeUser();

        // 2. Intercept the click if the user is blocked
        if (currentUser != null && currentUser.isBlocked()) {
            showBlockedWarning();
        } else {

            SceneManager.showOverlay("/screens/popup/BorrowEquipmentPopup.fxml", item);
        }
    }

    private void showBlockedWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Action Denied");
        alert.setHeaderText("Account Temporarily Blocked");
        alert.setContentText("You cannot borrow new items right now because you have overdue equipment. Please return your overdue items to restore your borrowing privileges.");
        alert.showAndWait();
    }

    private void setupCategoryMapping() {
        // Colors
        categoryColors.put("Engineering", "#B0825F");
        categoryColors.put("Chemistry", "#FFB347");
        categoryColors.put("Physical Education", "#FF6B6B");
        categoryColors.put("Multi Media", "#9B7CC3");
        categoryColors.put("Medical Sciences", "#48C9B0");
        categoryColors.put("Information Technology", "#4461CC");
        categoryColors.put("Architecture", "#5DADE2");
        categoryColors.put("Agriculture", "#82E0AA");

        // Icons
        categoryIcons.put("Engineering", "icon-engineering.png");
        categoryIcons.put("Chemistry", "icon-chemistry.png");
        categoryIcons.put("Physical Education", "icon-physical-education.png");
        categoryIcons.put("Multi Media", "icon-multi-media.png");
        categoryIcons.put("Medical Sciences", "icon-medical-sciences.png");
        categoryIcons.put("Information Technology", "icon-information-technology.png");
        categoryIcons.put("Architecture", "icon-architecture.png");
        categoryIcons.put("Agriculture", "icon-agriculture.png");
    }
}