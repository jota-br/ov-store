package ostro.veda.db.helpers.columns;

public enum UserColumns {

    USERNAME("username");

    private final String columnName;

    UserColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
