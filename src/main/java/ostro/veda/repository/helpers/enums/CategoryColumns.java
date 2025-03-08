package ostro.veda.repository.helpers.enums;

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
