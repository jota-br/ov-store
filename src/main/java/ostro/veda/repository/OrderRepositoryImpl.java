package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.model.dto.OrderDetailDto;
import ostro.veda.model.dto.OrderDto;
import ostro.veda.model.dto.OrderStatusHistoryDto;
import ostro.veda.repository.dao.*;
import ostro.veda.repository.interfaces.OrderRepository;
import ostro.veda.util.enums.DiscountType;
import ostro.veda.util.enums.OrderStatus;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.validation.ValidateParameter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    enum OrderOperation {
        INCREASE,
        DECREASE;
    }

    @Override
    @Transactional
    public OrderDto add(OrderDto orderDTO) {

        log.info("add() new Order = {} for User = {}", orderDTO.getOrderDate(), orderDTO.getUserId());
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
    public OrderDto update(OrderDto orderDTO) {

        log.info("update() Order = {} for User = {}", orderDTO.getOrderDate(), orderDTO.getUserId());
        Order order = this.entityManager.find(Order.class, orderDTO.getOrderId());
        if (order == null) return null;
        order.updateOrderStatus(orderDTO.getStatus());
        buildNewOrderStatusHistory(order);

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
        OrderStatus status = OrderStatus.CANCELLED;
        order.updateOrderStatus(status);
        buildNewOrderStatusHistory(order);

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
    public OrderDto returnItem(OrderDetailDto orderDetailDTO) {

        log.info("returnItem() Product and Quantity in Order = [{}, {}, {}] for User = {}", orderDetailDTO.getProduct().getProductId(),
                orderDetailDTO.getQuantity(), orderDetailDTO.getOrder().getOrderId(), orderDetailDTO.getOrder().getUserId());

        Order order = this.entityManager.find(Order.class, orderDetailDTO.getOrder().getOrderId());
        if (order == null) return null;

        try {
            if (!isReturnAvailable(order.getUpdatedAt(), order.getStatus())) return null;
            if (!isReturningItemsCorrect(order, orderDetailDTO)) return null;

            OrderStatus status = OrderStatus.RETURN_REQUESTED;
            order.updateOrderStatus(status);
            buildNewOrderStatusHistory(order);

            this.entityManager.persist(order);
            return order.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    private Order buildOrder(OrderDto orderDTO) {

        ValidateParameter.isNull(this.getClass(), orderDTO);

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

            DiscountType discountType = coupon.getDiscountType();
            if (DiscountType.AMOUNT.equals(discountType)) {
                totalAmount -= coupon.getDiscountValue();
            } else if (DiscountType.PERCENTAGE.equals(discountType)) {
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

        buildNewOrderStatusHistory(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setOrder(order);
        }

        for (OrderStatusHistory orderStatusHistory : order.getOrderStatusHistory()) {
            orderStatusHistory.setOrder(order);
        }

        return order;
    }

    private void buildNewOrderStatusHistory(Order order) {

        ValidateParameter.isNull(this.getClass(), order);

        log.info("buildNewOrderStatusHistory() Order = {}", order.getOrderId());

        OrderStatusHistory orderStatusHistory = OrderStatusHistory
                .builder()
                .order(order)
                .status(order.getStatus())
                .build();
        order.getOrderStatusHistory().add(orderStatusHistory);
    }

    private List<OrderDetail> buildOrderDetails(Order order, List<OrderDetailDto> orderDetailDtos) {

        ValidateParameter.isNull(this.getClass(), order);

        log.info("buildOrderDetails() OrderDetail list size = {}", orderDetailDtos.size());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderDetailDto orderDetailDTO : orderDetailDtos) {

            OrderDetail orderDetail = buildOrderDetail(order, orderDetailDTO);
            if (orderDetail == null) return null;
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }

    private OrderDetail buildOrderDetail(Order order, OrderDetailDto orderDetailDTO) {

        ValidateParameter.isNull(this.getClass(), order, orderDetailDTO);

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

    private List<OrderStatusHistory> buildOrderStatusHistories(Order order, List<OrderStatusHistoryDto> orderStatusHistoryDtos) {

        ValidateParameter.isNull(this.getClass(), order);

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

    private OrderStatusHistory buildOrderStatusHistory(Order order, OrderStatusHistoryDto orderStatusHistoryDTO) {

        ValidateParameter.isNull(this.getClass(), order, orderStatusHistoryDTO);

        log.info("buildOrderStatusHistory() Order = {}", order.getOrderId());

        return OrderStatusHistory
                .builder()
                .orderStatusHistoryId(orderStatusHistoryDTO.getOrderStatusHistoryId())
                .order(order)
                .status(orderStatusHistoryDTO.getStatus())
                .changedAt(orderStatusHistoryDTO.getChangedAt())
                .build();
    }

    private List<OrderDetail> updateProductInventory(List<OrderDetailDto> orderDetailList, OrderOperation orderOperation) {

        ValidateParameter.isNull(this.getClass(), orderDetailList, orderOperation);

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

    private boolean isCancellationAvailable(OrderStatus orderStatus) throws UnsupportedOperationException {

        ValidateParameter.isNull(this.getClass(), orderStatus);

        log.info("isCancellationAvailable() Order Status = {}", orderStatus);

        final int NO_CANCELLATION_ORDINAL = 4;
        final int ordinal = orderStatus.ordinal();
        if (ordinal >= NO_CANCELLATION_ORDINAL) {
            throw new UnsupportedOperationException("Cancellation is unavailable, check product Order Status.");
        }
        return true;
    }

    private boolean isReturnAvailable(LocalDateTime orderDate, OrderStatus status)
            throws InputException.InvalidInputException {

        ValidateParameter.isNull(this.getClass(), orderDate, status);

        log.info("isReturnAvailable() Order Date and Status = [{}, {}]", orderDate, status);

        final int MAXIMUM_AMOUNT_OF_DAY_TO_RETURN = 30;
        LocalDateTime elapsedTime = LocalDateTime.now().minusDays(MAXIMUM_AMOUNT_OF_DAY_TO_RETURN);
        if (
                elapsedTime.isAfter(orderDate) ||
                        !OrderStatus.DELIVERED.equals(status)
        ) {
            throw new InputException.InvalidInputException("Return is no longer available",
                    orderDate + ", status:" + status
            );
        }
        return true;
    }

    private boolean isReturningItemsCorrect(Order order, OrderDetailDto orderDetail) {

        ValidateParameter.isNull(this.getClass(), order, orderDetail);

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
