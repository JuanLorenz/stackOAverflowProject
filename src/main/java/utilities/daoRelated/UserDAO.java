package utilities.daoRelated;

import data.User;
import org.mindrot.jbcrypt.BCrypt;
import utilities.sqlRelated.MySqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements GeneralDAO<User> {
    // tried my best to adhere to single responsibility principle
    private final String FIND_ALL = "SELECT * FROM users";
    private final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private final String INSERT_USER   = "INSERT INTO users (name, email, password, userType) VALUES (?, ?, ?, ?)";
    private final String CHANGE_USER_DETAILS = "UPDATE users SET name = ?, email = ?, password = ? WHERE email = ?";

    /**
     * Finds the user in the database given an ID.
     * @return User - if user exists <br>
     * null - if user does not exist
     */
    @Override
    public User findByID(int id) {
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

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(FIND_ALL)) {

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                users.add(mapUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return users;
    }

    /**
     * Saves the new user in the database. Used when adding a new user via register.
     */
    @Override
    public boolean save(User user) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(INSERT_USER)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());// already hashed
            statement.setString(4, user.getUserType());
            return statement.executeUpdate() > 0; // true if row was inserted

        } catch (SQLException e) {
            System.err.println("Failed to save user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id){
        return false;
    }

    /**
     * Finds the user in the database given an email.
     * @return User - if user exists <br>
     * null - if user does not exist
     */
    public User findByEmail(String email) {
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
     * Changes the user's field details based on the inputted new values.
     * @return true - if query successful <br>
     *         false - if query failed
     */
    public boolean changeUserDetails(String currentEmail, String newEmail, String name, String password) {
        try (
                Connection c = MySqlConnection.getConnection();
                PreparedStatement statement = c.prepareStatement(CHANGE_USER_DETAILS)
        ) {

            statement.setString(1, name);
            statement.setString(2, newEmail);
            statement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
            statement.setString(4, currentEmail);

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Database connection or query failed: " + e.getMessage()
            );
        }
        return false;
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
                rs.getString("password"),
                rs.getString("userType")
        );
    }
}
