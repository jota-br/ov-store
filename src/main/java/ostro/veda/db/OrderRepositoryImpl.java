package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final EntityManager entityManager;
    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public OrderRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        this.entityManager = entityManager;
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    public OrderDTO add(@NonNull OrderDTO orderDTO) {
        log.info("add() new Order = {}", orderDTO.getOrderId());
        Order order = buildOrder(orderDTO);
        if (order == null) return null;

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            this.entityManager.persist(order);

            transaction.commit();
            orderDTO = order.transformToDto();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return orderDTO;
    }

    @Override
    public OrderDTO update(@NonNull OrderDTO orderDTO) {
        log.info("update() Order = {}", orderDTO.getOrderId());
        Order order = this.entityManager.find(Order.class, orderDTO.getOrderId());
        order.updateOrderStatus(orderDTO.getStatus());

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            this.entityManager.persist(order);

            transaction.commit();
            orderDTO = order.transformToDto();
            return orderDTO;
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    @Override
    public OrderDTO cancelOrder(int orderId) {
        log.info("cancelOrder() Order = {}", orderId);
        Order order = this.entityManager.find(Order.class, orderId);

        if (!isCancellationAvailable(order.getStatus())) return null;

        List<OrderDetail> orderDetailList = this.entityManagerHelper.findByFieldId(this.entityManager,
                OrderDetail.class, Map.of("order.orderId", orderId));

        List<OrderDetail> newOrderDetails = updateProductInventory(orderDetailList, OrderDetailRepository.OrderOperation.INCREASE);
        if (newOrderDetails == null || newOrderDetails.isEmpty()) return null;

        orderDetailList.addAll(newOrderDetails);
        order.setOrderDetails(orderDetailList);
        String status = OrderStatus.CANCELLED.getStatus();
        order.updateOrderStatus(status);
        order = buildNewOrderStatusHistory(order);

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            this.entityManager.persist(order);

            transaction.commit();
            return order.transformToDto();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    @Override
    public OrderDTO returnItem(@NonNull OrderDetailDTO orderDetailDTO) {
        log.info("returnItem() Product and Quantity for Order = [{}, {}, {}]", orderDetailDTO.getProduct().getProductId(),
                orderDetailDTO.getQuantity(), orderDetailDTO.getOrder().getOrderId());
        Order order = this.entityManager.find(Order.class, orderDetailDTO.getOrder().getOrderId());

        EntityTransaction transaction = null;
        try {
            if (!isReturnAvailable(order.getUpdatedAt(), order.getStatus())) return null;
            if (!isReturningItemsCorrect(order, orderDetailDTO)) return null;

            String status = OrderStatus.RETURN_REQUESTED.getStatus();
            order.updateOrderStatus(status);

            transaction = this.entityManager.getTransaction();
            transaction.begin();

            this.entityManager.persist(order);

            transaction.commit();
            return order.transformToDto();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    @Override
    public Order buildOrder(@NonNull OrderDTO orderDTO) {
        log.info("buildOrder() Order = {}", orderDTO.getOrderId());
        double totalAmount = 0.0;

        for (OrderDetailDTO orderDetail : orderDTO.getOrderDetails()) {
            Product product = this.entityManager.find(Product.class, orderDetail.getProduct().getProductId());

            int quantity = orderDetail.getQuantity();
            if (!hasStock(product.getStock(), quantity)) return null;

            double price = product.getPrice();
            totalAmount += totalAmount + (price * quantity);
        }

        Address shipping = this.entityManager.find(Address.class, orderDTO.getShippingAddress().getAddressId());
        Address billing = this.entityManager.find(Address.class, orderDTO.getBillingAddress().getAddressId());

        Order order = new Order()
                .setOrderId(orderDTO.getOrderId())
                .setUserId(orderDTO.getUserId())
                .setTotalAmount(totalAmount)
                .setStatus(orderDTO.getStatus())
                .setShippingAddress(shipping)
                .setBillingAddress(billing)
                .setUpdatedAt(orderDTO.getUpdatedAt());

        order
                .setOrderDetails(buildOrderDetails(order, orderDTO.getOrderDetails()))
                .setOrderStatusHistory(buildOrderStatusHistories(order, orderDTO.getOrderStatusHistory()));

        order = buildNewOrderStatusHistory(order);

        return order;
    }

    @Override
    public Order buildNewOrderStatusHistory(@NonNull Order order) {
        log.info("buildNewOrderStatusHistory() Order = {}", order.getOrderId());
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory()
                .setOrder(order)
                .setStatus(order.getStatus());
        order.getOrderStatusHistory().add(orderStatusHistory);

        return order;
    }

    @Override
    public List<OrderDetail> buildOrderDetails(@NonNull Order order, @NonNull List<OrderDetailDTO> orderDetailDTOS) {
        log.info("buildOrderDetails() OrderDetail list size = {}", orderDetailDTOS.size());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOS) {

            orderDetailList.add(buildOrderDetail(order, orderDetailDTO));
        }
        return orderDetailList;
    }

    @Override
    public OrderDetail buildOrderDetail(@NonNull Order order, @NonNull OrderDetailDTO orderDetailDTO) {
        log.info("buildOrderDetail() Order = {}", order.getOrderId());
        Product product = this.entityManager.find(Product.class, orderDetailDTO.getProduct().getProductId());
        int quantity = orderDetailDTO.getQuantity();
        double price = product.getPrice();

        updateProductInventory(order.getOrderDetails(), OrderDetailRepository.OrderOperation.DECREASE);

        return new OrderDetail()
                .setOrderDetailId(orderDetailDTO.getOrderDetailId())
                .setProduct(product)
                .setQuantity(quantity)
                .setUnitPrice(price)
                .setOrder(order);
    }

    @Override
    public List<OrderStatusHistory> buildOrderStatusHistories(@NonNull Order order, @NonNull List<OrderStatusHistoryDTO> orderStatusHistoryDTOS) {
        log.info("buildOrderStatusHistories() Order = {}", order.getOrderId());
        List<OrderStatusHistory> orderStatusHistoryList = new ArrayList<>();
        for (OrderStatusHistoryDTO orderStatusHistoryDTO : orderStatusHistoryDTOS) {
            OrderStatusHistory orderStatusHistory = buildOrderStatusHistory(order, orderStatusHistoryDTO);
            orderStatusHistoryList.add(orderStatusHistory);
        }
        return orderStatusHistoryList;
    }

    @Override
    public OrderStatusHistory buildOrderStatusHistory(@NonNull Order order, @NonNull OrderStatusHistoryDTO orderStatusHistoryDTO) {
        log.info("buildOrderStatusHistory() Order = {}", order.getOrderId());
        return new OrderStatusHistory()
                .setOrderStatusHistoryId(orderStatusHistoryDTO.getOrderStatusHistoryId())
                .setOrder(order)
                .setStatus(orderStatusHistoryDTO.getStatus())
                .setChangedAt(orderStatusHistoryDTO.getChangedAt());
    }

    private List<OrderDetail> updateProductInventory(@NonNull List<OrderDetail> orderDetailList, @NonNull OrderDetailRepository.OrderOperation orderOperation) {
        log.info("updateProductInventory() OrderDetail list size = {}", orderDetailList.size());

        List<OrderDetail> newOrderDetailList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {

            Product product = orderDetail.getProduct();
            int newStock = product.getStock();
            int quantity = orderDetail.getQuantity();


            if (orderOperation.equals(OrderDetailRepository.OrderOperation.INCREASE)) {
                newStock = newStock + quantity;
                quantity = -quantity;
            } else {
                if (!hasStock(newStock, quantity)) return null;
                newStock = newStock - quantity;
            }

            OrderDetail newOrderDetail = new OrderDetail()
                    .setOrderDetailId(orderDetail.getOrderDetailId())
                    .setOrder(orderDetail.getOrder())
                    .setProduct(product)
                    .setQuantity(quantity)
                    .setUnitPrice(product.getPrice());
            newOrderDetailList.add(newOrderDetail);
            product.updateStock(newStock);
        }
        return newOrderDetailList;
    }

    private boolean hasStock(int stock, int quantity) {
        log.info("hasStock() stock and quantity = {}, {}", stock, quantity);
        return stock >= quantity;
    }

    private boolean isCancellationAvailable(@NonNull String orderStatus) throws UnsupportedOperationException {
        log.info("isCancellationAvailable() Order Status = {}", orderStatus);
        final int NO_CANCELLATION_ORDINAL = 4;
        final int ordinal = OrderStatus.getOrdinal(orderStatus);
        if (ordinal >= NO_CANCELLATION_ORDINAL) {
            throw new UnsupportedOperationException("Cancellation is unavailable, check product Order Status.");
        }
        return true;
    }

    private boolean isReturnAvailable(@NonNull LocalDateTime orderDate, @NonNull String status)
            throws ErrorHandling.InvalidInputException {
        log.info("isReturnAvailable() Order Date and Status = [{}, {}]", orderDate, status);
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

    private boolean isReturningItemsCorrect(@NonNull Order order, @NonNull OrderDetailDTO orderDetail) {

        log.info("isReturningItemsCorrect() Order and Product = [{}, {}]", order.getOrderId(), orderDetail.getOrderDetailId());
        int orderId = order.getOrderId();
        int orderDetailId = orderDetail.getOrder().getOrderId();
        if (orderId != orderDetailId) return false;

        for (OrderDetail od : order.getOrderDetails()) {
            int productId = od.getProduct().getProductId();
            int quantity = od.getQuantity();

            if (productId == orderDetail.getProduct().getProductId() && quantity <= orderDetail.getQuantity()) return true;
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
