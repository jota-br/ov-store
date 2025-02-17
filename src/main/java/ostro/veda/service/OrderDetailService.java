package ostro.veda.service;

import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderDetailRepository;
import ostro.veda.loggerService.Logger;

import java.util.List;
import java.util.Map;

public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<OrderDetailDTO> processData(int orderId, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType) {

        try {
            return performDmlAction(orderId, productAndQuantity, processDataType);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private List<OrderDetailDTO> performDmlAction(int orderId, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType)
            throws ErrorHandling.InvalidProcessDataTypeException, ErrorHandling.InsufficientInventoryException,
            OptimisticLockException {
        if (InputValidator.hasValidProcessDataType(processDataType, ProcessDataType.ADD)) {
            return orderDetailRepository.addOrderDetail(orderId, productAndQuantity);
        }
        return null;
    }
}
