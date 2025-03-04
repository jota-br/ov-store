package main.java.ostro.veda.db.helpers.database;

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
