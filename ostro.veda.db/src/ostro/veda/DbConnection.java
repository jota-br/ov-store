package ostro.veda;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

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
        EntityManagerFactory emf = createEntityManagerFactory("ostro.veda.db");
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);

        return sessionFactory.openSession();
    }

    public static void closeSession(Session session) {
        if (session != null && session.isOpen()) {
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
