package ostro.veda.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;
import ostro.veda.util.validation.annotation.ValidDescription;
import ostro.veda.util.validation.annotation.ValidName;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class PermissionDto implements Dto {

    private final int permissionId;

    @NotBlank(message = "Name cannot be null")
    @ValidName
    private final String name;

    @ValidDescription
    private final String description;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"permissionId\":" + permissionId)
                .add("\"name\":\"" + name + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"createdAt\":\"" + createdAt + "\"")
                .add("\"updatedAt\":\"" + updatedAt + "\"")
                .toString();
    }
}
