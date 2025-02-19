package ostro.veda.db;

import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderStatusHistory;

public class OrderStatusHistoryRepository extends Repository {

    public OrderStatusHistoryRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public OrderStatusHistoryDTO addOrderStatusHistory(int orderId, String status) {

        Order order = this.getEm().find(Order.class, orderId);
        if (order == null) {
            return null;
        }

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory(order, status);
        boolean isInserted = entityManagerHelper.executePersist(this.getEm(), orderStatusHistory);

        if (!isInserted) {
            return null;
        }

        return orderStatusHistory.transformToDto();
    }
}
