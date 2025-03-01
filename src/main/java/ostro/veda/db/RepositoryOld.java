package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;

public abstract class RepositoryOld implements AutoCloseable {

    protected final EntityManager em;
    protected final EntityManagerHelper entityManagerHelper;

    public RepositoryOld() {
        this.em = JPAUtil.getEm();
        this.entityManagerHelper = new EntityManagerHelper();
    }

    public RepositoryOld(EntityManager em) {
        this.em = em;
        this.entityManagerHelper = new EntityManagerHelper();
    }

    public EntityManager getEm() {
        return this.em;
    }

    public EntityManagerHelper getEntityManagerHelper() {
        return this.entityManagerHelper;
    }

    @Override
    public void close() {
        if (this.em != null && this.em.isOpen()) {
            this.em.close();
        }
    }
}
