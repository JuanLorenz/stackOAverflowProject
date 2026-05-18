package screens.dashboard;

import data.equipment.Equipment;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import utilities.service.EquipmentService;

import java.util.List;

public class DashboardAdminViewModel {
    private static final ObservableList<Equipment> masterData = FXCollections.observableArrayList();

    private final FilteredList<Equipment> filteredData;
    private final EquipmentService equipmentService = new EquipmentService();

    private final StringProperty searchQuery = new SimpleStringProperty("");

    public DashboardAdminViewModel() {
        this.filteredData = new FilteredList<>(masterData, p -> true);

        searchQuery.addListener((obs, old, val) -> applyFilters());
    }

    /**
     * Uses a background Task so the UI doesn't freeze during SQL fetch.
     */
    public void ensureDataLoaded(Runnable onComplete) {
        // If data is already cached, just run the completion logic immediately
        if (!masterData.isEmpty()) {
            if (onComplete != null) onComplete.run();
            return;
        }

        // Otherwise, fetch from DB on a background thread
        Task<List<Equipment>> loadTask = new Task<>() {
            @Override
            protected List<Equipment> call() {
                return equipmentService.getAllEquipment(); // Your SQL call
            }
        };

        loadTask.setOnSucceeded(e -> {
            masterData.setAll(loadTask.getValue());
            if (onComplete != null) onComplete.run();
        });

        new Thread(loadTask).start();
    }

    public void refreshDataQuietly() {
        Task<List<Equipment>> refreshTask = new Task<>() {
            @Override
            protected List<Equipment> call() {
                return equipmentService.getAllEquipment();
            }
        };

        refreshTask.setOnSucceeded(e -> {
            masterData.setAll(refreshTask.getValue());
        });

        new Thread(refreshTask).start();
    }

    private void applyFilters() {
        String search = searchQuery.get().toLowerCase().trim();

        filteredData.setPredicate(item -> {
            boolean matchesSearch = search.isEmpty() ||
                    item.getEquipmentName().toLowerCase().contains(search) ||
                    item.getModelNo().toLowerCase().contains(search);

            return matchesSearch;
        });
    }

    // Clear cache (Call this during Logout)
    public static void clearCache() {
        masterData.clear();
    }

    // Getters for Controller
    public StringProperty searchQueryProperty() { return searchQuery; }
    public FilteredList<Equipment> getFilteredData() { return filteredData; }
}
