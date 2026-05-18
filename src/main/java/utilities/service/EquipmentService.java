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

    public boolean addNewEquipment(Equipment e) {
        Equipment check = equipmentDAO.findByName(e.getEquipmentName());
        if (check != null) {
            System.out.println("Equipment already exists!");
            return false;
        }

        if (equipmentDAO.save(e)) {
            System.out.println("Equipment added successfully!");
            return true;
        }
        System.out.println("Cannot connect to database.");
        return false;
    }

    public boolean updateEquipment(Equipment e) {
        boolean a = equipmentDAO.updateCondition(e.getEquipmentID(), e.getCondition());
        boolean b = equipmentDAO.updateQuantity(e.getEquipmentID(), e.getTotalQty(), e.getAvailableQty());
        boolean c = equipmentDAO.updateImagePath(e.getEquipmentID(), e.getImagePath());

        System.out.println(a + " " + b + " " + c);

        return a && b && c;
    }
}
