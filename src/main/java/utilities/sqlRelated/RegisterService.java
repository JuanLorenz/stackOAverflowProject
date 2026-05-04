package utilities.sqlRelated;



import org.mindrot.jbcrypt.BCrypt;
import utilities.daoRelated.UserDAO;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO();

    public int register(String name, String email, String password) {
        if (userDAO.findUser(email) != null) {
            return 0;
        }

        if (userDAO.addUser(name, email, BCrypt.hashpw(password, BCrypt.gensalt()))) {
            return 1;
        }
        return -1;
    }
}