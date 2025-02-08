package ostro.veda;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ostro.veda.dto.UserDTO;
import ostro.veda.helpers.SessionDml;
import ostro.veda.jpa.User;

public class UserRepository {

    public static UserDTO addUser(String username, String salt, String hash, String email,
                                  String firstName, String lastName, String phone) {

        Session session = DbConnection.getOpenSession();
        User user = SessionDml.findByUsername(session, User.class, "username", username);
        if (user != null) {
            return null;
        }

        user = new User(username, salt, hash, email, firstName, lastName, phone, false);
        boolean isInserted = executePersist(session, user);
        if (!isInserted) {
            return null;
        }

        UserDTO dto = user.transformToDto();
        DbConnection.closeSession(session);

        return dto;
    }

    private static boolean executePersist(Session session, User user) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DbConnection.transactionRollBack(transaction);
        }
        return false;
    }
}
