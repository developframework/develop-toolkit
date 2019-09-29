/**
 * @author qiushui on 2019-02-27.
 */
module develop.toolkit.support {
    requires lombok;
    requires develop.toolkit.base;
    requires org.apache.commons.lang3;
    requires java.sql;
    requires mysql.connector.java;
    requires expression;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    exports develop.toolkit.support.db;
    exports develop.toolkit.support.db.mysql;
}