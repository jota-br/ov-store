package ostro.veda.service.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.AuditDataDTO;
import ostro.veda.db.helpers.database.Action;
import ostro.veda.db.helpers.database.DbTables;
import ostro.veda.service.*;

@Slf4j
@Component
public class AuditEventListener {

    private final AuditService auditServiceImpl;

    @Autowired
    public AuditEventListener(AuditService auditServiceImpl) {
        this.auditServiceImpl = auditServiceImpl;
    }

    @EventListener
    public void handleAuditEvent(AuditEvent auditEvent) {
        log.info("handleAuditEvent() received");

        String table = "";
        String string = "";
        Action action = auditEvent.getAction();
        var source = auditEvent.getSource();
        if (source.getClass().equals(AddressServiceImpl.class)) {

            log.info("handleAuditEvent() -> AddressDTO.class found");
            table = DbTables.ADDRESS.getTableName();
            string = auditEvent.getAddressDTO().toJSON();

        } else if (source.getClass().equals(CategoryServiceImpl.class)) {

            log.info("handleAuditEvent() -> CategoryDTO.class found");
            table = DbTables.CATEGORY.getTableName();
            string = auditEvent.getCategoryDTO().toJSON();

        } else if  (source.getClass().equals(OrderServiceImpl.class)) {

            log.info("handleAuditEvent() -> OrderDTO.class found");
            table = DbTables.ORDER.getTableName();
            string = auditEvent.getOrderDTO().toJSON();

        } else if  (source.getClass().equals(ProductServiceImpl.class)) {

            log.info("handleAuditEvent() -> ProductDTO.class found");
            table = DbTables.PRODUCT.getTableName();
            string = auditEvent.getProductDTO().toJSON();

        } else if  (source.getClass().equals(UserServiceImpl.class)) {

            log.info("handleAuditEvent() -> UserDTO.class found");
            table = DbTables.USER.getTableName();
            string = auditEvent.getUserDTO().toJSON();

        } else {

            log.info("handleAuditEvent() -> No listening class received, event will close");
            return;
        }

        AuditDataDTO auditDataDTO = new AuditDataDTO(string, action.getActionName(), table,
                auditEvent.getId(), auditEvent.getUserId());
        auditServiceImpl.add(auditDataDTO);
    }
}
