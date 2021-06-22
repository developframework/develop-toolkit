package develop.toolkit.mybatis;

import com.zaxxer.hikari.HikariConfig;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeAliasRegistry;

import java.util.List;

/**
 * @author qiushui on 2021-06-22.
 */
@FunctionalInterface
public interface ConfigurationHandler {

    void configHikari(HikariConfig config);

    /**
     * 配置Mapper
     *
     * @param mapperRegistry mapper注册器
     */
    default void configMapperRegistry(MapperRegistry mapperRegistry) {

    }

    /**
     * 配置别名
     *
     * @param typeAliasRegistry 别名注册器
     */
    default void configTypeAliasRegistry(TypeAliasRegistry typeAliasRegistry) {

    }

    /**
     * 配置拦截器
     *
     * @param interceptors 拦截器链
     */
    default void configInterceptors(List<Interceptor> interceptors) {

    }

    /**
     * 其它配置
     */
    default void configurationSettings(Configuration configuration) {

    }
}
