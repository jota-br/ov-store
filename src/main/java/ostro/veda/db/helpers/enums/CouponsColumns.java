package ostro.veda.db.helpers.enums;

public enum CouponsColumns {

    CODE("code");

    private final String columnName;

    CouponsColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
