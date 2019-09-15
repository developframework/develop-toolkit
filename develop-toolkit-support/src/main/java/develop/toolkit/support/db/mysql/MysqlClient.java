package develop.toolkit.support.db.mysql;

import com.github.developframework.expression.ExpressionUtils;
import develop.toolkit.base.utils.JavaBeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiushui on 2019-09-03.
 */
public class MysqlClient {

    private Connection connection;

    public MysqlClient(MysqlProperties mysqlProperties) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    mysqlProperties.getUrl(),
                    mysqlProperties.getUsername(),
                    mysqlProperties.getPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询列表
     *
     * @param sql
     * @param rowMapper
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<T> list = new LinkedList<>();
        while (resultSet.next()) {
            list.add(rowMapper.mapping(resultSet));
        }
        resultSet.close();
        statement.close();
        return list;
    }

    /**
     * 查询列表
     *
     * @param sql
     * @param setter
     * @param rowMapper
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> List<T> query(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        setter.set(statement);
        ResultSet resultSet = statement.executeQuery();
        List<T> list = new LinkedList<>();
        while (resultSet.next()) {
            list.add(rowMapper.mapping(resultSet));
        }
        resultSet.close();
        statement.close();
        return list;
    }

    /**
     * 查询单记录
     *
     * @param sql
     * @param rowMapper
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Optional<T> queryOne(String sql, RowMapper<T> rowMapper) throws SQLException {
        List<T> list = query(sql, rowMapper);
        if (list.size() > 1) {
            throw new NoUniqueResultException(list.size());
        }
        return Optional.ofNullable(
                list.isEmpty() ? null : list.get(0)
        );
    }

    /**
     * 查询单记录
     *
     * @param sql
     * @param setter
     * @param rowMapper
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Optional<T> queryOne(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) throws SQLException {
        List<T> list = query(sql, setter, rowMapper);
        if (list.size() > 1) {
            throw new NoUniqueResultException(list.size());
        }
        return Optional.ofNullable(
                list.isEmpty() ? null : list.get(0)
        );
    }

    public <T> int insert(String table, List<T> list, String... fields) throws SQLException {
        String sql = new StringBuilder()
                .append("INSERT INTO ").append(table).append("(")
                .append(StringUtils.join(fields, ",")).append(") VALUES")
                .append(
                        list
                                .stream()
                                .map(data ->
                                        "(" + Stream.of(fields)
                                                .map(field -> {
                                                    Object object = ExpressionUtils.getValue(data, JavaBeanUtils.underlineToCamelcase(field));
                                                    String value = object != null ? object.toString() : null;
                                                    return StringUtils.isNumeric(value) ? value : ("'" + value + "'");
                                                })
                                                .collect(Collectors.joining(",")) + ")"
                                )
                                .collect(Collectors.joining(","))
                )
                .toString();
        Statement statement = connection.createStatement();
        int count = statement.executeUpdate(sql);
        statement.close();
        return count;
    }

    /**
     * 插入记录
     *
     * @param table
     * @param data
     * @param fields
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> int insert(String table, T data, String... fields) throws SQLException {
        return insert(table, List.of(data), fields);
    }
}
