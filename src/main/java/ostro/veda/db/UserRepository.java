package ostro.veda.db;

import org.hibernate.Session;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.helpers.SessionDml;
import ostro.veda.db.jpa.User;

import java.util.List;
import java.util.Map;

public class UserRepository {

    public static UserDTO addUser(String username, String salt, String hash, String email,
                                  String firstName, String lastName, String phone) {

        Session session = DbConnection.getOpenSession();
        List<User> result = SessionDml.findByFields(session, User.class, Map.of("username", username));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        User user = new User(username, salt, hash, email, firstName, lastName, phone);
        boolean isInserted = SessionDml.executePersist(session, user);
        if (!isInserted) {
            return null;
        }

        UserDTO dto = user.transformToDto();
        DbConnection.closeSession(session);

        return dto;
    }
}
