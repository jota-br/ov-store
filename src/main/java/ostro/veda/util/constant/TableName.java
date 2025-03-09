package ostro.veda.util.constant;

import java.util.HashSet;
import java.util.List;

public class TableName {

    public static final String USER = "users";
    public static final String ADDRESS = "addresses";
    public static final String AUDIT = "audits";
    public static final String CATEGORY = "categories";
    public static final String ORDER = "orders";
    public static final String ORDER_DETAIL = "order_details";
    public static final String ORDER_STATUS_HISTORY = "order_status_history";
    public static final String PERMISSION = "permissions";
    public static final String PRODUCT = "products";
    public static final String PRODUCT_IMAGE = "product_images";
    public static final String ROLE = "roles";
    public static final String COUPON = "coupons";

    public static HashSet<String> tableNameList = new HashSet<>(List.of(
            USER, ADDRESS, AUDIT, CATEGORY, ORDER, ORDER_DETAIL, ORDER_STATUS_HISTORY, PERMISSION, PRODUCT, PRODUCT_IMAGE, ROLE, COUPON
    ));
}
