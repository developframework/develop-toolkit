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

    exports develop.toolkit.support.db;
}