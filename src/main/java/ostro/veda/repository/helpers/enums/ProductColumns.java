package ostro.veda.repository.helpers.enums;

public enum ProductColumns {

    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    STOCK("stock"),
    CATEGORIES("categories");

    private final String columnName;

    ProductColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
