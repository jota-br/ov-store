package ostro.veda.db.helpers;

public class SqlBuilder {

    public static enum CrudType {

        SELECT("SELECT"),
        INSERT("INSERT INTO"),
        UPDATE("UPDATE"),
        DELETE("DELETE");

        private final String crud;

        CrudType(String crud) {
            this.crud = crud;
        }

        public String getCrud() {
            return this.crud;
        }
    }

    private static enum SqlKeywords {

        FROM("FROM"),
        WHERE("WHERE"),
        AND("AND"),
        LIKE("LIKE"),
        JOIN("JOIN"),
        GROUP_BY("GROUP BY"),
        ORDER_BY("ORDER BY"),
        LIMIT("LIMIT");

        private final String keyword;

        SqlKeywords(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return this.keyword;
        }
    }

    public static <T> String buildDml(Class<T> entityClass, CrudType dml, String... columns) {

        switch (dml) {
            case SELECT -> {
                return buildSelect(entityClass, dml, columns);
            }
//            case INSERT -> buildInsert(entityClass, crud, columns);
//            case UPDATE -> buildUpdate(entityClass, crud, columns);
//            case DELETE -> buildDelete(entityClass, crud, columns);
        }
        return null;
    }

    private static <T> String buildSelect(Class<T> entityClass, CrudType dml, String... columns) {
        var ec = entityClass.getSimpleName();

        StringBuilder sb = new StringBuilder();
        sb.append(dml.getCrud()).append(" c "); // SELECT c
        sb.append(SqlKeywords.FROM.getKeyword()).append(" ").append(ec).append(" c "); // FROM ec c

        int size = columns.length;
        if (size > 0) {
            sb.append(SqlKeywords.WHERE.getKeyword()).append(" "); // WHERE
            // c.field = input;
            int i = 0;
            for (String column : columns) {
                sb.append("c.").append(column).append(" = ?").append(i+1); // c.Column = ?
                if (i < size - 1) {
                    sb.append(" ").append(SqlKeywords.AND.getKeyword()).append(" "); // AND
                }
                i++;
            }
        }
        return sb.toString();
    }
}
