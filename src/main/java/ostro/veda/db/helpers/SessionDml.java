package ostro.veda.db.helpers;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;

public class SessionDml {

    public static <T> T findByUsername(Session session, Class<T> entityClass, String field, String username) {

        String dml = SqlBuilder.buildDml(entityClass, SqlBuilder.SqlCrudType.SELECT, field);
        TypedQuery<T> query = session.createQuery(dml, entityClass);
        query.setParameter(1, username);

        if (!query.getResultList().isEmpty()) {
            return query.getSingleResult();
        }

        return null;
    }
}
