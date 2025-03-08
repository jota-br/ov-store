package ostro.veda.repository.helpers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EntityManagerHelper {

    public <T> List<T> findByFields(EntityManager em, Class<T> entityClass, Map<String, String> columnsAndValues) {

        log.info("findByFields() = {}", columnsAndValues);
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

        log.info("findByFieldId() = {}", columnsAndValues);
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
}
