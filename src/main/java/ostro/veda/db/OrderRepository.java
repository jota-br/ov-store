package ostro.veda.db;

import ostro.veda.common.dto.OrderDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Order;

public class OrderRepository extends Repository {
    public OrderRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public OrderDTO addOrder(int userId, double totalAmount, String status, Address shippingAddress,
                             Address billingAddress) {

        Order order = getNewOrder(userId, totalAmount, status, shippingAddress, billingAddress);

        boolean isInserted = this.entityManagerHelper.executePersist(this.em, order);
        if (!isInserted) {
            return null;
        }

        return order.transformToDto();
    }

    private static Order getNewOrder(int userId, double totalAmount, String status, Address shippingAddress, Address billingAddress) {
        return new Order(userId, totalAmount, status, shippingAddress, billingAddress, null);
    }

    public boolean cancelOrder(OrderDTO order) {
        Order o = getOrder(order);
        updateOrder(o);
        return this.entityManagerHelper.executeMerge(this.em, o);
    }

    private Order getOrder(OrderDTO order) {
        return this.getEm().find(Order.class, order.getOrderId());
    }

    private void updateOrder(Order o) {
        o.updateOrder(OrderStatus.CANCELLED.getStatus());
    }
}
