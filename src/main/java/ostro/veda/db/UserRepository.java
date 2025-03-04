package ostro.veda.db;

import ostro.veda.common.dto.UserDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.User;

import java.util.List;

public interface UserRepository extends Repository<UserDTO> {

    User buildUser(UserDTO userDTO) throws ErrorHandling.InvalidInputException;

    List<Address> buildAddress(UserDTO userDTO);
}
