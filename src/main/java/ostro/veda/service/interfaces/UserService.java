package ostro.veda.service.interfaces;


import ostro.veda.model.dto.UserDto;

public interface UserService extends Service<UserDto> {

    UserDto add(UserDto userDTO, String password);

    UserDto update(UserDto userDTO, String password);
}
