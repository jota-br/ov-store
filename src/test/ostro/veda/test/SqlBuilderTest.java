package ostro.veda.test;

import ostro.veda.repository.helpers.SqlBuilder;
import ostro.veda.repository.dao.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlBuilderTest {

    @Test
    public void buildDml() {
        assertEquals("SELECT c FROM User c WHERE c.username = ?1 AND c.email = ?2",
                SqlBuilder.buildDml(User.class, SqlBuilder.CrudType.SELECT, "username", "email"));
        assertEquals("SELECT c FROM User c WHERE c.username = ?1",
                SqlBuilder.buildDml(User.class, SqlBuilder.CrudType.SELECT, "username"));
    }
}