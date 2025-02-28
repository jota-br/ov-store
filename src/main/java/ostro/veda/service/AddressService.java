package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.AddressRepository;

@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressDTO addAddress(AddressDTO addressDTO) {
        try {
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);
            return addressRepository.addAddress(addressDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public AddressDTO updateAddress(AddressDTO addressDTO) {
        try {
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);
            return addressRepository.updateAddress(addressDTO);
        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
