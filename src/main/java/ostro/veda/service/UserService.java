package ostro.veda.service;


import ostro.veda.common.dto.UserDTO;

public interface UserService extends Service<UserDTO> {

    UserDTO add(UserDTO userDTO, String password);

    UserDTO update(UserDTO userDTO, String password);
}
