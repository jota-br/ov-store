package ostro.veda.db;

import jakarta.persistence.EntityManager;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.helpers.JPAUtil;

public abstract class Repository {

    protected final EntityManager em = JPAUtil.getEm();
    protected final EntityManagerHelper entityManagerHelper;

    public Repository(EntityManagerHelper entityManagerHelper) {
        this.entityManagerHelper = entityManagerHelper;
    }

    public void closeEm() {
        JPAUtil.closeEntityManager(this.em);
    }
}
