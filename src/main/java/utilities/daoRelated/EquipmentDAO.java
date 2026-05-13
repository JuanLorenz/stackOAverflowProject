package utilities.daoRelated;

import data.Equipment;
import data.*;
import utilities.equipmentRelated.EquipmentBuilder;
import utilities.sqlRelated.MySqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO implements GeneralDAO<Equipment> {

    private final String FIND_ALL = "SELECT * FROM equipment";
    private final String FIND_BY_ID = "SELECT * FROM equipment WHERE equipmentID = ?";
    private final String FIND_BY_NAME = "SELECT * FROM equipment WHERE equipmentName = ?";
    private final String INSERT_EQUIPMENT = "INSERT INTO equipment (equipmentName, category, modelNo, serialNo, condition, totalQty, availableQty, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_CONDITION = "UPDATE equipment SET condition = ? WHERE id = ?";
    private final String UPDATE_QUANTITY = "UPDATE equipment SET totalQty = ?, availableQty = ? WHERE id = ?";

    @Override
    public boolean save(Equipment equipment) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(INSERT_EQUIPMENT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, equipment.getEquipmentName());
            statement.setString(2, equipment.getCategory());
            statement.setString(3, equipment.getModelNo());
            statement.setString(4, equipment.getSerialNo());
            statement.setString(5, equipment.getCondition());
            statement.setInt(6, equipment.getTotalQty());
            statement.setInt(7, equipment.getAvailableQty());
            statement.setString(8, equipment.getImagePath());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        equipment.setEquipmentID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Failed to save equipment: " + e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Equipment findByID(int id) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapEquipment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Equipment> findAll() {
        List<Equipment> equipments = new ArrayList<>();

        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_ALL)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                equipments.add(mapEquipment(rs));
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }

        return equipments;
    }

    public Equipment findByName(String name) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_NAME)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapEquipment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }

    public boolean updateCondition(int id, String condition) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(UPDATE_CONDITION)) {

            statement.setString(1, condition);
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Failed to update equipment: " + e.getMessage());
            return false;
        }
    }

    public boolean updateQuantity(int id, int total, int available) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(UPDATE_QUANTITY)) {

            statement.setInt(1, total);
            statement.setInt(2, available);
            statement.setInt(3, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Failed to update equipment: " + e.getMessage());
            return false;
        }
    }

    private Equipment mapEquipment(ResultSet rs) throws SQLException {
        return EquipmentBuilder.start(rs.getString("category"))
                .setInfo(rs.getInt("equipmentID"),
                        rs.getString("equipmentName"),
                        rs.getString("modelNo"))
                .setDetails(rs.getString("serialNo"),
                        rs.getString("condition"),
                        rs.getString("imagePath"))
                .setInventory(rs.getInt("totalQty"),
                        rs.getInt("availableQty"))
                .build();
    }
}