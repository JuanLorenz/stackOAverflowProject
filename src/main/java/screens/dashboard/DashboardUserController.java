package screens.dashboard;

import data.equipment.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import screens.popup.BorrowEquipmentPopupController;
import utilities.service.EquipmentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DashboardUserController {

    @FXML private StackPane overlayPane;
    @FXML private TextField tfSearch;
    @FXML private HBox categoryPill;
    @FXML private ImageView ivCategoryIcon;
    @FXML private ComboBox<String> cbCategory;
    @FXML private TilePane equipmentGrid;

    private final EquipmentService equipmentService = new EquipmentService();
    private final ObservableList<Equipment> masterData = FXCollections.observableArrayList();
    private FilteredList<Equipment> filteredData;

    // Maps to keep track of UI themes per category
    private final Map<String, String> categoryColors = new HashMap<>();
    private final Map<String, String> categoryIcons = new HashMap<>();

    @FXML
    public void initialize() {
        setupCategoryMapping();

        // 1. Initialize ComboBox items
        cbCategory.setItems(FXCollections.observableArrayList(
                "All Equipment", "Engineering", "Chemistry", "Physical Education",
                "Multi Media", "Medical Sciences", "Information Technology", "Architecture", "Agriculture"
        ));
        cbCategory.setValue("All Equipment");
        updateCategoryUI("All Equipment"); // Set initial Teal theme

        // 2. Fetch Data
        masterData.addAll(equipmentService.getAllEquipment());
        filteredData = new FilteredList<>(masterData, p -> true);

        // 3. Listeners for filtering
        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        cbCategory.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateCategoryUI(newVal);
            updateFilter();
        });

        // 4. Initial Render
        renderGrid();
    }

    private void updateFilter() {
        String searchText = tfSearch.getText().toLowerCase().trim();
        String selectedCategory = cbCategory.getValue();

        filteredData.setPredicate(item -> {
            boolean matchesSearch = searchText.isEmpty() ||
                    item.getEquipmentName().toLowerCase().contains(searchText) ||
                    item.getModelNo().toLowerCase().contains(searchText);

            boolean matchesCategory = selectedCategory.equals("All Equipment") ||
                    item.getCategory().equalsIgnoreCase(selectedCategory);

            return matchesSearch && matchesCategory;
        });

        renderGrid();
    }

    private void renderGrid() {
        equipmentGrid.getChildren().clear();
        for (Equipment item : filteredData) {
            Button card = EquipmentCardFactory.createCard(item, this::openBorrowPopup);
            equipmentGrid.getChildren().add(card);
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/popup/BorrowEquipmentPopup.fxml"));
            Parent popupRoot = loader.load();

            BorrowEquipmentPopupController controller = loader.getController();
            controller.setEquipment(item);

            // Pass the overlayPane to the controller so it can "close" itself
            controller.setOverlayPane(overlayPane);

            overlayPane.getChildren().clear();
            overlayPane.getChildren().add(popupRoot);
            overlayPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupCategoryMapping() {
        // Colors from your 2nd picture
        categoryColors.put("Engineering", "#B0825F");
        categoryColors.put("Chemistry", "#FFB347");
        categoryColors.put("Physical Education", "#FF6B6B");
        categoryColors.put("Multi Media", "#9B7CC3");
        categoryColors.put("Medical Sciences", "#48C9B0");
        categoryColors.put("Information Technology", "#4461CC");
        categoryColors.put("Architecture", "#5DADE2");
        categoryColors.put("Agriculture", "#82E0AA");

        // Icons from your 3rd picture
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