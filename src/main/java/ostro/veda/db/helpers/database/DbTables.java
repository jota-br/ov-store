package ostro.veda.db.helpers.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbTables {

    ADDRESS("addresses"),
    AUDIT("audits"),
    CATEGORY("categories"),
    ORDER("orders"),
    ORDER_DETAIL("order_details"),
    ORDER_STATUS_HISTORY("order_status_history"),
    PERMISSIONS("permissions"),
    PRODUCT("products"),
    PRODUCT_IMAGE("product_images"),
    ROLES("roles"),
    USER("users");

    private final String tableName;
}
