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
    private final String INSERT_USER   = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

    /**
     * Finds the user in the database given an ID.
     * @return User - if user exists <br>
     * null - if user does not exist
     */
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

    /**
     * Finds the user in the database given an email.
     * @return User - if user exists <br>
     * null - if user does not exist
     */
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

    /**
     * Saves the new user in the database. Used when adding a new user via register.
     */
    public boolean addUser(String name, String email, String password) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(INSERT_USER)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password); // already hashed

            return statement.executeUpdate() > 0; // true if row was inserted

        } catch (SQLException e) {
            System.err.println("Failed to save user: " + e.getMessage());
            return false;
        }
    }

    /**
     * UserDAO helper.
     * Maps the ResultSet retrieved from the database into a User class.
     * @return User
     */
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
