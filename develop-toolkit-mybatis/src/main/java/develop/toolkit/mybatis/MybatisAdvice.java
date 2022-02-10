package develop.toolkit.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author qiushui on 2021-06-22.
 */
public abstract class MybatisAdvice {

    /**
     * 构建SqlSessionFactory
     *
     * @param configurationHandler 配置处理接口
     */
    public static SqlSessionFactory buildSqlSessionFactory(ConfigurationHandler configurationHandler) {
        final Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setLogPrefix("mybatis.");
        final HikariConfig hikariConfig = new HikariConfig();
        configurationHandler.configHikari(hikariConfig);
        final DataSource dataSource = new HikariDataSource(hikariConfig);
        final TransactionFactory transactionFactory = new JdbcTransactionFactory();
        configuration.setEnvironment(new Environment("default", transactionFactory, dataSource));
        configurationHandler.configMapperRegistry(configuration.getMapperRegistry());
        configurationHandler.configTypeAliasRegistry(configuration.getTypeAliasRegistry());
        configurationHandler.configInterceptors(configuration.getInterceptors());

        for (MappedStatement mappedStatement : configuration.getMappedStatements()) {
            try {
                SimpleMapperHelper.changeMs(mappedStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
