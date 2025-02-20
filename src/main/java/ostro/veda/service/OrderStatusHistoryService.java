package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderStatusHistoryDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.OrderStatusHistoryRepository;
import ostro.veda.loggerService.Logger;

public class OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderStatusHistoryService(OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public boolean processData(int orderId, String status, ProcessDataType processDataType) {
//        OrderStatusHistoryDTO
        try {
            return hasValidInput(orderId, status, processDataType);
//            if (!hasValidInput(orderId, status, processDataType)) return null;
//            return performDmlAction(orderId, status, processDataType);
        } catch (Exception e) {
            Logger.log(e);
            return false;
        }
    }

    private boolean hasValidInput(int orderId, String status, ProcessDataType processDataType) throws ErrorHandling.InvalidOrderStatusException, ErrorHandling.InvalidProcessDataTypeException {
        return InputValidator.hasValidOrderStatus(status) && processDataType != null &&
                InputValidator.hasValidProcessDataType(processDataType, ProcessDataType.ADD) && orderId > 0;
    }

    private OrderStatusHistoryDTO performDmlAction(int orderId, String status, ProcessDataType processDataType) {
//        return orderStatusHistoryRepository.addOrderStatusHistory(orderId, status);
        return null;
    }
}
