package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.repository.interfaces.AddressRepository;
import ostro.veda.service.interfaces.AddressService;
import ostro.veda.util.enums.Action;
import ostro.veda.util.sanitization.SanitizeAddress;
import ostro.veda.util.validation.ValidatorUtil;

@Slf4j
@Component
public class AddressServiceImpl implements AddressService {

    private final ValidatorUtil validatorUtil;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(ValidatorUtil validatorUtil, ApplicationEventPublisher applicationEventPublisher, AddressRepository addressRepository) {
        this.validatorUtil = validatorUtil;
        this.applicationEventPublisher = applicationEventPublisher;
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDto add(AddressDto addressDTO) {
        try {

            log.info("add() Address for User = {}", addressDTO.getUser().getUserId());

            addressDTO = new SanitizeAddress().sanitize(addressDTO);

            addressDTO = addressRepository.add(addressDTO);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, addressDTO, 1);

            return addressDTO;

        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public AddressDto update(AddressDto addressDTO) {
        try {

            log.info("update() Address for User = {}", addressDTO.getUser().getUserId());

            addressDTO = new SanitizeAddress().sanitize(addressDTO);

            addressDTO = addressRepository.update(addressDTO);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, addressDTO, 1);

            return addressDTO;

        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
