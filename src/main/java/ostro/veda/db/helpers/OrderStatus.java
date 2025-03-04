package main.java.ostro.veda.db.helpers;

public enum OrderStatus {

    DRAFT("DRAFT"),
    PENDING_PAYMENT("PENDING PAYMENT"),
    PROCESSING("PROCESSING"),
    ON_HOLD("ON HOLD"),
    FAILED("FAILED"),
    IN_TRANSIT("IN TRANSIT"),
    OUT_FOR_DELIVERY("OUT FOR DELIVERY"),
    DELIVERED("DELIVERED"),
    COMPLETED("COMPLETED"),
    RETURN_REQUESTED("RETURN HAS BEING REQUESTED"),
    RETURN_REQUEST_APPROVED("RETURN REQUEST HAS BEEN APPROVED"),
    RETURN_SHIPPED("RETURNED ITEM IS BEING SHIPPED"),
    RETURN_RECEIVED("RETURNED ITEM HAS BEING RECEIVED"),
    RETURN_IN_INSPECTION("RETURNED IS IN INSPECTION"),
    RETURN_INSPECTION_APPROVED("RETURNED ITEM INSPECTION HAS BEEN APPROVED"),
    RETURN_INSPECTION_DENIED("RETURNED ITEM INSPECTION HAS BEEN DENIED"),
    REFUND_REQUESTED("REFUND HAS BEEN REQUESTED TO THE ORIGINAL PAYMENT METHOD"),
    REFUND_COMPLETED("REFUND HAS BEEN COMPLETED, FUNDS WERE SEND TO THE CUSTOMER"),
    CANCELLED("CANCELLED");

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
