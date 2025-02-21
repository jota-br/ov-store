package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderStatusHistory;

public class OrderStatusHistoryRepository extends Repository {

    public OrderStatusHistoryRepository(EntityManager em) {
        super(em);
    }

    /**
     * this method is exclusively called by OrderService
     * when using this method, it needs to have the same EntityManager as the OrderRepository,
     * for single transaction and data integrity.
     * @param order is validated at OrderService. Order to create the status history trace.
     * @param status is validated at OrderService. OrderStatus enum String value.
     * @return returns OrderStatusHistoryDTO entity to be added to the Order statusHistory List.
     */
    public OrderStatusHistoryDTO addOrder(Order order, String status) {

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory(order, status);
        this.em.persist(orderStatusHistory);

        return orderStatusHistory.transformToDto();
    }
}
