module ostro.veda.project {
    requires jakarta.persistence;
    requires junit;
    requires org.mariadb.jdbc;
    requires org.checkerframework.checker.qual;
    requires org.apache.commons.validator;
    requires static lombok;
    requires spring.context;
    requires spring.beans;
    requires java.annotation;
    requires jakarta.transaction;
    requires spring.tx;
    requires spring.orm;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires org.hibernate.orm.core;
    requires jdk.jfr;

    exports test.ostro.veda.test to junit;
    exports ostro.veda.common.dto;
    exports ostro.veda.common.error;
    exports ostro.veda.db.jpa;
    exports ostro.veda.db to spring.beans;
    exports ostro.veda.db.helpers to spring.beans;
    exports ostro.veda.service.events to spring.context;
    opens ostro.veda.common;
    opens ostro.veda.db;
    opens ostro.veda.db.jpa to org.hibernate.orm.core;
    opens ostro.veda.db.helpers to org.hibernate.orm.core;
    opens ostro.veda.db.helpers.database to org.hibernate.orm.core;
    opens ostro.veda.config;
}