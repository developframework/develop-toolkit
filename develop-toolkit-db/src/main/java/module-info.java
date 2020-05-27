/**
 * @author qiushui on 2019-02-27.
 */
module develop.toolkit.db {
    requires develop.toolkit.base;
    requires expression;
    requires java.net.http;
    requires java.sql;
    requires lombok;
    requires mysql.connector.java;
    requires org.apache.commons.lang3;
    requires mongo.java.driver;

    exports develop.toolkit.db;
    exports develop.toolkit.db.mysql;
}