package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableNames;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableNames.USER)
public class UserDto implements Dto {

    private final int userId;
    private final String username;
    private final String salt;
    private final String hash;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final boolean isActive;
    private final RoleDto role;
    private final List<AddressDto> addresses;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"userId\":" + userId)
                .add("\"username\":\"" + username + "\"")
                .add("\"salt\":\"" + salt + "\"")
                .add("\"hash\":\"" + hash + "\"")
                .add("\"email\":\"" + email + "\"")
                .add("\"firstName\":\"" + firstName + "\"")
                .add("\"lastName\":\"" + lastName + "\"")
                .add("\"phone\":\"" + phone + "\"")
                .add("\"isActive\":" + isActive)
                .add("\"role\":" + role)
                .add("\"addresses\":" + addresses)
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
