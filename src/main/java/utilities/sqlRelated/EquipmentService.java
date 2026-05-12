package utilities.sqlRelated;

import data.Equipment;
import data.GenEquipment;
import utilities.daoRelated.EquipmentDAO;
import utilities.equipmentRelated.EquipmentBuilder;

import java.util.ArrayList;
import java.util.List;

public class EquipmentService {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.findAll();
    }

    public Equipment addNewEquipment(String name, String category, String modelNo, String serialNo, String condition, int totalQty, String imgPath) {
        Equipment e = EquipmentBuilder.start(category)
                .setInfo(0, name, modelNo)                  // 0 id is placeholder
                .setDetails(serialNo, condition, imgPath)
                .setInventory(totalQty, totalQty)
                .build();

        if (equipmentDAO.save(e)) {
            return e;
        }

        return null;
    }
}
