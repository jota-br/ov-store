package ostro.veda.db;

import jakarta.persistence.EntityTransaction;
import ostro.veda.common.dto.AddressDTO;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.helpers.JPAUtil;
import ostro.veda.db.helpers.columns.UserColumns;
import ostro.veda.db.jpa.Address;
import ostro.veda.db.jpa.Role;
import ostro.veda.db.jpa.User;
import ostro.veda.loggerService.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository extends Repository {

    public UserDTO addUser(UserDTO user) {

        List<User> hasUser = this.entityManagerHelper.findByFields(this.em, User.class,
                Map.of(UserColumns.USERNAME.getColumnName(), user.getUsername()));
        if (hasUser != null && !hasUser.isEmpty()) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            List<Address> addressList = getAddresses(user);
            User newUser = getNewUser(user, addressList);
            this.em.persist(newUser);

            transaction.commit();
            return newUser.transformToDto();

        }  catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
            return null;
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {

        User user = this.em.find(User.class, userDTO.getUserId());
        if (user == null) {
            return null;
        }

        EntityTransaction transaction = null;
        try {
            transaction = this.em.getTransaction();
            transaction.begin();

            List<Address> addressList = getAddresses(userDTO);
            user.updateUser(getUpdatedData(userDTO, user, addressList));
            this.em.persist(user);

            transaction.commit();
            return user.transformToDto();

        }  catch (Exception e) {
            Logger.log(e);
            JPAUtil.transactionRollBack(transaction);
            return null;
        }
    }

    private User getNewUser(UserDTO user, List<Address> addressList) {
        Role role = this.em.find(Role.class, 20);
        return new User(user.getUserId(), user.getUsername(), user.getSalt(), user.getHash(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getPhone(), user.isActive(), role,
                addressList, user.getCreatedAt(), user.getUpdatedAt());
    }

    private User getUpdatedData(UserDTO userDTO, User user, List<Address> addressList) {
        return new User(user.getUserId(), userDTO.getUsername(), userDTO.getSalt(), userDTO.getHash(),
                userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(),
                userDTO.isActive(), user.getRole(), addressList, userDTO.getCreatedAt(), userDTO.getUpdatedAt());
    }

    private List<Address> getAddresses(UserDTO user) {
        List<Address> addressList = new ArrayList<>();
        for (AddressDTO addressDTO : user.getAddresses()) {
            addressList.add(new Address(user.getUserId(), addressDTO.getStreetAddress(), addressDTO.getAddressNumber(),
                    addressDTO.getAddressType(), addressDTO.getCity(), addressDTO.getState(), addressDTO.getZipCode(),
                    addressDTO.getCountry(), addressDTO.isActive()));
        }
        return addressList;
    }
}
