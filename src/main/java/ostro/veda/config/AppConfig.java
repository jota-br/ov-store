package ostro.veda.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ostro.veda.db.AddressRepositoryImpl;
import ostro.veda.db.OrderRepositoryImpl;
import ostro.veda.db.ProductRepositoryImpl;
import ostro.veda.db.UserRepositoryImpl;
import ostro.veda.db.helpers.EntityManagerFactoryManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.service.AddressServiceImpl;
import ostro.veda.service.OrderServiceImpl;
import ostro.veda.service.ProductServiceImpl;
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

    @Bean
    public ProductServiceImpl productServiceImpl(ProductRepositoryImpl productRepository) {
        return new ProductServiceImpl(productRepository);
    }

    @Bean
    public ProductRepositoryImpl productRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        return new ProductRepositoryImpl(entityManager, entityManagerHelper);
    }

    @Bean
    public OrderServiceImpl orderServiceImpl(OrderRepositoryImpl orderRepository) {
        return new OrderServiceImpl(orderRepository);
    }

    @Bean
    public OrderRepositoryImpl orderRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        return new OrderRepositoryImpl(entityManager, entityManagerHelper);
    }
}
