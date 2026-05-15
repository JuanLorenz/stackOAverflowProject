package screens.dashboard;

import data.equipment.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import utilities.service.EquipmentService;

public class DashboardViewModel {
    private final ObservableList<Equipment> masterData = FXCollections.observableArrayList();
    private final FilteredList<Equipment> filteredData;
    private final EquipmentService service = new EquipmentService();

    public DashboardViewModel() {
        masterData.addAll(service.getAllEquipment());
        this.filteredData = new FilteredList<>(masterData, p -> true);
    }

    public void filter(String query) {
        filteredData.setPredicate(item -> {
            if (query == null || query.isBlank()) return true;
            String lower = query.toLowerCase();
            return item.getEquipmentName().toLowerCase().contains(lower) ||
                    item.getModelNo().toLowerCase().contains(lower);
        });
    }

    public FilteredList<Equipment> getFilteredData() {
        return filteredData;
    }

    public ObservableList<Equipment> getMasterData() {
        return masterData;
    }
}