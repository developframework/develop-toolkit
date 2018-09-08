package develop.toolkit.struct;

import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 实体链
 *
 * @author qiushui on 2018-05-29.
 * @since 0.1
 * @param <T> 实体类型
 * @param <E> 提取条件类型
 */
public abstract class EntityChain<T extends Extractable<E>, E> implements Serializable{

    private static final long serialVersionUID = -4873465907902104664L;
    @Getter
    protected List<T> chain;

    public EntityChain() {
        this.chain = new LinkedList<>();
        T[] defaultEntities = defaultEntity();
        if(defaultEntities != null) {
            Arrays.stream(defaultEntities).forEach(chain::add);
        }
    }

    /**
     * 提供默认实体
     * @return 默认实体数组
     */
    protected abstract T[] defaultEntity();

    /**
     * 添加自定义实体
     *
     * @param customEntities 自定义实体
     */
    public void addCustomEntities(@NonNull T[] customEntities) {
        chain.addAll(Arrays.asList(customEntities));
    }

    /**
     * 提取
     * @param parameter 条件参数
     * @return 实体
     */
    public Optional<T> extract(E parameter) {
        for (T entity : chain) {
            if (entity.accept(parameter)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    /**
     * 强制提取
     * @param parameter 条件参数
     * @param customRuntimeException 如果实体不存在于链则抛出自定义异常
     * @return 实体
     */
    public T extractRequired(E parameter, RuntimeException customRuntimeException) {
        for (T entity : chain) {
            if (entity.accept(parameter)) {
                return entity;
            }
        }
        throw customRuntimeException;
    }

    /**
     * 提取，失败使用默认值
     * @param parameter 条件参数
     * @param defaultValue 默认值
     * @return 实体
     */
    public T extractOrDefault(E parameter, T defaultValue) {
        for (T entity : chain) {
            if (entity.accept(parameter)) {
                return entity;
            }
        }
        return defaultValue;
    }
}
