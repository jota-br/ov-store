package ostro.veda.db.helpers.enums;

public enum ProductImageColumns {

    IMAGE_URL("imageUrl");

    private final String columnName;

    ProductImageColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
