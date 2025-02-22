package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ostro.veda.common.dto.*;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;
import ostro.veda.loggerService.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository extends Repository {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderRepository(EntityManager em, OrderDetailRepository orderDetailRepository, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        super(em);
        this.orderDetailRepository = orderDetailRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    /**
     * @param userId             validated at OrderService
     * @param totalAmount        |
     * @param status             |
     * @param shippingAddress    |
     * @param billingAddress     |
     * @param productAndQuantity validated at OrderService
     * @return returns the persisted OrderDTO
     */
    public OrderDTO addOrder(int userId, double totalAmount, String status, AddressDTO shippingAddress,
                             AddressDTO billingAddress, Map<ProductDTO, Integer> productAndQuantity) {

        Order order = getNewOrder(userId, totalAmount, status, shippingAddress, billingAddress);
        OrderDTO orderDTO = null;

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            this.em.persist(order);
            List<OrderDetailDTO> orderDetailDTOList = orderDetailRepository.addOrder(productAndQuantity, order);
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

    /**
     * @param userId          required field
     * @param totalAmount     required field
     * @param status          required field
     * @param shippingAddress required field
     * @param billingAddress  required field
     * @return returns Order DAO to be persisted
     */
    private Order getNewOrder(int userId, double totalAmount, String status, AddressDTO shippingAddress, AddressDTO billingAddress) {
        Address shipping = this.em.find(Address.class, shippingAddress.getAddressId());
        Address billing = this.em.find(Address.class, billingAddress.getAddressId());
        return new Order(userId, totalAmount, status, shipping, billing, null);
    }

    /**
     * Called when an Order has it's Status updated (e.g. PENDING -> IN_TRANSIT)
     * Used to create an Order Status History of the Order
     *
     * @param orderId   validated at OrderService
     * @param newStatus |
     * @return returns the persisted OrderDTO
     */
    public OrderDTO updateOrderStatus(int orderId, String newStatus) {
        Order order = getOrder(orderId);
        // If no order is found, although a valid ID (id > 0), no matching order was found
        if (order == null) return null;
        // Updates Order DAO with new Status to be persisted
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

    /**
     * @param orderId orderId to retrieved DAO entity.
     *                OrderStatus needs to be checked
     *                if ordinal value is equal or higher
     *                than 3 cancellation is no longer available
     *                delivery must be completed for return
     *                and refund to be applicable.
     * @return will return OrderDTO with updated data
     */
    public OrderDTO cancelOrder(int orderId) throws UnsupportedOperationException {
        Order order = getOrder(orderId);
        if (order == null) return null;

        if (!isCancellationAvailable(order.getStatus())) return null;

        String status = OrderStatus.CANCELLED.getStatus();
        order.updateOrderStatus(status);
        EntityTransaction transaction = null;
        try {
            transaction = this.getEm().getTransaction();
            transaction.begin();

            this.getEm().persist(order);
            OrderStatusHistoryDTO orderStatusHistory = orderStatusHistoryRepository.addOrderStatusHistory(order, status);
            Map<Product, Integer> productDaoAndQuantity = getProductDaoAndQuantity(order.getOrderDetails());
            orderDetailRepository.cancelOrder(productDaoAndQuantity, OrderDetailRepository.Calculation.SUM);

            transaction.commit();
            OrderDTO orderDTO = order.transformToDto();
            orderDTO.getOrderStatusHistory().add(orderStatusHistory);
            return orderDTO;
        } catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    /**
     * @param orderStatus order status to be checked
     * @return returns true if the order is eligible for cancellation
     * @throws UnsupportedOperationException When an order has an ordinal higher than 2
     *                                       it can't be cancelled, and needs to be returned for refund
     */
    private boolean isCancellationAvailable(String orderStatus) throws UnsupportedOperationException {
        int NO_CANCELLATION_ORDINAL = 3;
        int ordinal = OrderStatus.getOrdinal(orderStatus);
        if (ordinal >= NO_CANCELLATION_ORDINAL) {
            throw new UnsupportedOperationException("Cancellation is unavailable, check product Order Status.");
        }
        return true;
    }

    private Map<Product, Integer> getProductDaoAndQuantity(List<OrderDetail> orderDetailList) {
        Map<Product, Integer> productDaoAndQuantity = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            productDaoAndQuantity.put(orderDetail.getProduct(), orderDetail.getQuantity());
        }
        return productDaoAndQuantity;
    }

    /**
     * Gets the Order DAO entity
     * @param orderId Validated at OrderService
     * @return returns Order DAO to be persisted
     */
    private Order getOrder(int orderId) {
        return this.getEm().find(Order.class, orderId);
    }
}
