package main.java.ostro.veda.db.helpers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Component
@Scope("singleton")
@Deprecated
public class EntityManagerFactoryManager {

    private final EntityManagerFactory entityManagerFactory;

    public EntityManagerFactoryManager() {
        this.entityManagerFactory = createEmf();
    }

    private EntityManagerFactory createEmf() {
        Map<String, Object> properties = getProperties();
        return Persistence.createEntityManagerFactory("ostro.veda.db", properties);
    }

    private Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();

        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mariadb://localhost:3306/ov_store");
        String dbUser = System.getenv().getOrDefault("DB_USERNAME", "root");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "root");

        properties.put("jakarta.persistence.jdbc.driver", "org.mariadb.jdbc.Driver");
        properties.put("jakarta.persistence.jdbc.url", dbUrl);
        properties.put("jakarta.persistence.jdbc.user", dbUser);
        properties.put("jakarta.persistence.jdbc.password", dbPassword);
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", false);
        properties.put("hibernate.use_sql_comments", false);
        return properties;
    }

    @PreDestroy
    public void shutdown() throws Exception {
        log.info("EntityManagerFactoryManager -> shutdown()");
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
