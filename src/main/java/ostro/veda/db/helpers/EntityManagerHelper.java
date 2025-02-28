package ostro.veda.db.helpers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class EntityManagerHelper {

    private static EntityManagerHelper entityManagerHelper;

    public static EntityManagerHelper getEntityManagerHelper() {
        if (entityManagerHelper == null) {
            entityManagerHelper = new EntityManagerHelper();
        }
        return entityManagerHelper;
    }

    public <T> List<T> findByFields(EntityManager em, Class<T> entityClass, Map<String, String> columnsAndValues) {

        String[] columns = columnsAndValues.keySet().toArray(new String[0]);
        String[] values = columnsAndValues.values().toArray(new String[0]);

        String dml = SqlBuilder.buildDml(entityClass, SqlBuilder.CrudType.SELECT, columns);
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

    public <T> List<T> findByFieldId(EntityManager em, Class<T> entityClass, Map<String, Integer> columnsAndValues) {

        String[] columns = columnsAndValues.keySet().toArray(new String[0]);
        Integer[] values = columnsAndValues.values().toArray(new Integer[0]);

        String dml = SqlBuilder.buildDml(entityClass, SqlBuilder.CrudType.SELECT, columns);
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
            log.warn(e.getMessage());
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
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return false;
    }

    public <T> List<T> executePersistBatch(EntityManager em, List<T> entities) {

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        List<T> list = new ArrayList<>();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            for (T entity : entities) {
                em.persist(entity);
                list.add(entity);
            }
            transaction.commit();
            return list;
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }

    public <T> List<T> executeMergeBatch(EntityManager em, List<T> entities) {

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        List<T> list = new ArrayList<>();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            for (T entity : entities) {
                em.merge(entity);
                list.add(entity);
            }
            transaction.commit();
            return list;
        } catch (Exception e) {
            log.warn(e.getMessage());
            JPAUtil.transactionRollBack(transaction);
        }
        return null;
    }
}
