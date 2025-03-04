package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class UserDTO {

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
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"userId\":" + userId)
                .add("\"username\":\"" + username + "\"")
                .add("\"salt\":\"*\"")
                .add("\"hash\":\"*\"")
                .add("\"email\":\"" + email + "\"")
                .add("\"firstName\":\"" + firstName + "\"")
                .add("\"lastName\":\"" + lastName + "\"")
                .add("\"phone\":\"" + phone + "\"")
                .add("\"isActive\":" + isActive)
                .add("\"role\":" + role)
                .add("\"addresses\":" + addresses)
                .add("\"createdAt\":" + createdAt)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }
}
