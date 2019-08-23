/**
 * @author qiushui on 2019-02-27.
 */
module develop.toolkit.support {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires develop.toolkit.base;
    requires java.persistence;
    requires org.apache.commons.lang3;
    requires spring.data.commons;
    requires spring.core;
    requires mongo.java.driver;
    requires spring.data.mongodb;

    exports develop.toolkit.support.jpa.converter;
    exports develop.toolkit.support.mongo.converter;
    exports develop.toolkit.support.mongo.utils;
}