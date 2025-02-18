package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderRepository;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.loggerService.Logger;

import java.util.List;
import java.util.Map;

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final OrderStatusHistoryService orderStatusHistoryService;

    public OrderService(OrderRepository orderRepository, OrderDetailService orderDetailService, OrderStatusHistoryService orderStatusHistoryService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.orderStatusHistoryService = orderStatusHistoryService;
    }

    public Map<OrderDTO, List<OrderDetailDTO>> processData(int userId, double totalAmount, OrderStatus status, Address shippingAddress,
                                                           Address billingAddress, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType) {

        try {
            if (!hasValidInput(userId, totalAmount, status, shippingAddress, billingAddress, productAndQuantity, processDataType))
                return null;

            OrderDTO orderDTO = performDmlAction(userId, totalAmount, status.getStatus(), shippingAddress, billingAddress, processDataType);

            if (orderDTO == null) {
                return null;
            }

            List<OrderDetailDTO> orderDetailDTOList = orderDetailService.processData(orderDTO.getOrderId(), productAndQuantity, processDataType);
            if (orderDetailDTOList == null) {
                int retries = 0;
                while (retries < 10) {
                    boolean isCanceled = cancelOrder(orderDTO);
                    if (isCanceled) {
                        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryService.processData(orderDTO.getOrderId(), OrderStatus.CANCELLED.getStatus(), ProcessDataType.ADD);
                        orderDTO.getOrderStatusHistory().add(orderStatusHistoryDTO);
                        break;
                    }
                    retries++;
                }
                return null;
            }

            OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryService.processData(orderDTO.getOrderId(), orderDTO.getStatus(), ProcessDataType.ADD);
            orderDTO.getOrderStatusHistory().add(orderStatusHistoryDTO);
            return Map.of(orderDTO, orderDetailDTOList);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private boolean hasValidInput(int userId, double totalAmount, OrderStatus status, Address shippingAddress,
                                  Address billingAddress, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType)
            throws ErrorHandling.InvalidValueException, ErrorHandling.InvalidAddressException, ErrorHandling.InvalidQuantityException,
            ErrorHandling.InvalidProductException {
        return InputValidator.hasValidValue(totalAmount) && status != null &&
                InputValidator.hasValidAddress(userId, billingAddress) && InputValidator.hasValidAddress(userId, shippingAddress) &&
                InputValidator.hasValidProductAndQuantity(productAndQuantity) && processDataType != null;
    }

    private OrderDTO performDmlAction(int userId, double totalAmount, String status, Address shippingAddress,
                                      Address billingAddress, ProcessDataType processDataType)
            throws ErrorHandling.InvalidProcessDataTypeException {
        if (InputValidator.hasValidProcessDataType(processDataType, ProcessDataType.ADD)) {
            return orderRepository.addOrder(userId, totalAmount, status, shippingAddress, billingAddress);
        }
        return null;
    }

    private boolean cancelOrder(OrderDTO order) {
        return orderRepository.cancelOrder(order);
    }
}
