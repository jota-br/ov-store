package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;

public abstract class Repository implements AutoCloseable {

    protected final EntityManager em;
    protected final EntityManagerHelper entityManagerHelper;

    public Repository(EntityManager em, EntityManagerHelper entityManagerHelper) {
        this.em = em == null ? JPAUtil.getEm() : em;
        this.entityManagerHelper = entityManagerHelper;
    }

    public EntityManager getEm() {
        return this.em;
    }

    @Override
    public void close() {
        if (this.em != null && this.em.isOpen()) {
            this.em.close();
        }
    }
}
