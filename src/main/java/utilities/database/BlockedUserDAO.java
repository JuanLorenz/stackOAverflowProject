package utilities.database;

import data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BlockedUserDAO implements ContractDAO<User>{
    private final String FIND_ALL = "SELECT u.*, b.blockedUntil FROM users u JOIN blockedusers b ON u.id = b.userID";
    private final String FIND_BY_ID = "SELECT u.*, b.blockedUntil FROM users u JOIN blockedusers b ON u.id = b.userID WHERE u.id = ?";
    private final String INSERT_BLOCKEDUSER = "INSERT INTO blockedusers (userID, blockedUntil) VALUES (?, ?)";
    private final String DELETE_BLOCKEDUSER = "DELETE FROM blockedusers WHERE userID = ?";

    @Override
    public boolean save(User user) {
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(INSERT_BLOCKEDUSER)) {

            statement.setInt(1, user.getId());
            statement.setObject(2, user.getUnblockedDate());
            return statement.executeUpdate() > 0; // true if row was inserted

        } catch (SQLException e) {
            System.err.println("Failed to save user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(DELETE_BLOCKEDUSER)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0; // Returns true if a row was actually deleted
        } catch (SQLException e) {
            System.err.println("Failed to delete user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User findByID(int id) {
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapBlockedUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> blockedusers = new ArrayList<>();
        try (Connection c = ConnectionSQL.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_ALL)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                blockedusers.add(mapBlockedUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return blockedusers;
    }

    private User mapBlockedUser(ResultSet rs) throws SQLException {
        User u = new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("userType"),
                rs.getBoolean("isBlocked"),
                rs.getString("profilePhotoPath")
        );
        u.setBlockedUntil(rs.getObject("blockedUntil", LocalDate.class));
        return u;
    }
}
