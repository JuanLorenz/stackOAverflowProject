package utilities.daoRelated;

import data.User;
import org.mindrot.jbcrypt.BCrypt;
import utilities.sqlRelated.MySqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    // tried my best to adhere to single responsibility principle
    private final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    // finds user from database that matches the provided id
    public User findUser(int id) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }

    // finds user from database that matches the provided email
    public User findUser(String email) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_BY_EMAIL)) {

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }

    // helper; makes the user class derived from the database
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
