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

    public String addNewEquipment(String name, String category, String modelNo, String serialNo, String condition, int totalQty, String imgPath) {
        Equipment e = EquipmentBuilder.start(category)
                .setInfo(0, name, modelNo)                  // 0 id is placeholder
                .setDetails(serialNo, condition, imgPath)
                .setInventory(totalQty, totalQty)
                .build();

        Equipment check = equipmentDAO.findByName(name);
        if (check != null) {
            return "Equipment already exists!";
        }

        if (equipmentDAO.save(e)) {
            return "Equipment added successfully!";
        }

        return "Cannot connect to database.";
    }

    public boolean updateEquipment(String name, String condition, int totalQty, String finalPath) {
        Equipment e = equipmentDAO.findByName(name);

        boolean a = equipmentDAO.updateCondition(e.getEquipmentID(), condition);
        boolean b = equipmentDAO.updateQuantity(e.getEquipmentID(), totalQty, e.getAvailableQty());
        boolean c = equipmentDAO.updateImagePath(e.getEquipmentID(), finalPath);

        System.out.println(a + " " + b + " " + c);

        return a && b && c;
    }
}
