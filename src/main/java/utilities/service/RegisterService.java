package utilities.service;

import data.User;
import org.mindrot.jbcrypt.BCrypt;
import utilities.database.UserDAO;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO();

    public int register(String name, String email, String password, String role) {
        if (userDAO.findByEmail(email) != null) {
            return 0;
        }

        // register ser
        if (userDAO.save(new User(0, name, email, BCrypt.hashpw(password, BCrypt.gensalt()),role, false, "equipments/"))) {
            return 1;
        }
        return -1;
    }
}