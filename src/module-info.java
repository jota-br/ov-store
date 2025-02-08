module ostro.veda.project {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires junit;
    requires org.mariadb.jdbc;

    exports test to junit;
    opens main.java.ostro.veda.db.jpa to org.hibernate.orm.core;
}