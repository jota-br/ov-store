package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserWithPasswordDTO {

    private final int userId;
    private final String username;
    private final String salt;
    private final String hash;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final boolean isActive;
    private final RoleDTO role;
    private final List<AddressDTO> addresses;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String password;
    private final int version;
}
