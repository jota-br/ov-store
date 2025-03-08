package ostro.veda.repository.interfaces;

import ostro.veda.model.dto.UserDto;
import ostro.veda.util.exception.InputException;
import ostro.veda.repository.dao.Address;
import ostro.veda.repository.dao.User;

import java.util.List;

public interface UserRepository extends Repository<UserDto> {

    User buildUser(UserDto userDTO) throws InputException.InvalidInputException;

    List<Address> buildAddress(UserDto userDTO);
}
