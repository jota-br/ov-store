package main.java.ostro.veda.db;

import main.java.ostro.veda.common.dto.UserDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.db.jpa.Address;
import main.java.ostro.veda.db.jpa.User;

import java.util.List;

public interface UserRepository extends Repository<UserDTO> {

    User buildUser(UserDTO userDTO) throws ErrorHandling.InvalidInputException;

    List<Address> buildAddress(UserDTO userDTO);
}
