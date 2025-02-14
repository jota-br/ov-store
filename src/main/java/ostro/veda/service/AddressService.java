package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.AddressRepository;

public class AddressService {

    AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressDTO processData(int addressId, String streetAddress, String addressNumber, String addressType, String city,
                                         String state, String zipCode, String country, boolean isActive, ProcessDataType processDataType) {

        try {
            if (processDataType == null) {
                return null;
            }

            // implement Google Map API (Geocoding)

            String cityCheck = InputValidator.stringChecker(city, false, true, false, 1);
            String stateCheck = InputValidator.stringChecker(state, false, true, false, 1);
            String zipCodeCheck = InputValidator.stringChecker(zipCode, false, true, false, 1);
            String countryCheck = InputValidator.stringChecker(country, false, true, false, 1);

            if (
                    !InputValidator.hasValidAddressNumber(streetAddress) ||
                    !InputValidator.hasValidAddressNumber(addressNumber) ||
                    !InputValidator.hasValidAddressType(addressType) ||
                    cityCheck == null ||
                    stateCheck == null ||
                    zipCodeCheck == null ||
                    countryCheck == null
            ) {
                return null;
            }

            streetAddress = InputValidator.stringSanitize(streetAddress);
            addressNumber = InputValidator.stringSanitize(addressNumber);

            if (
                    !InputValidator.lengthChecker(streetAddress, 1, streetAddress.length()) ||
                    !InputValidator.lengthChecker(addressNumber, 1, addressNumber.length())
            ) {
                return null;
            }

            return performDmlAction(addressId, streetAddress, addressNumber, addressType, city,
                    state, zipCode, country, isActive, processDataType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AddressDTO performDmlAction(int addressId, String streetAddress, String addressNumber, String addressType, String city,
                                        String state, String zipCode, String country, boolean isActive, ProcessDataType processDataType) {
        switch (processDataType) {
            case ADD -> {
                AddressDTO addressDTO = this.addressRepository.addAddress(1, streetAddress, addressNumber, addressType, city, state, zipCode, country, isActive);
                this.addressRepository.closeEm();
                return addressDTO;
            }
            case UPDATE -> {
                if (addressId < 1) {
                    return null;
                }
                AddressDTO addressDTO = this.addressRepository.updateAddress(1, addressId, streetAddress, addressNumber, addressType, city, state, zipCode, country, isActive);
                this.addressRepository.closeEm();
                return addressDTO;
            }
            default -> {
                return null;
            }
        }
    }
}
