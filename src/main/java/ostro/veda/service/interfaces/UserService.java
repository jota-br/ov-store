package ostro.veda.service.interfaces;


import jakarta.validation.constraints.NotBlank;
import ostro.veda.model.dto.UserDto;
import ostro.veda.util.validation.annotation.ValidPassword;

public interface UserService extends Service<UserDto> {

    UserDto add(UserDto userDTO, @NotBlank @ValidPassword String password);

    UserDto update(UserDto userDTO, @NotBlank @ValidPassword String password);
}
