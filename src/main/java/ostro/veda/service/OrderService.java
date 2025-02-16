package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderRepository;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Product;
import ostro.veda.loggerService.Logger;

import java.util.List;
import java.util.Map;

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
    }

    public OrderDTO processData(int userId, double totalAmount, OrderStatus status, Address shippingAddress,
                                Address billingAddress, Map<Product, Integer> productAndQuantity, ProcessDataType processDataType) {

        try {
            if (!hasValidInput(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity, processDataType))
                return null;

            OrderDTO orderDTO = performDmlAction(userId, totalAmount, status, shippingAddress, billingAddress,
                    productAndQuantity, processDataType);

            if (orderDTO == null) {
                return null;
            }

            List<OrderDetailDTO> orderDetailDTOList = orderDetailService.processData(orderDTO.getOrderId(), productAndQuantity, processDataType);
            return orderDTO;
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private boolean hasValidInput(int userId, double totalAmount, OrderStatus status, Address shippingAddress,
                                  Address billingAddress, Map<Product, Integer> productAndQuantity, ProcessDataType processDataType)
            throws ErrorHandling.InvalidValueException, ErrorHandling.InvalidAddressException, ErrorHandling.InvalidQuantityException,
            ErrorHandling.InvalidProductException {
        return InputValidator.hasValidValue(totalAmount) && status != null &&
                InputValidator.hasValidAddress(userId, billingAddress) && InputValidator.hasValidAddress(userId, shippingAddress) &&
                InputValidator.hasValidProductAndQuantity(productAndQuantity) && processDataType != null;
    }

    private OrderDTO performDmlAction(int userId, double totalAmount, OrderStatus status, Address shippingAddress,
                                      Address billingAddress, Map<Product, Integer> productAndQuantity, ProcessDataType processDataType)
            throws ErrorHandling.InvalidProcessDataType {
        if (InputValidator.hasValidProcessDataType(processDataType, ProcessDataType.ADD)) {
            return orderRepository.addOrder(userId, totalAmount, status, shippingAddress,
                    billingAddress, productAndQuantity);
        }
        return null;
    }
}
