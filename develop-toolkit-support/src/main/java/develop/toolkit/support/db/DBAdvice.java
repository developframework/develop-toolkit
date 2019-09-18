package develop.toolkit.support.db;

import develop.toolkit.support.db.mysql.MysqlClient;
import develop.toolkit.support.db.mysql.MysqlProperties;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author qiushui on 2019-09-03.
 */
public final class DBAdvice {

    public static MysqlClient mysql(String domain, String username, String password, String database, Map<String, String> parameters) throws SQLException {
        return mysql(domain, 3306, username, password, database, parameters);
    }

    public static MysqlClient mysql(String domain, String username, String password, String database) throws SQLException {
        return mysql(domain, 3306, username, password, database, null);
    }

    public static MysqlClient mysql(String domain, int port, String username, String password, String database, Map<String, String> parameters) throws SQLException {
        MysqlProperties mysqlProperties = new MysqlProperties(domain, port, username, password, database);
        if (parameters != null) {
            mysqlProperties.getParameters().putAll(parameters);
        }
        return new MysqlClient(mysqlProperties);
    }
}
