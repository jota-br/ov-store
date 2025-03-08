package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.service.interfaces.AddressService;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.enums.Action;
import ostro.veda.util.validation.SanitizeUtil;
import ostro.veda.util.validation.ValidateUtil;
import ostro.veda.repository.interfaces.AddressRepository;

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
    public AddressDto add(@NonNull AddressDto addressDTO) {
        try {
            log.info("add() Address for User = {}", addressDTO.getUser().getUserId());
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);

            addressDTO = addressRepository.add(addressDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, addressDTO, 1);

            return addressDTO;

        } catch (InputException.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public AddressDto update(@NonNull AddressDto addressDTO) {
        try {
            log.info("update() Address for User = {}", addressDTO.getUser().getUserId());
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);

            addressDTO = addressRepository.update(addressDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, addressDTO, 1);

            return addressDTO;

        } catch (InputException.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
