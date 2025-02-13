package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO processData(int userId, String username, String password, String email,
                               String firstName, String lastName, String phone, boolean isActive, ProcessDataType processDataType) {

        try {
            if (processDataType == null) {
                return null;
            }

            int usernameMinLength = 8;
            int passwordMinLength = 8;
            int firstNameMinLength = 3;
            int lastNameMinLength = 3;

            String usernameCheck = InputValidator.stringChecker(username, false, false, false, usernameMinLength);
            String passwordCheck = InputValidator.stringChecker(password, false, false, false, passwordMinLength);
            String emailCheck = InputValidator.emailChecker(email);
            String firstNameCheck = InputValidator.stringChecker(firstName, true, false, true, firstNameMinLength);
            String lastNameCheck = InputValidator.stringChecker(lastName, true, false, true, lastNameMinLength);
            String phoneCheck = InputValidator.phoneChecker("+" + phone);

            if (usernameCheck == null || passwordCheck == null || emailCheck == null ||
                    (firstNameCheck == null && !firstName.isEmpty()) ||
                    (lastNameCheck == null && !lastName.isEmpty()) ||
                    (phoneCheck == null && !phone.isEmpty())) {
                return null;
            }


            return performDmlAction(userId, username, password, email,
                    firstName, lastName, phone, isActive, processDataType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private UserDTO performDmlAction(int userId, String username, String password, String email,
                                     String firstName, String lastName, String phone, boolean isActive, ProcessDataType processDataType) {
        switch (processDataType) {
            case ADD -> {

                byte[] salt = getSalt();
                String encodedSalt = Base64.getEncoder().encodeToString(salt);
                String hash = getHash(password, salt);

                return userRepository.addUser(username, encodedSalt, hash, email, firstName, lastName, phone, isActive);
            }
            case UPDATE -> {

                byte[] salt = getSalt();
                String encodedSalt = getEncodedSalt(salt);
                String hash = getHash(password, salt);

                return userRepository.updateUser(userId, username, encodedSalt, hash, email, firstName, lastName, phone, isActive);
            }
            default -> {
                return null;
            }
        }
    }

    private String getEncodedSalt(byte[] salt) {
        return Base64.getEncoder().encodeToString(salt);
    }

    private byte[] getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[32];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getHash(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
