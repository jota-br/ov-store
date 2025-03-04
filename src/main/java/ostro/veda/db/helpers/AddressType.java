package ostro.veda.db.helpers;

public enum AddressType {

    HOME("Home"),
    WORK("Work"),
    BILLING("Billing"),
    SHIPPING("Shipping"),
    DELIVERY("Delivery"),
    PICKUP("Pickup"),
    OFFICE("Office"),
    STORE("Store"),
    PO_BOX("PO Box"),
    WAREHOUSE("Warehouse"),
    OTHER("Other");

    private final String value;

    AddressType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
