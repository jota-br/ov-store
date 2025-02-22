package ostro.veda.db.helpers;

public enum OrderStatus {
    PENDING_PAYMENT("PENDING PAYMENT"),
    PROCESSING("PROCESSING"),
    ON_HOLD("ON HOLD"),
    IN_TRANSIT("IN TRANSIT"),
    OUT_FOR_DELIVERY("OUT FOR DELIVERY"),
    DELIVERED("DELIVERED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
    REFUNDED("REFUNDED"),
    FAILED("FAILED"),
    DRAFT("DRAFT");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static int getOrdinal(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equals(status)) {
                return orderStatus.ordinal();
            }
        }
        return -1;
    }
}
