package ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class CategoryDTO {

    private final int categoryId;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int version;

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"categoryId\":" + categoryId)
                .add("\"name\":\"" + name + "\"")
                .add("\"description\":\"" + description + "\"")
                .add("\"isActive\":" + isActive)
                .add("\"createdAt\":" + createdAt)
                .add("\"updatedAt\":" + updatedAt)
                .toString();
    }
}
