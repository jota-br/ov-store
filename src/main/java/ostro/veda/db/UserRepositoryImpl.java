package main.java.ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.AddressDTO;
import main.java.ostro.veda.common.dto.UserDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.db.helpers.EntityManagerHelper;
import main.java.ostro.veda.db.helpers.database.UserColumns;
import main.java.ostro.veda.db.jpa.Address;
import main.java.ostro.veda.db.jpa.Role;
import main.java.ostro.veda.db.jpa.User;
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
        main.java.ostro.veda.db.jpa.Role role = this.entityManager.find(Role.class, userDTO.getRole().getRoleId());

        if (role == null)
            throw new ErrorHandling.InvalidInputException("Invalid Role id, roleId:", String.valueOf(userDTO.getRole().getRoleId()));

        User user = new User()
                .setUserId(userDTO.getUserId())
                .setUsername(userDTO.getUsername())
                .setSalt(userDTO.getSalt())
                .setHash(userDTO.getHash())
                .setEmail(userDTO.getEmail())
                .setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName())
                .setPhone(userDTO.getPhone())
                .setActive(userDTO.isActive())
                .setRole(role)
                .setAddresses(addressList)
                .setCreatedAt(userDTO.getCreatedAt())
                .setUpdatedAt(userDTO.getUpdatedAt());

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
            Address address = new Address()
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
