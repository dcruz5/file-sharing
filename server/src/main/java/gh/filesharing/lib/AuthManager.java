package gh.filesharing.lib;

import at.favre.lib.crypto.bcrypt.BCrypt;
import gh.filesharing.db.UserDAO;
import gh.filesharing.models.User;

public class AuthManager {
    public static String validateCredentials(String username, String password) {
        UserDAO userDAO = new UserDAO();

        User foundUser = userDAO.findByUsername(username);
        if (foundUser != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), foundUser.getPassword());
            if (result.verified) return JwtUtil.generateToken(foundUser);
        }
        return null;
    }
}
