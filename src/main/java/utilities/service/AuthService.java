package utilities.service;



import org.mindrot.jbcrypt.BCrypt;
import data.User;
import utilities.database.UserDAO;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    // business logic; checks if given password matches in the database
    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null)   return null;

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}