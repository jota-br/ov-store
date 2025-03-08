package ostro.veda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ostro.veda.model.dto.interfaces.Dto;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class PermissionDto implements Dto {

    private final int permissionId;
    private final String name;
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
