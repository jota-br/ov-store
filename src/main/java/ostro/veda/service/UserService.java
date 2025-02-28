package ostro.veda.service;

import ostro.veda.common.dto.UserDTO;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
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

    public UserDTO addUser(UserDTO userDTO, String password) {
        try {
            ValidateUtil.validateUser(userDTO, password);
            userDTO = SanitizeUtil.sanitizeUser(userDTO);
            UserDTO user = getUserWithSaltAndHash(userDTO, password);

            return userRepository.addUser(user);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    public UserDTO updateUser(UserDTO userDTO, String password) {
        try {
            ValidateUtil.validateUser(userDTO, password);
            UserDTO user = getUserWithSaltAndHash(userDTO, password);
            return userRepository.updateUser(user);
        } catch (Exception e) {
            Logger.log(e);
            return null;
        }
    }

    private UserDTO getUserWithSaltAndHash(UserDTO userDTO, String password) {

        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(password, salt);

        return new UserDTO(userDTO.getUserId(), userDTO.getUsername(), encodedSalt, hash, userDTO.getEmail(),
                userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.isActive(), userDTO.getRole(),
                userDTO.getAddresses(), userDTO.getCreatedAt(), userDTO.getUpdatedAt());
    }

    private String getEncodedSalt(byte[] salt) {
        return Base64.getEncoder().encodeToString(getSalt());
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
