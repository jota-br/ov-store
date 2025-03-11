package ostro.veda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ostro.veda.model.dto.UserDto;
import ostro.veda.repository.interfaces.UserRepository;
import ostro.veda.service.interfaces.UserService;
import ostro.veda.util.enums.Action;
import ostro.veda.util.sanitization.SanitizeUserDto;

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
    public UserDto add(UserDto userDTO) {

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
    public UserDto update(UserDto userDTO) {

        log.info("update() -> update() User");

        try {

            if (userDTO.getSalt().isBlank()) return null;
            return userRepositoryImpl.update(userDTO);

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public UserDto add(UserDto userDTO, String password) {

        log.info("add() new User = [{}, {}]", userDTO.getUsername(), userDTO.getEmail());

        try {

            userDTO = new SanitizeUserDto().sanitize(userDTO);
            UserDto user = getUserWithSaltAndHash(userDTO, password);

            userDTO = add(user);

            this.auditCaller(applicationEventPublisher, this, Action.INSERT, userDTO, 1);

            return userDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    @Override
    public UserDto update(UserDto userDTO, String password) {

        log.info("update() User = [{}, {}, {}]", userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail());

        try {

            userDTO = new SanitizeUserDto().sanitize(userDTO);
            UserDto user = getUserWithSaltAndHash(userDTO, password);

            userDTO = update(user);

            this.auditCaller(applicationEventPublisher, this, Action.UPDATE, userDTO, 1);

            return userDTO;

        } catch (Exception e) {

            log.warn(e.getMessage());
            return null;

        }
    }

    private UserDto getUserWithSaltAndHash(UserDto userDTO, String password) {

        log.info("getUserWithSaltAndHash() -> generating salt and hash");

        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(password, salt);

        return new UserDto(userDTO.getUserId(), userDTO.getUsername(), encodedSalt, hash, userDTO.getEmail(),
                userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPhone(), userDTO.isActive(), userDTO.getRole(),
                userDTO.getAddresses(), userDTO.getCreatedAt(), userDTO.getUpdatedAt(), userDTO.getVersion());
    }

    private String getEncodedSalt(byte[] salt) {
        log.info("getEncodedSalt()");
        return Base64.getEncoder().encodeToString(getSalt());
    }

    private byte[] getSalt() {

        log.info("getSalt()");

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

    private String getHash(String password, byte[] salt) {

        log.info("getHash()");

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
