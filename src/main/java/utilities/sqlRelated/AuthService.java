package utilities.sqlRelated;



import org.mindrot.jbcrypt.BCrypt;
import data.User;
import utilities.daoRelated.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    // business logic; checks if given password matches in the database
    public User login(String email, String password) {
        User user = userDAO.findUser(email);
        if (user == null)   return null;

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}