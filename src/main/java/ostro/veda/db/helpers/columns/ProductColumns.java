package ostro.veda.db.helpers.columns;

public enum ProductColumns {

    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    STOCK("stock");

    private final String columnName;

    ProductColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
