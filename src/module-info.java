module ostro.veda.project {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires junit;
    requires org.mariadb.jdbc;
    requires org.checkerframework.checker.qual;
    requires ostro.veda.Logger;
    requires org.apache.commons.validator;
    requires static lombok;
    requires org.slf4j;

    exports test.resources to junit;
    opens ostro.veda.db.jpa to org.hibernate.orm.core;
    opens ostro.veda.db to org.hibernate.orm.core;
    opens ostro.veda.db.helpers to org.hibernate.orm.core;
    opens ostro.veda.db.helpers.columns to org.hibernate.orm.core;
}