package ostro.veda.common.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class UserDTO {

    private final int userId;
    private final String username;

    @ToString.Exclude
    private final String salt;
    @ToString.Exclude
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

    public UserDTO(int userId, String username, String salt, String hash, String email, String firstName,
                   String lastName, String phone, boolean isActive, RoleDTO role, List<AddressDTO> addresses,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.salt = salt;
        this.hash = hash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isActive = isActive;
        this.role = role;
        this.addresses = addresses == null ? new ArrayList<>() : addresses;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
