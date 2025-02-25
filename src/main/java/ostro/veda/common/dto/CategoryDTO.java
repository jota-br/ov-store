package ostro.veda.common.dto;

import java.time.LocalDateTime;

public class CategoryDTO {

    private final int categoryId;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /**
     *
     * @param categoryId int
     * @param name String
     * @param description String
     * @param isActive boolean
     * @param createdAt LocalDateTime
     * @param updatedAt LocalDateTime
     */
    public CategoryDTO(int categoryId, String name, String description, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // WHEN DTO HAS AN VALID ID

    /**
     *
     * @param categoryId int
     * @param name String
     * @param description String
     * @param isActive boolean
     */
    public CategoryDTO(int categoryId, String name, String description, boolean isActive) {
        this(categoryId, name, description, isActive, null, null);
    }

    // WHEN DTO HAS NO ID
    // USUALLY FOR NEW ENTITY

    /**
     *
     * @param name String
     * @param description String
     * @param isActive boolean
     */
    public CategoryDTO(String name, String description, boolean isActive) {
        this(-1, name, description, isActive, null, null);
    }


    // GETTERS
    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
