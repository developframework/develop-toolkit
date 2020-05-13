/**
 * @author qiushui on 2019-02-27.
 */
module develop.toolkit.support {
    requires develop.toolkit.base;
    requires expression;
    requires java.net.http;
    requires java.sql;
    requires lombok;
    requires mysql.connector.java;
    requires org.apache.commons.lang3;

    exports develop.toolkit.support.db;
    exports develop.toolkit.support.db.mysql;
}