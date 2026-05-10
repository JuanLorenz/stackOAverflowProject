package utilities.sqlRelated;

import data.Equipment;
import data.GenEquipment;
import utilities.daoRelated.EquipmentDAO;

import java.util.ArrayList;
import java.util.List;

public class EquipmentService {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.findAll();
    }
}
