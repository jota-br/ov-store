package ostro.veda.db.helpers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.mariadb.jdbc.MariaDbDataSource;
import ostro.veda.loggerService.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

    public static EntityManagerFactory entityManagerFactory;

    private static EntityManagerFactory getEmf() {
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
        properties.put("hibernate.show_sql", true);
//        properties.put("hibernate.format_sql", true);
//        properties.put("hibernate.use_sql_comments", true);
        return properties;
    }

    public static EntityManager getEm() {
        return getEmf().createEntityManager();
    }

    public static void closeEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static void transactionRollBack(EntityTransaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    public static Connection getConnection() {

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/");
            dataSource.setUser(System.getenv("SQL_USER"));
            dataSource.setPassword(System.getenv("SQL_PASS"));

            return dataSource.getConnection();
        } catch (SQLException e) {
            Logger.log(e);
        }
        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.log(e);
        }
    }
}
