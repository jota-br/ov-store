package ostro.veda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ostro.veda.model.dto.AddressDto;
import ostro.veda.model.dto.UserDto;
import ostro.veda.repository.dao.Address;
import ostro.veda.repository.dao.Role;
import ostro.veda.repository.dao.User;
import ostro.veda.repository.helpers.EntityManagerHelper;
import ostro.veda.repository.helpers.enums.UserColumns;
import ostro.veda.repository.interfaces.UserRepository;
import ostro.veda.util.exception.InputException;
import ostro.veda.util.validation.ValidateParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public UserRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    @Transactional
    public UserDto add(UserDto userDTO) {

        log.info("add() new User = [{}, {}]", userDTO.getUsername(), userDTO.getEmail());

        List<User> hasUser = this.entityManagerHelper.findByFields(this.entityManager, User.class,
                Map.of(UserColumns.USERNAME.getColumnName(), userDTO.getUsername()));
        if (hasUser != null && !hasUser.isEmpty())
            throw new IllegalArgumentException("Cannot add user %s, it already exists".formatted(userDTO.getUsername()));

        try {
            User newUser = buildUser(userDTO);
            this.entityManager.persist(newUser);


            return newUser.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDTO) {

        log.info("update() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        User user = this.entityManager.find(User.class, userDTO.getUserId());
        if (user == null) {
            return null;
        }

        try {

            User newUserData = buildUser(userDTO);
            user.updateUser(newUserData);
            this.entityManager.persist(user);
            return user.transformToDto();

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    private User buildUser(UserDto userDTO) throws InputException.InvalidInputException {

        ValidateParameter.isNull(this.getClass(), userDTO);

        log.info("buildUser() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        List<Address> addressList = buildAddress(userDTO);
        ostro.veda.repository.dao.Role role = this.entityManager.find(Role.class, userDTO.getRole().getRoleId());

        if (role == null)
            throw new InputException.InvalidInputException("Invalid Role id, roleId:", String.valueOf(userDTO.getRole().getRoleId()));

        User user = User
                .builder()
                .userId(userDTO.getUserId())
                .username(userDTO.getUsername())
                .salt(userDTO.getSalt())
                .hash(userDTO.getHash())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .phone(userDTO.getPhone())
                .isActive(userDTO.isActive())
                .role(role)
                .addresses(addressList)
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .build();

        for (Address address : addressList) {
            address.setUser(user);
        }
        return user;
    }

    private List<Address> buildAddress(UserDto userDTO) {

        ValidateParameter.isNull(this.getClass(), userDTO);

        log.info("buildAddress() Address for User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        User user = this.entityManager.find(User.class, userDTO.getUserId());
        List<Address> addressList = new ArrayList<>();
        for (AddressDto addressDTO : userDTO.getAddresses()) {
            Address address = Address
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
            addressList.add(address);
        }
        return addressList;
    }

    @Override
    public void close() throws Exception {
        log.info("close() resource EntityManager");
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
}
