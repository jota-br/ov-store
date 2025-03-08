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
    exports ostro.veda.util.dto;
    exports ostro.veda.util.error;
    exports ostro.veda.repository.dao;
    exports ostro.veda.repository to spring.beans;
    exports ostro.veda.repository.helpers to spring.beans;
    exports ostro.veda.service.events to spring.context;
    opens ostro.veda.util;
    opens ostro.veda.repository;
    opens ostro.veda.repository.dao to org.hibernate.orm.core;
    opens ostro.veda.repository.helpers to org.hibernate.orm.core;
    opens ostro.veda.repository.helpers.enums to org.hibernate.orm.core;
    opens ostro.veda.config;
}