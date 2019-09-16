package develop.toolkit.support.db;

import develop.toolkit.support.db.mysql.MysqlClient;
import develop.toolkit.support.db.mysql.MysqlProperties;

import java.sql.SQLException;

/**
 * @author qiushui on 2019-09-03.
 */
public final class DBAdvice {

    public static MysqlClient mysql(String domain, String username, String password, String database) throws SQLException {
        return mysql(domain, 3306, username, password, database);
    }

    public static MysqlClient mysql(String domain, int port, String username, String password, String database) throws SQLException {
        return new MysqlClient(
                new MysqlProperties(domain, port, username, password, database)
        );
    }
}
