package ostro.veda.db;

import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.AddressColumns;
import ostro.veda.db.jpa.Address;

import java.util.List;
import java.util.Map;

@Slf4j
public class AddressRepository extends Repository {

    public AddressDTO addAddress(AddressDTO addressDTO) {

        List<Address> result = this.entityManagerHelper.findByFields(this.em, Address.class, Map.of(
                AddressColumns.STREET_ADDRESS.getColumnName(), addressDTO.getStreetAddress(),
                AddressColumns.ADDRESS_NUMBER.getColumnName(), addressDTO.getAddressNumber(),
                AddressColumns.ADDRESS_TYPE.getColumnName(), addressDTO.getAddressType(),
                AddressColumns.CITY.getColumnName(), addressDTO.getCity(),
                AddressColumns.STATE.getColumnName(), addressDTO.getState(),
                AddressColumns.ZIPCODE.getColumnName(), addressDTO.getZipCode(),
                AddressColumns.COUNTRY.getColumnName(), addressDTO.getCountry()
        ));
        Address address = null;
        if (result != null && !result.isEmpty()) {
            address = result.get(0);

            if (address.getUserId() == addressDTO.getUserId()) {
                return null;
            }
        } else {
            address = getAddress(addressDTO);
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            this.em.persist(address);

            transaction.commit();
        }  catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return address.transformToDto();
    }

    public AddressDTO updateAddress(AddressDTO addressDTO) {

        Address address = this.em.find(Address.class, addressDTO.getAddressId());
        if (address == null) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            Address updatedAddress = getAddress(addressDTO);
            address.updateAddress(updatedAddress);

            this.em.merge(address);

            transaction.commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return address.transformToDto();
    }

    private Address getAddress(AddressDTO addressDTO) {
        return new Address(addressDTO.getUserId(), addressDTO.getStreetAddress(), addressDTO.getAddressNumber(),
                addressDTO.getAddressType(), addressDTO.getCity(), addressDTO.getState(), addressDTO.getZipCode(),
                addressDTO.getCountry(), addressDTO.isActive());
    }
}
