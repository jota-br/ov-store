package ostro.veda.service;

import ostro.veda.common.InputValidator;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.db.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UserService {

    public static UserDTO processData(String username, String password, String email,
                                      String firstName, String lastName, String phone, boolean isActive) {

        int usernameMinLength = 8;
        int passwordMinLength = 8;
        int firstNameMinLength = 3;
        int lastNameMinLength = 3;

        String usernameCheck = InputValidator.stringChecker(username, false, false, usernameMinLength);
        String passwordCheck = InputValidator.stringChecker(password, false, false, passwordMinLength);
        String emailCheck = InputValidator.emailChecker(email);
        String firstNameCheck = InputValidator.stringChecker(firstName, true, false, firstNameMinLength);
        String lastNameCheck = InputValidator.stringChecker(lastName, true, false, lastNameMinLength);
        String phoneCheck = InputValidator.phoneChecker("+" + phone);

        if (usernameCheck == null || passwordCheck == null || emailCheck == null ||
                (firstNameCheck == null && !firstName.isEmpty()) ||
                (lastNameCheck == null && !lastName.isEmpty()) ||
                (phoneCheck == null && !phone.isEmpty())) {
            return null;
        }

        byte[] salt = getSalt();
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hash = getHash(password, salt);

        return UserRepository.addUser(username, encodedSalt, hash, email, firstName, lastName, phone, isActive);
    }

    private static byte[] getSalt() {
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

    private static String getHash(String password, byte[] salt) {
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
