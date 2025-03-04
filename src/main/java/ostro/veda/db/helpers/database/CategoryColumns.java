package main.java.ostro.veda.db.helpers.database;

public enum CategoryColumns {

    NAME("name");

    private final String columnName;

    CategoryColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
