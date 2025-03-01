package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.*;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderRepository extends Repository {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderRepository(EntityManager em, OrderDetailRepository orderDetailRepository, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        super(em);
        this.orderDetailRepository = orderDetailRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public OrderDTO addOrder(OrderBasic orderBasic) {

        Order order = getNewOrder(orderBasic);
        if (order == null) return null;

        OrderDTO orderDTO = null;
        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            this.em.persist(order);
            List<OrderDetail> orderDetailList = getOrderDetail(order, orderBasic);
            List<OrderDetailDTO> orderDetailDTOList = orderDetailRepository.addOrderDetail(order, orderDetailList, OrderDetailRepository.OrderOperation.DECREASE);
            OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryRepository.addOrderStatusHistory(order);

            transaction.commit();
            orderDTO = order.transformToDto();
            orderDTO.getOrderDetails().addAll(orderDetailDTOList);
            orderDTO.getOrderStatusHistory().add(orderStatusHistoryDTO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return orderDTO;
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
        order.updateOrderStatus(newStatus);

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            this.em.persist(order);
            OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryRepository.addOrderStatusHistory(order);

            transaction.commit();
            OrderDTO orderDTO = order.transformToDto();
            orderDTO.getOrderStatusHistory().add(orderStatusHistoryDTO);
            return orderDTO;
        } catch (Exception e) {
            log.warn(e.getMessage());
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
    public OrderDTO cancelOrder(int orderId) {
        Order order = this.getEm().find(Order.class, orderId);
        List<OrderDetail> orderDetailList = this.getEntityManagerHelper().findByFieldId(this.getEm(),
                        OrderDetail.class, Map.of("order.orderId", orderId));

        if (!isCancellationAvailable(order.getStatus())) return null;

        String status = OrderStatus.CANCELLED.getStatus();
        order.updateOrderStatus(status);
        EntityTransaction transaction = null;
        try {
            transaction = this.getEm().getTransaction();
            transaction.begin();

            this.getEm().persist(order);
            OrderStatusHistoryDTO orderStatusHistory = orderStatusHistoryRepository.addOrderStatusHistory(order);
            orderDetailRepository.addOrderDetail(order, orderDetailList, OrderDetailRepository.OrderOperation.INCREASE);

            transaction.commit();
            OrderDTO orderDTO = order.transformToDto();
            orderDTO.getOrderStatusHistory().add(orderStatusHistory);
            return orderDTO;
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    public OrderDTO returnItem(OrderBasic orderBasic)
            throws UnsupportedOperationException, ErrorHandling.InvalidInputException {

        Order order = getOrder(orderBasic.getOrderId());

        if (!isReturnAvailable(order.getUpdatedAt(), order.getStatus())) return null;
        if (!isReturningItemsCorrect(order, orderBasic)) return null;

        String status = OrderStatus.RETURN_REQUESTED.getStatus();
        order.updateOrderStatus(status);
        EntityTransaction transaction = null;
        try {
            transaction = this.getEm().getTransaction();
            transaction.begin();

            this.getEm().persist(order);
            OrderStatusHistoryDTO orderStatusHistory = orderStatusHistoryRepository.addOrderStatusHistory(order);
            orderDetailRepository.addOrderDetail(order, order.getOrderDetails(), OrderDetailRepository.OrderOperation.INCREASE);

            transaction.commit();
            OrderDTO orderDTO = order.transformToDto();
            orderDTO.getOrderStatusHistory().add(orderStatusHistory);
            return orderDTO;
        } catch (Exception e) {
            log.warn(e.getMessage());
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
        final int NO_CANCELLATION_ORDINAL = 4;
        final int ordinal = OrderStatus.getOrdinal(orderStatus);
        if (ordinal >= NO_CANCELLATION_ORDINAL) {
            throw new UnsupportedOperationException("Cancellation is unavailable, check product Order Status.");
        }
        return true;
    }

    private boolean isReturnAvailable(LocalDateTime orderDate, String status)
            throws ErrorHandling.InvalidInputException {
        final int MAXIMUM_AMOUNT_OF_DAY_TO_RETURN = 30;
        LocalDateTime elapsedTime = LocalDateTime.now().minusDays(MAXIMUM_AMOUNT_OF_DAY_TO_RETURN);
        if (
                elapsedTime.isAfter(orderDate) ||
                        !OrderStatus.DELIVERED.getStatus().equals(status)
        ) {
            throw new ErrorHandling.InvalidInputException("Return is no longer available",
                    orderDate + ", status:" + status
            );
        }
        return true;
    }

    private boolean isReturningItemsCorrect(Order order, OrderBasic orderBasic) {

        int orderId = order.getOrderId();
        int orderBasicId = orderBasic.getOrderId();
        if (orderId != orderBasicId) return false;

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            int productId = orderDetail.getProduct().getProductId();
            int quantity = orderDetail.getQuantity();

            for (OrderDetailBasic orderDetailBasic : orderBasic.getOrderDetails()) {
                int orderBasicProductId = orderDetailBasic.getProductId();
                int orderBasicQuantity = -(orderDetailBasic.getQuantity());
                if (productId != orderBasicProductId || quantity < orderBasicQuantity) return false;
            }
        }
        return true;
    }

    private Order getNewOrder(OrderBasic orderBasic) {

        double totalAmount = 0.0;

        for (OrderDetailBasic orderDetailBasic : orderBasic.getOrderDetails()) {
            Product product = this.getEm().find(Product.class, orderDetailBasic.getProductId());

            int quantity = orderDetailBasic.getQuantity();
            if (product.getStock() < quantity) return null;

            double price = product.getPrice();
            totalAmount += totalAmount + (price * quantity);
        }

        Address shipping = this.em.find(Address.class, orderBasic.getShippingAddressId());
        Address billing = this.em.find(Address.class, orderBasic.getBillingAddressId());

        return new Order(0, orderBasic.getUserId(), LocalDateTime.now(), totalAmount, orderBasic.getStatus(), null,
                shipping, billing, null, LocalDateTime.now(), 0);
    }

    private List<OrderDetail> getOrderDetail(Order order, OrderBasic orderBasic) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderDetailBasic orderDetailBasic : orderBasic.getOrderDetails()) {
            Product product = this.getEm().find(Product.class, orderDetailBasic.getProductId());
            int quantity = orderDetailBasic.getQuantity();
            double price = product.getPrice();
            orderDetailList.add(new OrderDetail(order, product, quantity, price));
        }
        return orderDetailList;
    }

    /**
     * Gets the Order DAO entity
     *
     * @param orderId int
     * @return Order
     */
    private Order getOrder(int orderId) {
        return this.getEm().find(Order.class, orderId);
    }
}
