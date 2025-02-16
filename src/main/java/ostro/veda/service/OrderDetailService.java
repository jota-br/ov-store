package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderDTO;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderDetailRepository;
import ostro.veda.db.helpers.OrderStatus;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Product;
import ostro.veda.loggerService.Logger;

import java.util.List;
import java.util.Map;

public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<OrderDetailDTO> processData(int orderId, Map<Product, Integer> productAndQuantity, ProcessDataType processDataType) {

        try {
            return performDmlAction(orderId, productAndQuantity, processDataType);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private List<OrderDetailDTO> performDmlAction(int orderId, Map<Product, Integer> productAndQuantity, ProcessDataType processDataType)
            throws ErrorHandling.InvalidProcessDataType {
        if (InputValidator.hasValidProcessDataType(processDataType, ProcessDataType.ADD)) {
            return orderDetailRepository.addOrderDetail(orderId, productAndQuantity);
        }
        return null;
    }
}
