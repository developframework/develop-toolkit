/**
 * @author qiushui on 2019-02-27.
 */
module develop.toolkit.support {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;
    requires develop.toolkit.base;
    requires expression;
    requires java.net.http;
    requires java.sql;
    requires lombok;
    requires mysql.connector.java;
    requires org.apache.commons.lang3;

    exports develop.toolkit.support.db;
    exports develop.toolkit.support.db.mysql;
    exports develop.toolkit.support.http;
}