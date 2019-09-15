package develop.toolkit.support.db.mysql;

import java.sql.PreparedStatement;

/**
 * @author qiushui on 2019-09-03.
 */
public interface PreparedStatementSetter {

    void set(PreparedStatement preparedStatement);
}
