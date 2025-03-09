package ostro.veda.util.enums;

import lombok.Getter;

@Getter
public enum AddressType {

    HOME("HOME"),
    WORK("WORK"),
    BILLING("BILLING"),
    SHIPPING("SHIPPING"),
    DELIVERY("DELIVERY"),
    PICKUP("PICKUP"),
    OFFICE("OFFICE"),
    STORE("STORE"),
    PO_BOX("PO BOX"),
    WAREHOUSE("WAREHOUSE"),
    OTHER("OTHER");

    private final String type;

    AddressType(String type) {
        this.type = type;
    }
}
