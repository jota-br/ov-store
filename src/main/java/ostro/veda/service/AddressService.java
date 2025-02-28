package ostro.veda.service;

import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.AddressRepository;
import ostro.veda.loggerService.Logger;

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
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    public AddressDTO updateAddress(AddressDTO addressDTO) {
        try {
            ValidateUtil.validateAddress(addressDTO);
            addressDTO = SanitizeUtil.sanitizeAddress(addressDTO);
            return addressRepository.updateAddress(addressDTO);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }
}
