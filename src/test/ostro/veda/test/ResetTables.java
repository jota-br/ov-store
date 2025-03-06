package ostro.veda.test;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ResetTables {

    public static DataSource getDataSource() {

        var dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/");
            dataSource.setUser(System.getenv().getOrDefault("SQL_USER", "root"));
            dataSource.setPassword(System.getenv().getOrDefault("SQL_PASS", "root"));
        } catch (SQLException e) {
            System.err.printf("SQL State: %s%nError Code: %s%nMessage: %s%n", e.getSQLState(), e.getErrorCode(), e.getMessage());
            return null;
        }

        return dataSource;
    }

    public static void resetTables() {
        Path path = Path.of("src/test/resources/resetTables.sql");

        try (Connection con = getDataSource().getConnection(); Statement statement = con.createStatement()) {
            List<String> files = Files.readAllLines(path);
            statement.execute("USE ov_store");
            for (String str : files) {
                statement.execute(str);
            }
        } catch (IOException | SQLException ignored) {
        }
    }
}
