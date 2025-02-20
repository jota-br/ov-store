package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderRepository;
import ostro.veda.db.jpa.Address;
import ostro.veda.loggerService.Logger;

import java.util.Map;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO processData(int userId, double totalAmount, String status, Address shippingAddress,
                                                           Address billingAddress, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType) {

        try {
            if (!hasValidInput(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity, processDataType)) {
                return null;
            }
            return performDmlAction(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity, processDataType);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private boolean hasValidInput(int userId, double totalAmount, String status, Address shippingAddress,
                                  Address billingAddress, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType)
            throws ErrorHandling.InvalidValueException, ErrorHandling.InvalidAddressException, ErrorHandling.InvalidQuantityException,
            ErrorHandling.InvalidProductException, ErrorHandling.InvalidOrderStatusException {
        return InputValidator.hasValidValue(totalAmount) &&
                InputValidator.hasValidOrderStatus(status) &&
                InputValidator.hasValidAddress(userId, billingAddress) &&
                InputValidator.hasValidAddress(userId, shippingAddress) &&
                InputValidator.hasValidProductAndQuantity(productAndQuantity) &&
                processDataType != null;
    }

    private OrderDTO performDmlAction(int userId, double totalAmount, String status, Address shippingAddress,
                                      Address billingAddress, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType) {
        if (processDataType.equals(ProcessDataType.ADD)) {
            return orderRepository.addOrder(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity);
        }
        return null;
    }

    private OrderDTO updateOrderStatus(int orderId, String newStatus) throws ErrorHandling.InvalidOrderStatusException {
        if (!hasValidInput(orderId, newStatus)) return null;
        return orderRepository.updateOrderStatus(orderId, newStatus);
    }

    private boolean hasValidInput(int orderId, String status) throws ErrorHandling.InvalidOrderStatusException {
        return InputValidator.hasValidOrderStatus(status) && orderId > 0;
    }
}
