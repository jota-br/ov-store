package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.AddressColumns;
import ostro.veda.db.jpa.Address;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AddressRepositoryImpl implements AddressRepository {

    private final EntityManager entityManager;
    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public AddressRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        this.entityManager = entityManager;
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    public AddressDTO add(@NonNull AddressDTO addressDTO) {
        log.info("add() Address for User = {}", addressDTO.getUserId());
        List<Address> result = this.entityManagerHelper.findByFields(this.entityManager, Address.class, Map.of(
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
            address = buildAddress(addressDTO);
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            this.entityManager.persist(address);

            transaction.commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return address.transformToDto();
    }

    @Override
    public AddressDTO update(@NonNull AddressDTO addressDTO) {
        log.info("update() Address for User = {}", addressDTO.getUserId());
        Address address = this.entityManager.find(Address.class, addressDTO.getAddressId());
        if (address == null) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            Address updatedAddress = buildAddress(addressDTO);
            address.updateAddress(updatedAddress);

            this.entityManager.merge(address);

            transaction.commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }

        return address.transformToDto();
    }

    @Override
    public Address buildAddress(@NonNull AddressDTO addressDTO) {
        log.info("buildAddress() Address for User = {}", addressDTO.getUserId());
        return new Address()
                .setAddressId(addressDTO.getAddressId())
                .setUserId(addressDTO.getUserId())
                .setStreetAddress(addressDTO.getStreetAddress())
                .setAddressNumber(addressDTO.getAddressNumber())
                .setAddressType(addressDTO.getAddressType())
                .setCity(addressDTO.getCity())
                .setState(addressDTO.getState())
                .setZipCode(addressDTO.getZipCode())
                .setCountry(addressDTO.getCountry())
                .setActive(addressDTO.isActive())
                .setCreatedAt(addressDTO.getCreatedAt())
                .setUpdatedAt(addressDTO.getUpdatedAt());
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
