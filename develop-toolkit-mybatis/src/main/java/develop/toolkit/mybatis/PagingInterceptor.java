package develop.toolkit.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

/**
 * @author qiushui on 2021-06-18.
 */
@Intercepts(@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
))
@SuppressWarnings("unchecked")
public class PagingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) args[0];
        final Object parameterObject = args[1];
//        final RowBounds rowBounds = (RowBounds) args[2];

        final Optional<MybatisPager> mybatisPagerOptional = extractPager(parameterObject);
        if (mybatisPagerOptional.isEmpty()) {
            //不需要分页，直接返回结果
            return invocation.proceed();
        }
        final ResultHandler<?> resultHandler = (ResultHandler<?>) args[3];
        final MybatisPager mybatisPager = mybatisPagerOptional.get();
        final Executor executor = (Executor) invocation.getTarget();
        final BoundSql boundSql = ms.getBoundSql(parameterObject);
        final Map<String, Object> additionalParameters = getAdditionalParameters(boundSql);
        final long count = queryCount(executor, ms, boundSql, parameterObject, additionalParameters, resultHandler);
        if (count == 0) {
            return new ArrayList<>();
        }
        return queryList(executor, ms, boundSql, parameterObject, additionalParameters, resultHandler, mybatisPager);
    }

    /**
     * 查询总条数
     */
    private long queryCount(Executor executor, MappedStatement ms, BoundSql boundSql, Object parameterObject, Map<String, Object> additionalParameters, ResultHandler<?> resultHandler) throws SQLException {
        MappedStatement countMs = newMappedStatement(ms);
        CacheKey countKey = executor.createCacheKey(countMs, parameterObject, RowBounds.DEFAULT, boundSql);
        String countSql = String.format("SELECT COUNT(*) FROM (%s) total", boundSql.getSql());
        BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
        additionalParameters.forEach(countBoundSql::setAdditionalParameter);
        List<Object> countQueryResult = executor.query(countMs, parameterObject, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        return (Long) countQueryResult.get(0);
    }

    /**
     * 查询列表
     */
    private List<Object> queryList(Executor executor, MappedStatement ms, BoundSql boundSql, Object parameterObject, Map<String, Object> additionalParameters, ResultHandler<?> resultHandler, MybatisPager pager) throws SQLException {
        CacheKey pageKey = executor.createCacheKey(ms, parameterObject, RowBounds.DEFAULT, boundSql);
        String pageSql = boundSql.getSql() + " " + pager.limitSQL();
        BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameterObject);
        additionalParameters.forEach(pageBoundSql::setAdditionalParameter);
        return executor.query(ms, parameterObject, RowBounds.DEFAULT, resultHandler, pageKey, pageBoundSql);
    }

    private MappedStatement newMappedStatement(MappedStatement ms) {
        return new MappedStatement.Builder(
                ms.getConfiguration(),
                ms.getId() + "_count",
                ms.getSqlSource(),
                ms.getSqlCommandType()
        )
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .timeout(ms.getTimeout())
                .parameterMap(ms.getParameterMap())
                .resultSetType(ms.getResultSetType())
                .cache(ms.getCache())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache())
                .resultMaps(
                        Collections.singletonList(
                                new ResultMap.Builder(
                                        ms.getConfiguration(),
                                        ms.getId(),
                                        Long.class,
                                        Collections.emptyList()
                                ).build()
                        )
                )
                .keyProperty(StringUtils.join(ms.getKeyProperties(), ","))
                .build();
    }

    private Optional<MybatisPager> extractPager(Object parameterObject) {
        if (parameterObject instanceof MybatisPager) {
            return Optional.of((MybatisPager) parameterObject);
        } else if (parameterObject instanceof Map) {
            return ((Map<String, Object>) parameterObject)
                    .values()
                    .stream()
                    .filter(v -> v instanceof MybatisPager)
                    .map(v -> (MybatisPager) v)
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    private Map<String, Object> getAdditionalParameters(BoundSql boundSql) throws NoSuchFieldException, IllegalAccessException {
        Field additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
        additionalParametersField.setAccessible(true);
        return (Map<String, Object>) additionalParametersField.get(boundSql);
    }
}
