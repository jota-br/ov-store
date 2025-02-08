package ostro.veda.db;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbConnection {

    public static Connection getConnection() {

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/");
            dataSource.setUser(System.getenv("SQL_USER"));
            dataSource.setPassword(System.getenv("SQL_PASS"));

            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Session getOpenSession() {
        Map<String, Object> properties = new HashMap<>();
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mariadb://localhost:3306/ov_store");
        String dbUser = System.getenv().getOrDefault("DB_USERNAME", "root");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "root");

        properties.put("javax.persistence.jdbc.driver", "org.mariadb.jdbc.Driver");
        properties.put("javax.persistence.jdbc.url", dbUrl);
        properties.put("javax.persistence.jdbc.user", dbUser);
        properties.put("javax.persistence.jdbc.password", dbPassword);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ostro.veda.db", properties);
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);

        return sessionFactory.openSession();
    }

    public static void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            session.getEntityManagerFactory().close();
            session.close();
        }
    }

    public static Transaction getTransaction(Session session) {
        return session.getTransaction();
    }

    public static void transactionRollBack(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
