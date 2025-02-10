package ostro.veda.service;

public enum EntityType {

    USER("User"),
    PRODUCT("Product"),
    PRODUCT_IMAGE("ProductImage"),
    CATEGORY("Category");

    private final String type;

    EntityType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
