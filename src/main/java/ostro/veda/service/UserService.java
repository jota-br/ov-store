package ostro.veda.service;

import ostro.veda.common.dto.UserDTO;

public interface UserService {

    UserDTO add(UserDTO userDTO, String password);

    UserDTO update(UserDTO userDTO, String password);
}
