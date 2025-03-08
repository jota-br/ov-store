package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.util.Action;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.AddressRepository;

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

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, addressDTO, 1);

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

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, addressDTO, 1);

            return addressDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
