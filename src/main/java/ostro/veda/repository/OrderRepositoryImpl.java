package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.repository.interfaces.OrderRepository;
import ostro.veda.model.dto.OrderDto;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderStatusHistoryDto;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.enums.DiscountType;
import ostro.veda.util.enums.OrderStatus;
import ostro.veda.repository.helpers.EntityManagerHelper;
import ostro.veda.repository.dao.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public OrderRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    enum OrderOperation {
        INCREASE,
        DECREASE;
    }

    @Override
    @Transactional
    public OrderDto add(@NonNull OrderDto orderDTO) {

        log.info("add() new Order = {}", orderDTO.getOrderId());
        Order order = buildOrder(orderDTO);
        if (order == null) return null;

        try {

            this.entityManager.persist(order);
            return order.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public OrderDto update(@NonNull OrderDto orderDTO) {

        log.info("update() Order = {}", orderDTO.getOrderId());
        Order order = this.entityManager.find(Order.class, orderDTO.getOrderId());
        if (order == null) return null;
        order.updateOrderStatus(orderDTO.getStatus());
        order = buildNewOrderStatusHistory(order);
        if (order == null) return null;

        try {

            this.entityManager.persist(order);
            return order.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(int orderId) {

        log.info("cancelOrder() Order = {}", orderId);
        Order order = this.entityManager.find(Order.class, orderId);
        if (order == null) return null;

        if (!isCancellationAvailable(order.getStatus())) return null;

        List<OrderDetail> orderDetailList = order.getOrderDetails();

        List<OrderDetail> newOrderDetails = updateProductInventory(
                orderDetailList.stream().map(
                        OrderDetail::transformToDto).toList(),
                OrderOperation.INCREASE
        );
        if (newOrderDetails == null || newOrderDetails.isEmpty()) return null;

        orderDetailList.addAll(newOrderDetails);
        order.setOrderDetails(orderDetailList);
        String status = OrderStatus.CANCELLED.getStatus();
        order.updateOrderStatus(status);
        order = buildNewOrderStatusHistory(order);

        try {

            for (OrderDetail orderDetail : newOrderDetails) {
                Product product = this.entityManager.find(Product.class, orderDetail.getProduct().getProductId());
                orderDetail
                        .setOrder(order)
                        .setProduct(product);
            }
            this.entityManager.merge(order);
            return order.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public OrderDto returnItem(@NonNull OrderDetailDto orderDetailDTO) {

        log.info("returnItem() Product and Quantity for Order = [{}, {}, {}]", orderDetailDTO.getProduct().getProductId(),
                orderDetailDTO.getQuantity(), orderDetailDTO.getOrder().getOrderId());
        Order order = this.entityManager.find(Order.class, orderDetailDTO.getOrder().getOrderId());
        if (order == null) return null;

        try {
            if (!isReturnAvailable(order.getUpdatedAt(), order.getStatus())) return null;
            if (!isReturningItemsCorrect(order, orderDetailDTO)) return null;

            String status = OrderStatus.RETURN_REQUESTED.getStatus();
            order.updateOrderStatus(status);
            order = buildNewOrderStatusHistory(order);

            this.entityManager.persist(order);
            return order.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public Order buildOrder(@NonNull OrderDto orderDTO) {

        log.info("buildOrder() Order = {}", orderDTO.getOrderId());
        double totalAmount = 0.0;

        for (OrderDetailDto orderDetail : orderDTO.getOrderDetails()) {
            Product product = this.entityManager.find(Product.class, orderDetail.getProduct().getProductId());
            if (product == null) return null;

            int quantity = orderDetail.getQuantity();
            if (!hasStock(product.getStock(), quantity)) return null;

            double price = product.getPrice();
            totalAmount += totalAmount + (price * quantity);
        }

        Address shipping = this.entityManager.find(Address.class, orderDTO.getShippingAddress().getAddressId());
        Address billing = this.entityManager.find(Address.class, orderDTO.getBillingAddress().getAddressId());

        if (shipping == null || billing == null) return null;

        Coupon coupon = null;
        if (orderDTO.getCoupon() != null) {
            coupon = this.entityManager.find(Coupon.class, orderDTO.getCoupon().getCouponId());
            if (coupon == null) {
                return null;
            }

            String discountType = coupon.getDiscountType();
            if (DiscountType.AMOUNT.getDiscountType().equals(discountType)) {
                totalAmount -= coupon.getDiscountValue();
            } else if (DiscountType.PERCENTAGE.getDiscountType().equals(discountType)) {
                totalAmount *= (100 - coupon.getDiscountValue()) / 100;
            }
        }

        Order order = Order
                .builder()
                .orderId(orderDTO.getOrderId())
                .userId(orderDTO.getUserId())
                .totalAmount(totalAmount)
                .status(orderDTO.getStatus())
                .shippingAddress(shipping)
                .billingAddress(billing)
                .updatedAt(orderDTO.getUpdatedAt())
                .coupon(coupon)
                .build();

        List<OrderDetail> orderDetails = buildOrderDetails(order, orderDTO.getOrderDetails());
        List<OrderStatusHistory> orderStatusHistoryList = buildOrderStatusHistories(order, orderDTO.getOrderStatusHistory());
        if (orderDetails == null || orderStatusHistoryList == null) return null;
        order
                .setOrderDetails(orderDetails)
                .setOrderStatusHistory(orderStatusHistoryList);

        order = buildNewOrderStatusHistory(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setOrder(order);
        }

        for (OrderStatusHistory orderStatusHistory : order.getOrderStatusHistory()) {
            orderStatusHistory.setOrder(order);
        }

        return order;
    }

    @Override
    public Order buildNewOrderStatusHistory(@NonNull Order order) {

        log.info("buildNewOrderStatusHistory() Order = {}", order.getOrderId());
        OrderStatusHistory orderStatusHistory = OrderStatusHistory
                .builder()
                .order(order)
                .status(order.getStatus())
                .build();
        order.getOrderStatusHistory().add(orderStatusHistory);
        return order;
    }

    @Override
    public List<OrderDetail> buildOrderDetails(@NonNull Order order, List<OrderDetailDto> orderDetailDtos) {

        log.info("buildOrderDetails() OrderDetail list size = {}", orderDetailDtos.size());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderDetailDto orderDetailDTO : orderDetailDtos) {

            OrderDetail orderDetail = buildOrderDetail(order, orderDetailDTO);
            if (orderDetail == null) return null;
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }

    @Override
    public OrderDetail buildOrderDetail(@NonNull Order order, @NonNull OrderDetailDto orderDetailDTO) {

        log.info("buildOrderDetail() Order = {}", order.getOrderId());
        Product product = this.entityManager.find(Product.class, orderDetailDTO.getProduct().getProductId());
        if (product == null) return null;

        int quantity = orderDetailDTO.getQuantity();
        double price = product.getPrice();

        updateProductInventory(List.of(orderDetailDTO), OrderOperation.DECREASE);

        return OrderDetail
                .builder()
                .orderDetailId(orderDetailDTO.getOrderDetailId())
                .product(product)
                .quantity(quantity)
                .unitPrice(price)
                .order(order)
                .build();
    }

    @Override
    public List<OrderStatusHistory> buildOrderStatusHistories(@NonNull Order order, List<OrderStatusHistoryDto> orderStatusHistoryDtos) {

        log.info("buildOrderStatusHistories() Order = {}", order.getOrderId());
        List<OrderStatusHistory> orderStatusHistoryList = new ArrayList<>();

        if (orderStatusHistoryDtos == null) return orderStatusHistoryList;
        try {
            for (OrderStatusHistoryDto orderStatusHistoryDTO : orderStatusHistoryDtos) {
                OrderStatusHistory orderStatusHistory = buildOrderStatusHistory(order, orderStatusHistoryDTO);
                orderStatusHistoryList.add(orderStatusHistory);
            }
            return orderStatusHistoryList;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public OrderStatusHistory buildOrderStatusHistory(@NonNull Order order, @NonNull OrderStatusHistoryDto orderStatusHistoryDTO) {

        log.info("buildOrderStatusHistory() Order = {}", order.getOrderId());
        return OrderStatusHistory
                .builder()
                .orderStatusHistoryId(orderStatusHistoryDTO.getOrderStatusHistoryId())
                .order(order)
                .status(orderStatusHistoryDTO.getStatus())
                .changedAt(orderStatusHistoryDTO.getChangedAt())
                .build();
    }

    private List<OrderDetail> updateProductInventory(@NonNull List<OrderDetailDto> orderDetailList, @NonNull OrderOperation orderOperation) {

        log.info("updateProductInventory() OrderDetail list size = {}", orderDetailList.size());
        List<OrderDetail> newOrderDetailList = new ArrayList<>();
        for (OrderDetailDto orderDetail : orderDetailList) {

            Product product = this.entityManager.find(Product.class, orderDetail.getProduct().getProductId());
            if (product == null) return null;

            int newStock = product.getStock();
            int quantity = orderDetail.getQuantity();


            if (orderOperation.equals(OrderOperation.INCREASE)) {
                newStock = newStock + quantity;
                quantity = -quantity;
            } else {
                if (!hasStock(newStock, quantity)) return null;
                newStock = newStock - quantity;
            }

            OrderDetail newOrderDetail = OrderDetail
                    .builder()
//                    .orderDetailId(orderDetail.getOrderDetailId())
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
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
            throws InputException.InvalidInputException {

        log.info("isReturnAvailable() Order Date and Status = [{}, {}]", orderDate, status);
        final int MAXIMUM_AMOUNT_OF_DAY_TO_RETURN = 30;
        LocalDateTime elapsedTime = LocalDateTime.now().minusDays(MAXIMUM_AMOUNT_OF_DAY_TO_RETURN);
        if (
                elapsedTime.isAfter(orderDate) ||
                        !OrderStatus.DELIVERED.getStatus().equals(status)
        ) {
            throw new InputException.InvalidInputException("Return is no longer available",
                    orderDate + ", status:" + status
            );
        }
        return true;
    }

    private boolean isReturningItemsCorrect(@NonNull Order order, @NonNull OrderDetailDto orderDetail) {

        log.info("isReturningItemsCorrect() Order and Product = [{}, {}]", order.getOrderId(), orderDetail.getOrderDetailId());
        int orderId = order.getOrderId();
        int orderDetailId = orderDetail.getOrder().getOrderId();
        if (orderId != orderDetailId) return false;

        for (OrderDetail od : order.getOrderDetails()) {
            int productId = od.getProduct().getProductId();
            int quantity = od.getQuantity();

            if (productId == orderDetail.getProduct().getProductId() && quantity >= orderDetail.getQuantity())
                return true;
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
