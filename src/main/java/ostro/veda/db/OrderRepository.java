package ostro.veda.db;

import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Order;

import java.util.Map;

public class OrderRepository extends Repository {
    public OrderRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public OrderDTO addOrder(int userId, double totalAmount, OrderStatus status, Address shippingAddress,
                             Address billingAddress, Map<ProductDTO, Integer> productAndQuantity) {

        Order order = new Order(userId, totalAmount, status, shippingAddress, billingAddress);

        boolean isInserted = this.entityManagerHelper.executePersist(this.em, order);
        if (!isInserted) {
            return null;
        }

        return order.transformToDto();
    }
}
