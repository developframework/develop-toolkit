package develop.toolkit.db.mysql;

import com.github.developframework.expression.ExpressionUtils;
import develop.toolkit.base.utils.JavaBeanUtils;
import develop.toolkit.base.utils.K;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mysql客户端
 *
 * @author qiushui on 2019-09-03.
 */
@SuppressWarnings("unused")
public class MysqlClient implements AutoCloseable {

    @Getter
    private final Connection connection;

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
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws SQLException {
        return query(sql, null, rowMapper);
    }

    /**
     * 查询列表
     */
    public <T> List<T> query(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(sql);
        if (setter != null) {
            setter.set(statement);
        }
        final ResultSet resultSet = statement.executeQuery();
        try (statement; resultSet) {
            List<T> list = new LinkedList<>();
            while (resultSet.next()) {
                list.add(rowMapper.mapping(resultSet));
            }
            return Collections.unmodifiableList(list);
        }
    }

    /**
     * 查询单记录
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

    public <T> int insertAll(String table, Collection<T> collection, String... fields) throws SQLException {
        String sql = new StringBuilder()
                .append("INSERT INTO ").append(table).append("(")
                .append(Stream.of(fields).map(f -> String.format("`%s`", f)).collect(Collectors.joining(",")))
                .append(") VALUES")
                .append(
                        collection
                                .stream()
                                .map(data ->
                                        Stream.of(fields)
                                                .map(field -> {
                                                    String value = K.map(
                                                            ExpressionUtils.getValue(data, JavaBeanUtils.underlineToCamelcase(field)),
                                                            Object::toString
                                                    );
                                                    return StringUtils.isNumeric(value) ? value : ("'" + value + "'");
                                                })
                                                .collect(Collectors.joining(",", "(", ")"))
                                )
                                .collect(Collectors.joining(","))
                )
                .toString();
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }

    /**
     * 插入记录
     */
    public <T> int insert(String table, T data, String... fields) throws SQLException {
        return insertAll(table, List.of(data), fields);
    }

    /**
     * 执行修改语句
     */
    public int executeUpdate(String sql, PreparedStatementSetter setter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (setter != null) {
                setter.set(preparedStatement);
            }
            return preparedStatement.executeUpdate();
        }
    }

    /**
     * 执行修改语句
     */
    public int executeUpdate(String sql) throws SQLException {
        return executeUpdate(sql, null);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
