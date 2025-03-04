package ostro.veda.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.ApplicationEventPublisher;
import ostro.veda.db.*;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ostro.veda.service.events.AuditEventListener;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "ostro.veda")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setJdbcUrl(System.getenv().getOrDefault("DB_URL", "jdbc:mariadb://localhost:3306/ov_store"));
        dataSource.setUsername(System.getenv().getOrDefault("DB_USERNAME", "root"));
        dataSource.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "root"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPersistenceUnitName("ostro.veda.db");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", false);
        properties.put("hibernate.use_sql_comments", false);
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public EntityManagerHelper entityManagerHelper() {
        return new EntityManagerHelper();
    }

    @Bean
    public AddressService addressServiceImpl(ApplicationEventPublisher applicationEventPublisher, AddressRepository addressRepository) {
        return new AddressServiceImpl(applicationEventPublisher, addressRepository);
    }

    @Bean
    public AddressRepository addressRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        return new AddressRepositoryImpl(entityManagerHelper);
    }

    @Bean
    public UserService userServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository) {
        return new UserServiceImpl(applicationEventPublisher, userRepository);
    }

    @Bean
    public UserRepository userRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        return new UserRepositoryImpl(entityManagerHelper);
    }

    @Bean
    public CategoryService categoryServiceImpl(ApplicationEventPublisher applicationEventPublisher, CategoryRepository categoryRepository) {
        return new CategoryServiceImpl(applicationEventPublisher, categoryRepository);
    }

    @Bean
    public CategoryRepository categoryRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        return new CategoryRepositoryImpl(entityManagerHelper);
    }

    @Bean
    public ProductService productServiceImpl(ApplicationEventPublisher applicationEventPublisher, ProductRepository productRepository) {
        return new ProductServiceImpl(applicationEventPublisher, productRepository);
    }

    @Bean
    public ProductRepository productRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        return new ProductRepositoryImpl(entityManagerHelper);
    }

    @Bean
    public OrderService orderServiceImpl(ApplicationEventPublisher applicationEventPublisher, OrderRepository orderRepository) {
        return new OrderServiceImpl(applicationEventPublisher, orderRepository);
    }

    @Bean
    public OrderRepository orderRepositoryImpl(EntityManagerHelper entityManagerHelper) {
        return new OrderRepositoryImpl(entityManagerHelper);
    }

    @Bean
    public AuditEventListener auditEventListener(AuditService auditService) {
        return new AuditEventListener(auditService);
    }

    @Bean
    public AuditService auditServiceImpl(AuditRepository auditRepository) {
        return new AuditServiceImpl(auditRepository);
    }

    @Bean
    public AuditRepository auditRepositoryImpl() {
        return new AuditRepositoryImpl();
    }
}