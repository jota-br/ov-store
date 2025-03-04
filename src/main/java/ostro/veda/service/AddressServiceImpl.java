package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.db.helpers.database.Action;
import ostro.veda.service.events.AuditEvent;

@Slf4j
@Component
public class AddressServiceImpl implements AddressService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(ApplicationEventPublisher applicationEventPublisher, AddressRepository addressRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO add(@NonNull AddressDTO addressDTO) {
        try {
            log.info("add() Address for User = {}", addressDTO.getUser().getUserId());
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);

            addressDTO = addressRepository.add(addressDTO);

            AuditEvent event = AuditEvent.builder()
                    .source(this)
                    .action(Action.INSERT)
                    .addressDTO(addressDTO)
                    .userId(1)
                    .id(addressDTO.getAddressId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            return addressDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public AddressDTO update(@NonNull AddressDTO addressDTO) {
        try {
            log.info("update() Address for User = {}", addressDTO.getUser().getUserId());
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);

            addressDTO = addressRepository.update(addressDTO);

            AuditEvent event = AuditEvent.builder()
                    .source(this)
                    .action(Action.INSERT)
                    .addressDTO(addressDTO)
                    .userId(1)
                    .id(addressDTO.getAddressId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            return addressDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
