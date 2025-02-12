package ostro.veda.db;

import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.jpa.Address;

import java.util.List;
import java.util.Map;

public class AddressRepository extends Repository {

    public AddressRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public AddressDTO addAddress(String streetAddress, String addressNumber, String addressType, String city,
                                 String state, String zip_code, String country, boolean isActive) {

        List<Address> result = this.entityManagerHelper.findByFields(this.em, Address.class, Map.of(
                "streetAddress", streetAddress,
                "addressNumber", addressNumber,
                "addressType", addressType,
                "city", city,
                "state", state,
                "zip_code", zip_code,
                "country", country,
                "isActive", "true"
        ));
        Address address = null;
        if (result != null && !result.isEmpty()) {
            address = result.get(0);
        } else {
            address = new Address(streetAddress, addressNumber,
                    addressType, city, state, zip_code, country, isActive);
        }

        boolean isInserted = this.entityManagerHelper.executePersist(this.em, address);
        if (!isInserted) {
            return null;
        }

        return address.transformToDto();
    }

    public AddressDTO updateAddress(int addressId, String streetAddress, String addressNumber, String addressType, String city,
                                     String state, String zip_code, String country, boolean isActive) {

        Address address = this.em.find(Address.class, addressId);

        if (address == null) {
            return addAddress(streetAddress, addressNumber,
                    addressType, city, state, zip_code, country, isActive);
        }

        address.updateAddress(new Address(streetAddress, addressNumber,
                addressType, city, state, zip_code, country, isActive));

        boolean isInserted = this.entityManagerHelper.executeMerge(this.em, address);
        if (!isInserted) {
            return null;
        }

        return address.transformToDto();
    }
}
