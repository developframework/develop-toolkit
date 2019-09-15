package develop.toolkit.support.db.mysql;

import java.sql.ResultSet;

/**
 * @author qiushui on 2019-09-03.
 */
public interface RowMapper<T> {

    T mapping(ResultSet resultSet);
}
