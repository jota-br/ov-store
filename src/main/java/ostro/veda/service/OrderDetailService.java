package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.ProductDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderDetailRepository;
import ostro.veda.loggerService.Logger;

import java.util.Map;

public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public boolean processData(int orderId, Map<ProductDTO, Integer> productAndQuantity, ProcessDataType processDataType) {
//        List<OrderDetailDTO>
        try {
            return hasValidInput(processDataType);
//            if (!hasValidInput(processDataType)) return null;
//            return performDmlAction(orderId, productAndQuantity);
        } catch (Exception e) {
            Logger.log(e);
            return false;
        }
    }

    private boolean hasValidInput(ProcessDataType processDataType) throws ErrorHandling.InvalidProcessDataTypeException {
        return processDataType != null && InputValidator.hasValidProcessDataType(processDataType, ProcessDataType.ADD);
    }

//    private List<OrderDetailDTO> performDmlAction(int orderId, Map<ProductDTO, Integer> productAndQuantity)
//            throws ErrorHandling.InsufficientInventoryException,
//            OptimisticLockException {
//        return orderDetailRepository.addOrderDetail(orderId, productAndQuantity);
//    }
}
