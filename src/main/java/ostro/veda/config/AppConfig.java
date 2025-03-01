package ostro.veda.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ostro.veda.db.AddressRepositoryImpl;
import ostro.veda.db.UserRepositoryImpl;
import ostro.veda.db.helpers.EntityManagerFactoryManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.service.AddressServiceImpl;
import ostro.veda.service.UserServiceImpl;

@Configuration
@ComponentScan(basePackages = "ostro.veda")
public class AppConfig {

    @Bean
    public EntityManagerFactoryManager entityManagerFactoryManager() {
        return new EntityManagerFactoryManager();
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactoryManager entityManagerFactoryManager) {
        return entityManagerFactoryManager.getEntityManagerFactory().createEntityManager();
    }

    @Bean
    public EntityManagerHelper entityManagerHelper() {
        return new EntityManagerHelper();
    }

    @Bean
    public AddressServiceImpl addressServiceImpl(AddressRepositoryImpl addressRepository) {
        return new AddressServiceImpl(addressRepository);
    }

    @Bean
    public AddressRepositoryImpl addressRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        return new AddressRepositoryImpl(entityManager, entityManagerHelper);
    }

    @Bean
    public UserServiceImpl userServiceImpl(UserRepositoryImpl userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public UserRepositoryImpl userRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        return new UserRepositoryImpl(entityManager, entityManagerHelper);
    }
}
