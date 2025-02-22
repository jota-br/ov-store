package test.resources;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ostro.veda.db.helpers.JPAUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResetTables {

    public static void resetTables() {
        Path path = Path.of("src/test/resources/resetTables.sql");
        EntityManager em = JPAUtil.getEm();
        try {
            List<String> files = Files.readAllLines(path);
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            for (String str : files) {
                em.createNativeQuery(str).executeUpdate();
            }
            transaction.commit();
        } catch (IOException ignored) {
        }
    }
}
