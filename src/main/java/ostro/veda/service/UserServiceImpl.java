package ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.common.dto.UserDTO;
import ostro.veda.common.error.ErrorHandling;
import ostro.veda.common.util.Action;
import ostro.veda.common.validation.SanitizeUtil;
import ostro.veda.common.validation.ValidateUtil;
import ostro.veda.db.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
public class UserServiceImpl implements UserService {
    private final ApplicationEventPublisher applicationEventPublisher;

    private final UserRepository userRepositoryImpl;

    @Autowired
    public UserServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepositoryImpl) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public UserDTO add(UserDTO userDTO) {
        log.info("add() -> add() new User");
        try {
            if (userDTO.getSalt().isBlank()) return null;
            return userRepositoryImpl.add(userDTO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO add(@NonNull UserDTO userDTO, @NonNull String password) {
        log.info("add() new User = [{}, {}]", userDTO.getUsername(), userDTO.getEmail());
        try {
            ValidateUtil.validateUser(userDTO, password);
            userDTO = SanitizeUtil.sanitizeUser(userDTO);
            UserDTO user = getUserWithSaltAndHash(userDTO, password);

            userDTO = add(user);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, userDTO, 1);

            return userDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public UserDTO update(@NonNull UserDTO userDTO, @NonNull String password) {
        log.info("update() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());
        try {
            ValidateUtil.validateUser(userDTO, password);
            UserDTO user = getUserWithSaltAndHash(userDTO, password);

            userDTO = userRepositoryImpl.update(user);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, userDTO, 1);

            return userDTO;

        } catch (ErrorHandling.InvalidInputException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private UserDTO getUserWithSaltAndHash(@NonNull UserDTO userDTO, @NonNull String password) {

        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(password, salt);

        return new UserDTO(userDTO.getUserId(), userDTO.getUsername(), encodedSalt, hash, userDTO.getEmail(),
                userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.isActive(), userDTO.getRole(),
                userDTO.getAddresses(), userDTO.getCreatedAt(), userDTO.getUpdatedAt(), userDTO.getVersion());
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
            log.warn(e.getMessage());
            return null;
        }
    }

    private String getHash(@NonNull String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
