package main.java.ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.AddressDTO;
import main.java.ostro.veda.db.helpers.EntityManagerHelper;
import main.java.ostro.veda.db.helpers.database.AddressColumns;
import main.java.ostro.veda.db.jpa.Address;
import main.java.ostro.veda.db.jpa.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerHelper entityManagerHelper;

    public AddressRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    @Transactional
    public AddressDTO add(@NonNull AddressDTO addressDTO) {

        log.info("add() Address for User = {}", addressDTO.getUser().getUserId());
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

            if (address.getUser().getUserId() == addressDTO.getUser().getUserId()) {
                return null;
            }
        } else {
            address = buildAddress(addressDTO);
        }

        if (address == null) return null;

        try {

            this.entityManager.persist(address);
            return address.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public AddressDTO update(@NonNull AddressDTO addressDTO) {

        log.info("update() Address for User = {}", addressDTO.getUser().getUserId());
        Address address = this.entityManager.find(Address.class, addressDTO.getAddressId());
        if (address == null) return null;

        Address updatedAddress = buildAddress(addressDTO);
        if (updatedAddress == null) return null;
        address.updateAddress(updatedAddress);

        try {

            this.entityManager.merge(address);
            return address.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public Address buildAddress(@NonNull AddressDTO addressDTO) {

        log.info("buildAddress() Address for User = {}", addressDTO.getUser().getUserId());
        User user = this.entityManager.find(User.class, addressDTO.getUser().getUserId());
        if (user == null) return null;

        return new Address()
                .setAddressId(addressDTO.getAddressId())
                .setUser(user)
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
