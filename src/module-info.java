module ostro.veda.project {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires junit;
    requires org.mariadb.jdbc;

    exports test.resources to junit;
    opens ostro.veda.db.jpa to org.hibernate.orm.core;
}