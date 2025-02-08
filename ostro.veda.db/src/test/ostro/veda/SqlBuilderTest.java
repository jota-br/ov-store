package test.ostro.veda;

import org.junit.jupiter.api.Test;
import ostro.veda.helpers.SqlBuilder;
import ostro.veda.jpa.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlBuilderTest {

    @Test
    void buildDml() {
        assertEquals("SELECT c FROM User c WHERE c.username = ?1 AND c.email = ?2",
                SqlBuilder.buildDml(User.class, SqlBuilder.SqlCrudType.SELECT, "username", "email"));
        assertEquals("SELECT c FROM User c WHERE c.username = ?1",
                SqlBuilder.buildDml(User.class, SqlBuilder.SqlCrudType.SELECT, "username"));
    }
}