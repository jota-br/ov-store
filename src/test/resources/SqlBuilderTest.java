package test.resources;

import org.junit.Test;
import ostro.veda.db.helpers.SqlBuilder;
import ostro.veda.db.jpa.User;

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