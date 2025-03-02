package ostro.veda.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ostro.veda.db.*;
import ostro.veda.db.helpers.EntityManagerFactoryManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.service.*;

@Configuration
@ComponentScan(basePackages = "ostro.veda")
public class AppConfig {

    @Bean
    public EntityManagerFactoryManager entityManagerFactoryManager() {
        return new EntityManagerFactoryManager();
    }

    @Bean
    @Scope("transaction")
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
    public CategoryServiceImpl categoryServiceImpl(CategoryRepositoryImpl categoryRepository) {
        return new CategoryServiceImpl(categoryRepository);
    }

    @Bean
    public CategoryRepositoryImpl categoryRepositoryImpl(EntityManager entityManager, EntityManagerHelper entityManagerHelper) {
        return new CategoryRepositoryImpl(entityManager, entityManagerHelper);
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
