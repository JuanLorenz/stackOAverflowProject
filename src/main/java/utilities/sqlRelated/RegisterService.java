package utilities.sqlRelated;

import data.User;
import org.mindrot.jbcrypt.BCrypt;
import utilities.daoRelated.UserDAO;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO();

    public int register(String name, String email, String password) {
        if (userDAO.findByEmail(email) != null) {
            return 0;
        }

        // set to "user" ang userType sa mga mu register kay walay maka register nga admin dapat ofc
        if (userDAO.save(new User(0, name, email, BCrypt.hashpw(password, BCrypt.gensalt()),"user"))) {
            return 1;
        }
        return -1;
    }
}