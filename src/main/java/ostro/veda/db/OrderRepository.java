package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ostro.veda.common.dto.*;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;
import ostro.veda.loggerService.Logger;

import java.time.LocalDateTime;
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
            Map<Product, Integer> productDaoAndQuantity = getProductDaoAndQuantity(productAndQuantity);
            List<OrderDetailDTO> orderDetailDTOList = orderDetailRepository.addOrderDetail(productDaoAndQuantity, order, OrderDetailRepository.Calculation.SUBTRACTION);
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
        return new Order(userId, totalAmount, status, shipping, billing);
    }

    /**
     * Called when an Order has it's Status updated (e.g. PENDING -> IN_TRANSIT)
     * Used to create an Order Status History of the Order
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
            orderDetailRepository.addOrderDetail(productDaoAndQuantity, order, OrderDetailRepository.Calculation.SUM);

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

    public OrderDTO returnItem(int orderId, Map<ProductDTO, Integer> productAndQuantity)
            throws UnsupportedOperationException, ErrorHandling.InvalidInputException {
        Order order = getOrder(orderId);
        if (order == null) return null;

        if (!isReturnAvailable(order.getUpdatedAt(), order.getStatus())) return null;
        if (!orderHasValidProductAndQuantity(order.getOrderDetails(), productAndQuantity)) return null;

        String status = OrderStatus.RETURN_REQUESTED.getStatus();
        order.updateOrderStatus(status);
        EntityTransaction transaction = null;
        try {
            transaction = this.getEm().getTransaction();
            transaction.begin();

            this.getEm().persist(order);
            OrderStatusHistoryDTO orderStatusHistory = orderStatusHistoryRepository.addOrderStatusHistory(order, status);
            Map<Product, Integer> productDaoAndQuantity = getProductDaoAndQuantity(order.getOrderDetails());
            orderDetailRepository.addOrderDetail(productDaoAndQuantity, order, OrderDetailRepository.Calculation.SUM);

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
            throw new ErrorHandling.InvalidInputException(ErrorHandling.InputExceptionMessage.EX_INVALID_ORDER_RETURN_DS,
                    orderDate + ", status:" + status
            );
        }
        return true;
    }

    /**
     * Validates the Product and Quantity requested for cancellation/returning
     * @param orderDetailList List of products in the Order to be cancelled/returned
     * @param productAndQuantity Map with product and quantity for the requested cancellation/return
     * @return true if success
     * @throws ErrorHandling.InvalidInputException if product was not found in the order or quantity
     * was higher than the order quantity
     */
    private boolean orderHasValidProductAndQuantity(List<OrderDetail> orderDetailList, Map<ProductDTO, Integer> productAndQuantity)
            throws ErrorHandling.InvalidInputException {
        int matchingRequired = productAndQuantity.size();
        for (OrderDetail orderDetail : orderDetailList) {
            Product product = orderDetail.getProduct();
            for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
                int productDtoId = entry.getKey().getProductId();
                int quantity = entry.getValue();
                if (
                        product.getProductId() == productDtoId &&
                                orderDetail.getQuantity() >= quantity &&
                                quantity > 0
                ) {
                    matchingRequired--;
                    break;
                }
            }
        }
        if (matchingRequired == 0) return true;
        throw new ErrorHandling.InvalidInputException(ErrorHandling.InputExceptionMessage.EX_INVALID_ORDER_RETURN, "");
    }

    /**
     * this method will populate the Map<Product, Integer>
     * using the OrderDetail list from the Order
     * it doesn't require stock checks, this is used for order cancellation or returning items
     *
     * @param orderDetailList contains products and quantities from the returning/canceling order
     * @return return DAO, Integer map
     */
    private Map<Product, Integer> getProductDaoAndQuantity(List<OrderDetail> orderDetailList) {
        Map<Product, Integer> productDaoAndQuantity = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            productDaoAndQuantity.put(orderDetail.getProduct(), orderDetail.getQuantity());
        }
        return productDaoAndQuantity;
    }

    /**
     * Exclusive use for addOrder, this method will populate the Map<Product, Integer>
     * if product is found != null and if stock > requested quantity
     *
     * @param productAndQuantity Product, Integer map
     * @return returns DAO Product and quantity to persist order and update products
     * @throws ErrorHandling.InvalidInputException if product is null or stock is no available
     */
    private Map<Product, Integer> getProductDaoAndQuantity(Map<ProductDTO, Integer> productAndQuantity)
            throws ErrorHandling.InvalidInputException {
        Map<Product, Integer> productDaoAndQuantity = new HashMap<>();
        for (Map.Entry<ProductDTO, Integer> entry : productAndQuantity.entrySet()) {
            Product product = this.em.find(Product.class, entry.getKey().getProductId());
            if (product == null) continue;
            int quantity = entry.getValue();
            if (product.getStock() < quantity) throw new ErrorHandling.InvalidInputException(
                    ErrorHandling.InputExceptionMessage.EX_INVALID_PRODUCT_QUANTITY,
                    "quantity:" + quantity + ", stock:" + product.getStock()
            );
            productDaoAndQuantity.put(product, quantity);
        }
        return productDaoAndQuantity;
    }

    /**
     * Gets the Order DAO entity
     *
     * @param orderId Validated at OrderService
     * @return returns Order DAO to be persisted
     */
    private Order getOrder(int orderId) {
        List<OrderDetail> orderDetail = this.getEntityManagerHelper().findByFieldId(this.getEm(), OrderDetail.class, Map.of("order.orderId", orderId));
        Order order = this.getEm().find(Order.class, orderId);
        order.getOrderDetails().addAll(orderDetail);
        return order;
    }
}
