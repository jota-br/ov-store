package ostro.veda.db.helpers.database;

public enum AddressColumns {

    STREET_ADDRESS("streetAddress"),
    ADDRESS_NUMBER("addressNumber"),
    ADDRESS_TYPE("addressType"),
    CITY("city"),
    STATE("state"),
    ZIPCODE("zipCode"),
    COUNTRY("country");

    private final String columnName;

    AddressColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
