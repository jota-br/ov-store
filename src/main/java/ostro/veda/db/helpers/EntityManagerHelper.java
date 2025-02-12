package ostro.veda.db.helpers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;

public class EntityManagerHelper {

    public <T> List<T> findByFields(EntityManager em, Class<T> entityClass, Map<String, String> columnsAndValues) {

        String[] columns = columnsAndValues.keySet().toArray(new String[0]);
        String[] values = columnsAndValues.values().toArray(new String[0]);

        String dml = SqlBuilder.buildDml(entityClass, SqlBuilder.SqlCrudType.SELECT, columns);
        TypedQuery<T> query = em.createQuery(dml, entityClass);

        for (int i = 0; i < values.length; i++) {
            int parameterIndex = i + 1;
            query.setParameter(parameterIndex, values[i]);
        }

        if (!query.getResultList().isEmpty()) {
            return query.getResultList();
        }

        return null;
    }

    public <T> boolean executePersist(EntityManager em, T entity) {

        if (entity == null) {
            return false;
        }

        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JPAUtil.transactionRollBack(transaction);
        }
        return false;
    }

    public <T> boolean executeMerge(EntityManager em, T entity) {

        if (entity == null) {
            return false;
        }

        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JPAUtil.transactionRollBack(transaction);
        }
        return false;
    }
}
