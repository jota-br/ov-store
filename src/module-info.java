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
    requires spring.context;
    requires spring.beans;
    requires java.annotation;
    requires jakarta.transaction;

    exports test.ostro.veda.test to junit;
    exports ostro.veda.db to spring.beans;
    exports ostro.veda.db.helpers to spring.beans;
    opens ostro.veda.db.jpa to org.hibernate.orm.core;
    opens ostro.veda.db to org.hibernate.orm.core;
    opens ostro.veda.db.helpers to org.hibernate.orm.core;
    opens ostro.veda.db.helpers.columns to org.hibernate.orm.core;
    opens ostro.veda.config;
    exports ostro.veda.common.dto to ostro.veda.db;
    exports ostro.veda.db.jpa to ostro.veda.db;
}