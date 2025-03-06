package ostro.veda.db.helpers;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Deprecated
public class JpaHelper {

    public static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory getEmf() {
        if (entityManagerFactory == null) {
            Map<String, Object> properties = getProperties();
            entityManagerFactory = Persistence.createEntityManagerFactory("ostro.veda.db", properties);
        }
        return entityManagerFactory;
    }

    private static Map<String, Object> getProperties() {
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

    public static EntityManager getEm() {
        return getEmf().createEntityManager();
    }

    @PreDestroy
    public void shutdown() throws Exception {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
