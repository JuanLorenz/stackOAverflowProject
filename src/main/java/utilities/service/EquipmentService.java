package utilities.service;

import data.equipment.Equipment;
import utilities.database.EquipmentDAO;
import data.equipment.EquipmentBuilder;

import java.util.List;

public class EquipmentService {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.findAll();
    }

    public Boolean addNewEquipment(String name, String category, String modelNo, String serialNo, String condition, int totalQty, String imgPath) {
        Equipment e = EquipmentBuilder.start(category)
                .setInfo(0, name, modelNo)                  // 0 id is placeholder
                .setDetails(serialNo, condition, imgPath)
                .setInventory(totalQty, totalQty)
                .build();

        if (equipmentDAO.save(e)) {
            return true;
        }

        return false;
    }
}
