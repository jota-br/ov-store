package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.db.UserRepository;
import ostro.veda.loggerService.Logger;

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
            if (!hasValidInput(username, password, email, firstName, lastName, phone, processDataType)) return null;
            firstName = InputValidator.stringSanitize(firstName);
            lastName = InputValidator.stringSanitize(lastName);

            return performDmlAction(userId, username, password, email,
                    firstName, lastName, phone, isActive, processDataType);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private boolean hasValidInput(String username, String password, String email, String firstName,
                                  String lastName, String phone, ProcessDataType processDataType)
            throws ErrorHandling.InvalidUsernameException, ErrorHandling.InvalidPasswordException,
            ErrorHandling.InvalidEmailException, ErrorHandling.InvalidPhoneException, ErrorHandling.InvalidPersonName {
        return InputValidator.hasValidPhone("+" + phone) &&
                InputValidator.hasValidUsername(username) &&
                InputValidator.hasValidPassword(password) &&
                InputValidator.hasValidEmail(email) &&
                InputValidator.hasValidPersonName(firstName) &&
                InputValidator.hasValidPersonName(lastName) &&
                processDataType != null;
    }

//    private boolean hasValidLength(String firstName, String lastName) {
//        int min = 3;
//        int max = 255;
//        return InputValidator.hasValidLength(firstName, min, max) &&
//                InputValidator.hasValidLength(lastName, min, max);
//    }

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
            Logger.log(e);
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
            Logger.log(e);
        }
        return null;
    }
}
