package main.java.ostro.veda.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.java.ostro.veda.common.dto.AuditDataDTO;
import main.java.ostro.veda.common.dto.UserDTO;
import main.java.ostro.veda.common.error.ErrorHandling;
import main.java.ostro.veda.common.validation.SanitizeUtil;
import main.java.ostro.veda.common.validation.ValidateUtil;
import main.java.ostro.veda.db.UserRepository;
import main.java.ostro.veda.db.helpers.database.Action;
import main.java.ostro.veda.db.helpers.database.DbTables;
import main.java.ostro.veda.db.helpers.database.UserColumns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepositoryImpl;
    private final AuditService auditServiceImpl;

    @Autowired
    public UserServiceImpl(UserRepository userRepositoryImpl, AuditService auditServiceImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.auditServiceImpl = auditServiceImpl;
    }

    @Override
    public UserDTO add(@NonNull UserDTO userDTO, @NonNull String password) {
        log.info("add() new User = [{}, {}]", userDTO.getUsername(), userDTO.getEmail());
        try {
            ValidateUtil.validateUser(userDTO, password);
            userDTO = SanitizeUtil.sanitizeUser(userDTO);
            UserDTO user = getUserWithSaltAndHash(userDTO, password);

            UserDTO userReturned = userRepositoryImpl.add(user);
        auditServiceImpl.add(List.of(new AuditDataDTO(userReturned.getUsername(), 0, 0,
                UserColumns.USERNAME.getColumnName(), Action.INSERT.getActionName(),
                DbTables.USER.getTableName(), userReturned.getUserId(), userReturned.getUserId())));

        return userReturned;
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
            UserDTO userReturned = userRepositoryImpl.update(user);
            auditServiceImpl.add(List.of(new AuditDataDTO(userReturned.getUsername(), 0, 0,
                    UserColumns.USERNAME.getColumnName(), Action.INSERT.getActionName(),
                    DbTables.USER.getTableName(), userReturned.getUserId(), userReturned.getUserId())));

            return userReturned;
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
