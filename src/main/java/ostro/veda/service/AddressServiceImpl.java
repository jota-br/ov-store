package main.java.ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.AddressDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.common.validation.SanitizeUtil;
import main.java.ostro.veda.common.validation.ValidateUtil;
import main.java.ostro.veda.db.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO add(@NonNull AddressDTO addressDTO) {
        try {
            log.info("add() Address for User = {}", addressDTO.getUser().getUserId());
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);
            return addressRepository.add(addressDTO);
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
            return addressRepository.update(addressDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
