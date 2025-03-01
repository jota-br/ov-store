package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderStatusHistory;

public class OrderStatusHistoryRepository extends RepositoryOld {

    public OrderStatusHistoryRepository(EntityManager em) {
        super(em);
    }

    public OrderStatusHistoryDTO addOrderStatusHistory(Order order) {

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory(0, order, order.getStatus(), null, 0);
        this.em.persist(orderStatusHistory);
        return orderStatusHistory.transformToDto();
    }
}
