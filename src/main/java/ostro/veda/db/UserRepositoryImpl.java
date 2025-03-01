package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.UserColumns;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Role;
import ostro.veda.db.jpa.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;
    private final EntityManagerHelper entityManagerHelper;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        this.entityManager = entityManager;
        this.entityManagerHelper = entityManagerHelper;
    }

    @Override
    public UserDTO add(@NonNull UserDTO userDTO) {
        log.info("add() new User = [{}, {}]", userDTO.getUsername(), userDTO.getEmail());

        List<User> hasUser = this.entityManagerHelper.findByFields(this.entityManager, User.class,
                Map.of(UserColumns.USERNAME.getColumnName(), userDTO.getUsername()));
        if (hasUser != null && !hasUser.isEmpty()) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            User newUser = buildUser(userDTO);
            this.entityManager.persist(newUser);

            transaction.commit();
            return newUser.transformToDto();

        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
            return null;
        }
    }

    @Override
    public UserDTO update(@NonNull UserDTO userDTO) {
        log.info("update() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        User user = this.entityManager.find(User.class, userDTO.getUserId());
        if (user == null) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            user.updateUser(buildUser(userDTO));
            this.entityManager.persist(user);

            transaction.commit();
            return user.transformToDto();

        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
            return null;
        }
    }

    @Override
    public User buildUser(@NonNull UserDTO userDTO) {
        log.info("buildAddress() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        Role role = this.entityManager.find(Role.class, 20);
        List<Address> addressList = buildAddress(userDTO);

        return new User()
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
    }

    @Override
    public List<Address> buildAddress(@NonNull UserDTO userDTO) {
        log.info("buildAddress() Address for User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        List<Address> addressList = new ArrayList<>();
        for (AddressDTO addressDTO : userDTO.getAddresses()) {
            Address address = new Address()
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
