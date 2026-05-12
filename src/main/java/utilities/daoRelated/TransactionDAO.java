package utilities.daoRelated;

import data.Transaction;
import data.User;
import utilities.equipmentRelated.EquipmentBuilder;
import utilities.sqlRelated.MySqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionDAO implements GeneralDAO<Transaction> {

    private final String FIND_ALL = "SELECT * FROM transaction";
    private final String FIND_BY_ID = "SELECT t.*, u.*, e.* FROM transaction t JOIN users u ON t.userID = u.userID JOIN equipment e ON t.equipmentID = e.equipmentID WHERE transactionID = ?";
    private final String INSERT_EQUIPMENT = "INSERT INTO transaction (equipmentID, userID, dateBorrowed, dateReturned)";

    @Override
    public boolean save(Transaction transaction) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Transaction findByID(int id) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapTransaction(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        return List.of();
    }

    private Transaction mapTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("transactionID"),
                new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("userType")
                ),
                EquipmentBuilder.start(rs.getString("category"))
                        .setInfo(rs.getInt("e.equipmentID"),
                                rs.getString("equipmentName"),
                                rs.getString("modelNo"))
                        .setDetails(rs.getString("serialNo"),
                                rs.getString("condition"),
                                rs.getString("imagePath"))
                        .setInventory(rs.getInt("totalQty"),
                                rs.getInt("availableQty"))
                        .build(),
                rs.getObject("dateBorrowed", LocalDate.class),
                rs.getObject("dateReturned", LocalDate.class)
        );
    }
}
