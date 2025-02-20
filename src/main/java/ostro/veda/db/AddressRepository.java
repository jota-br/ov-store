package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.columns.AddressColumns;
import ostro.veda.db.jpa.Address;

import java.util.List;
import java.util.Map;

public class AddressRepository extends Repository {

    public AddressRepository(EntityManager em, EntityManagerHelper entityManagerHelper) {
        super(em, entityManagerHelper);
    }

    public AddressDTO addAddress(int userId, String streetAddress, String addressNumber, String addressType, String city,
                                 String state, String zipCode, String country, boolean isActive) {

        List<Address> result = this.entityManagerHelper.findByFields(this.em, Address.class, Map.of(
                AddressColumns.STREET_ADDRESS.getColumnName(), streetAddress,
                AddressColumns.ADDRESS_NUMBER.getColumnName(), addressNumber,
                AddressColumns.ADDRESS_TYPE.getColumnName(), addressType,
                AddressColumns.CITY.getColumnName(), city,
                AddressColumns.STATE.getColumnName(), state,
                AddressColumns.ZIPCODE.getColumnName(), zipCode,
                AddressColumns.COUNTRY.getColumnName(), country
        ));
        Address address = null;
        if (result != null && !result.isEmpty()) {
            address = result.get(0);

            if (address.getUserId() == userId) {
                return null;
            }
        } else {
            address = new Address(userId, streetAddress, addressNumber,
                    addressType, city, state, zipCode, country, isActive);
        }

        boolean isInserted = this.entityManagerHelper.executePersist(this.em, address);
        if (!isInserted) {
            return null;
        }

        return address.transformToDto();
    }

    public AddressDTO updateAddress(int addressId, int userId, String streetAddress, String addressNumber, String addressType, String city,
                                     String state, String zipCode, String country, boolean isActive) {

        Address address = this.em.find(Address.class, addressId);

        if (address == null) {
            return addAddress(userId, streetAddress, addressNumber,
                    addressType, city, state, zipCode, country, isActive);
        }

        address.updateAddress(new Address(userId, streetAddress, addressNumber,
                addressType, city, state, zipCode, country, isActive));

        boolean isInserted = this.entityManagerHelper.executeMerge(this.em, address);
        if (!isInserted) {
            return null;
        }

        return address.transformToDto();
    }
}
