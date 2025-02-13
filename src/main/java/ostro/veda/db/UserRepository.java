package ostro.veda.db;

import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.jpa.Role;
import ostro.veda.db.jpa.User;

import java.util.List;
import java.util.Map;

public class UserRepository extends Repository {


    public UserRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public UserDTO addUser(String username, String salt, String hash, String email,
                           String firstName, String lastName, String phone, boolean isActive) {

        List<User> result = this.entityManagerHelper.findByFields(this.em, User.class, Map.of("username", username));
        if (result != null && !result.isEmpty()) {
            return null;
        }

        Role role = this.em.find(Role.class, 20); // default user role (Guest/User) ID 20
        User user = new User(username, salt, hash, email, firstName, lastName, phone, isActive, role);
        boolean isInserted = this.entityManagerHelper.executePersist(this.em, user);
        if (!isInserted) {
            return null;
        }

        return user.transformToDto();
    }

    public UserDTO updateUser(int userId, String username, String salt, String hash, String email,
                              String firstName, String lastName, String phone, boolean isActive) {

        User user = this.em.find(User.class, userId);

        if (user == null) {
            return addUser(username, salt, hash, email, firstName, lastName, phone, isActive);
        }

        user.updateUser(new User(username, salt, hash, email, firstName, lastName, phone, isActive, user.getRole()));

        boolean isInserted = this.entityManagerHelper.executeMerge(this.em, user);
        if (!isInserted) {
            return null;
        }

        return user.transformToDto();
    }
}
