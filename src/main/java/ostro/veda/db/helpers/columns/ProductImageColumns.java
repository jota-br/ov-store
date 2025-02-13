package ostro.veda.db.helpers.columns;

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
