package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;

public abstract class Repository implements AutoCloseable {

    protected final EntityManager em = JPAUtil.getEm();
    protected final EntityManagerHelper entityManagerHelper;

    public Repository(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    public EntityManager getEm() {
        return this.em;
    }

    public void closeEm() {
        JPAUtil.closeEntityManager(this.em);
    }

    @Override
    public void close() {
        if (this.em != null && this.em.isOpen()) {
            this.em.close();
        }
    }
}
