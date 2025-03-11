package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.repository.dao.Address;
import ostro.veda.repository.dao.User;
import ostro.veda.repository.helpers.EntityManagerHelper;
import ostro.veda.repository.helpers.enums.AddressColumns;
import ostro.veda.repository.interfaces.AddressRepository;
import ostro.veda.util.validation.ValidateParameter;

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
    public AddressDto add(AddressDto addressDTO) {

        log.info("add() new Address = {} for User = {}", addressDTO.getAddressType() , addressDTO.getUser().getUserId());

        List<Address> result = this.entityManagerHelper.findByFields(this.entityManager, Address.class, Map.of(
                AddressColumns.STREET_ADDRESS.getColumnName(), addressDTO.getStreetAddress(),
                AddressColumns.ADDRESS_NUMBER.getColumnName(), addressDTO.getAddressNumber(),
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
    public AddressDto update(AddressDto addressDTO) {

        log.info("update() Address = {} for User = {}", addressDTO.getAddressType() , addressDTO.getUser().getUserId());
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

    private Address buildAddress(AddressDto addressDTO) {

        ValidateParameter.isNull(this.getClass(), addressDTO);

        log.info("buildAddress() Address for User = {}", addressDTO.getUser().getUserId());

        User user = this.entityManager.find(User.class, addressDTO.getUser().getUserId());
        if (user == null) return null;

        return Address
                .builder()
                .addressId(addressDTO.getAddressId())
                .user(user)
                .streetAddress(addressDTO.getStreetAddress())
                .addressNumber(addressDTO.getAddressNumber())
                .addressType(addressDTO.getAddressType())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .zipCode(addressDTO.getZipCode())
                .country(addressDTO.getCountry())
                .isActive(addressDTO.isActive())
                .createdAt(addressDTO.getCreatedAt())
                .updatedAt(addressDTO.getUpdatedAt())
                .build();
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
