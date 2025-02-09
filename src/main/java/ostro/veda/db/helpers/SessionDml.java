package ostro.veda.db.helpers;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ostro.veda.db.DbConnection;

import java.util.List;
import java.util.Map;

public class SessionDml {

    public static <T> List<T> findByFields(Session session, Class<T> entityClass, Map<String, String> columnsAndValues) {

        String[] columns = columnsAndValues.keySet().toArray(new String[0]);
        String[] values = columnsAndValues.values().toArray(new String[0]);

        String dml = SqlBuilder.buildDml(entityClass, SqlBuilder.SqlCrudType.SELECT, columns);
        TypedQuery<T> query = session.createQuery(dml, entityClass);

        for (int i = 0; i < values.length; i++) {
            int parameterIndex = i + 1;
            query.setParameter(parameterIndex, values[i]);
        }

        if (!query.getResultList().isEmpty()) {
            return query.getResultList();
        }

        return null;
    }

    public static <T> boolean executePersist(Session session, T entity) {

        if (entity == null) {
            return false;
        }

        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DbConnection.transactionRollBack(transaction);
        }
        return false;
    }
}
