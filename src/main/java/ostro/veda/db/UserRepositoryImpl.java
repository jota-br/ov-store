package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.enums.UserColumns;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Role;
import ostro.veda.db.jpa.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public UserDTO add(@NonNull UserDTO userDTO) {
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
    public UserDTO update(@NonNull UserDTO userDTO) {
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

    @Override
    public User buildUser(@NonNull UserDTO userDTO) throws ErrorHandling.InvalidInputException {
        log.info("buildUser() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        List<Address> addressList = buildAddress(userDTO);
        ostro.veda.db.jpa.Role role = this.entityManager.find(Role.class, userDTO.getRole().getRoleId());

        if (role == null)
            throw new ErrorHandling.InvalidInputException("Invalid Role id, roleId:", String.valueOf(userDTO.getRole().getRoleId()));

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

    @Override
    public List<Address> buildAddress(@NonNull UserDTO userDTO) {
        log.info("buildAddress() Address for User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        User user = this.entityManager.find(User.class, userDTO.getUserId());
        List<Address> addressList = new ArrayList<>();
        for (AddressDTO addressDTO : userDTO.getAddresses()) {
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
