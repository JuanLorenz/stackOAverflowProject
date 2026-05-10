package utilities.daoRelated;

import data.Equipment;

import java.util.List;

public class EquipmentDAO implements GeneralDAO<Equipment> {

    @Override
    public boolean save(Equipment entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Equipment findByID(int id) {
        return null;
    }

    @Override
    public List<Equipment> findAll() {
        return List.of();
    }
}