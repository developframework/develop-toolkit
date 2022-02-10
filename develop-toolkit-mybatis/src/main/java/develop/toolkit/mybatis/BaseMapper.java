package develop.toolkit.mybatis;

import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author qiushui on 2022-02-10.
 */
public interface BaseMapper<T> {

    @SelectProvider(type = BaseMapperMysqlProvider.class, method = "select")
    List<T> select(Object search);
}
