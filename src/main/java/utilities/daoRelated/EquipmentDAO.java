package utilities.daoRelated;

import data.Equipment;
import data.*;
import utilities.sqlRelated.MySqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO implements GeneralDAO<Equipment> {

    private final String FIND_ALL = "SELECT * FROM equipment";

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

    //Bruh, what do I do with the concrete classes
    private Equipment mapEquipment(ResultSet rs) throws SQLException {
        Equipment e;

        String category = rs.getString("category");
        int id = rs.getInt("equipmentID");
        String eq = rs.getString("equipmentName");
        String md = rs.getString("modelNo");
        String sr = rs.getString("serialNo");
        String cd = rs.getString("condition");
        int tl = rs.getInt("totalQty");
        int av = rs.getInt("availableQty");
        String pt = "/images/placeholder-img.png"; //STILL PLACEHOLDER


        return new GenEquipment(id, eq, md, sr, cd, tl, av, pt);
    }
}