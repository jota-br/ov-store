package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderStatusHistory;

public class OrderStatusHistoryRepository extends Repository {

    public OrderStatusHistoryRepository(EntityManager em) {
        super(em);
    }

    public OrderStatusHistoryDTO addOrderStatusHistory(Order order, String status) {

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory(order, status);
        this.em.persist(orderStatusHistory);

        return orderStatusHistory.transformToDto();
    }
}
