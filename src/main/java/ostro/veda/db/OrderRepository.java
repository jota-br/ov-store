package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Order;
import ostro.veda.loggerService.Logger;

import java.util.List;
import java.util.Map;

public class OrderRepository extends Repository {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderRepository(EntityManager em, EntityManagerHelper entityManagerHelper, OrderDetailRepository orderDetailRepository, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        super(em, entityManagerHelper);
        this.orderDetailRepository = orderDetailRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public OrderDTO addOrder(int userId, double totalAmount, String status, Address shippingAddress,
                             Address billingAddress, Map<ProductDTO, Integer> productAndQuantity) {

        Order order = getNewOrder(userId, totalAmount, status, shippingAddress, billingAddress);
        OrderDTO orderDTO = null;

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            this.em.persist(order);
            List<OrderDetailDTO> orderDetailDTOList = orderDetailRepository.addOrderDetail(productAndQuantity, order);
            OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryRepository.addOrderStatusHistory(order, status);

            transaction.commit();
            orderDTO = order.transformToDto();
            orderDTO.getOrderDetails().addAll(orderDetailDTOList);
            orderDTO.getOrderStatusHistory().add(orderStatusHistoryDTO);
        } catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
        }

        return orderDTO;
    }

    private static Order getNewOrder(int userId, double totalAmount, String status, Address shippingAddress, Address billingAddress) {
        return new Order(userId, totalAmount, status, shippingAddress, billingAddress, null);
    }

    public OrderDTO updateOrderStatus(int orderId, String newStatus) {
        Order order = getOrder(orderId);
        if (order == null) return null;
        order.updateOrderStatus(newStatus);
        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            this.em.persist(order);
            OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryRepository.addOrderStatusHistory(order, newStatus);

            transaction.commit();
            OrderDTO orderDTO = order.transformToDto();
            orderDTO.getOrderStatusHistory().add(orderStatusHistoryDTO);
            return orderDTO;
        } catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    private Order getOrder(int orderId) {
        return this.getEm().find(Order.class, orderId);
    }
}
