package utilities.sqlRelated;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import utilities.daoRelated.UserDAO;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO();

    public int registerUser(String name, String email, String password) {
        if (userDAO.findUser(email) != null) {
            return 0;
        }

        if (userDAO.addUser(name, email, BCrypt.hashpw(password, BCrypt.gensalt()))) {
            return 1;
        }
        return -1;

//        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
//
//        try (Connection c = MySqlConnection.getConnection();
//             PreparedStatement statement = c.prepareStatement(query)) {
//
//            // This will hash password so when testing please remember your password
//            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//
//            statement.setString(1, name);
//            statement.setString(2, email);
//            statement.setString(3, hashedPassword);
//
//            return statement.executeUpdate();
//
//        } catch (SQLIntegrityConstraintViolationException e) {
//            System.err.println("Registration failed: Email already exists.");
//            return 0;
//        } catch (SQLException e) {
//            System.err.println("Registration failed due to a database error: " + e.getMessage());
//            return -1;
//        }
    }
}