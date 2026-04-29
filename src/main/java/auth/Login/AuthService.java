package auth.Login;



import org.mindrot.jbcrypt.BCrypt;
import shared.MySqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static User authenticate(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String dbPasswordHash = rs.getString("password");

                if (BCrypt.checkpw(password, dbPasswordHash)) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            dbPasswordHash
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
        }
        return null;
    }
}