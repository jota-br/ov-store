package ostro.veda.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.annotation.Auditable;
import ostro.veda.util.constant.TableName;
import ostro.veda.util.validation.annotation.ValidEmail;
import ostro.veda.util.validation.annotation.ValidPersonName;
import ostro.veda.util.validation.annotation.ValidPhone;
import ostro.veda.util.validation.annotation.ValidUsername;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
@Auditable(tableName = TableName.USER)
public class UserDto implements Dto {

    private final int userId;

    @NotBlank(message = "Username cannot be blank")
    @ValidUsername
    private final String username;

    private final String salt;
    private final String hash;

    @NotBlank(message = "Email cannot be blank")
    @ValidEmail
    private final String email;

    @ValidPersonName
    private final String firstName;

    @ValidPersonName
    private final String lastName;

    @ValidPhone
    private final String phone;

    private final boolean isActive;
    private final RoleDto role;

    @Valid
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
