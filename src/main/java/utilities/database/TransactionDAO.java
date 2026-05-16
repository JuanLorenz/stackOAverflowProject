package utilities.database;

import data.Transaction;
import data.User;
import data.equipment.EquipmentBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements ContractDAO<Transaction> {

    private final String FIND_ALL = """
            SELECT\s
                t.*,\s
                u.id AS u_id, u.name AS u_name, u.email AS u_email, u.password AS u_pw, u.userType AS u_type,
                e.equipmentID AS e_id, e.equipmentName AS e_name, e.category AS e_cat, e.modelNo AS e_model,\s
                e.serialNo AS e_serial, e.condition AS e_cond, e.totalQty AS e_total,\s
                e.availableQty AS e_avail, e.imagePath AS e_path
            FROM transaction t\s
            JOIN users u ON t.userID = u.id\s
            JOIN equipment e ON t.equipmentID = e.equipmentID
            """;
    private final String FIND_BY_TRANSACTION_ID = FIND_ALL + " WHERE t.transactionID = ?";
    private final String FIND_BY_USER_ID = FIND_ALL + " WHERE u.id = ?";
    private final String INSERT_TRANSACTION = "INSERT INTO transaction (equipmentID, userID, dateBorrowed, dateReturned) VALUES (?, ?, ?, ?)";
    private final String UPDATE_RETURN = "UPDATE transaction SET dateReturned = ? WHERE transactionID = ?";

    @Override
    public boolean save(Transaction transaction) {
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, transaction.getEquipment().getEquipmentID());
            statement.setInt(2, transaction.getUser().getId());
            statement.setObject(3, transaction.getDateBorrowed());
            statement.setObject(4, null);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Failed to save transaction: " + e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Transaction findByID(int id) {
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_TRANSACTION_ID)) {

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
        List<Transaction> transactions = new ArrayList<>();

        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_ALL)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(mapTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }

        return transactions;
    }

    public List<Transaction> findAllByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_USER_ID)) {

            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                transactions.add(mapTransaction(rs));
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch user history: " + e.getMessage());
        }

        return transactions;
    }

    public boolean updateReturn(int id, LocalDate dateReturned) {
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(UPDATE_RETURN)) {

            statement.setObject(1, dateReturned);
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Failed to update equipment: " + e.getMessage());
            return false;
        }
    }

    private Transaction mapTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("transactionID"),
                new User(
                        rs.getInt("u_id"),
                        rs.getString("u_name"),
                        rs.getString("u_email"),
                        rs.getString("u_pw"),
                        rs.getString("u_type"),
                        rs.getBoolean("u_blocked"),
                        rs.getString("u_photo_path")
                ),
                EquipmentBuilder.start(rs.getString("e_cat"))
                        .setInfo(rs.getInt("e_id"),
                                rs.getString("e_name"),
                                rs.getString("e_model"))
                        .setDetails(rs.getString("e_serial"),
                                rs.getString("e_cond"),
                                rs.getString("e_path"))
                        .setInventory(rs.getInt("e_total"),
                                rs.getInt("e_avail"))
                        .build(),
                rs.getObject("dateBorrowed", LocalDate.class)
        );
    }
}
