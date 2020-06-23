package develop.toolkit.base.components;

import lombok.NonNull;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 实体注册器
 *
 * @author qiushui on 2018-05-29.
 */
@SuppressWarnings("unused")
public abstract class EntityRegistry<T extends EntitySign<K>, K> implements Serializable {

    private static final long serialVersionUID = 8580818076321536793L;

    protected Map<K, T> entityMap;

    public EntityRegistry() {
        this.entityMap = new LinkedHashMap<>();
        T[] defaultEntities = defaultEntity();
        if (defaultEntities != null) {
            for (T defaultEntity : defaultEntities) {
                entityMap.put(defaultEntity.key(), defaultEntity);
            }
        }
    }

    /**
     * 提供默认实体
     *
     * @return 默认实体数组
     */
    protected abstract T[] defaultEntity();

    /**
     * 添加自定义实体
     *
     * @param customEntities 自定义实体
     */
    public final void addCustomEntities(@NonNull T[] customEntities) {
        if (customEntities != null) {
            for (T entity : customEntities) {
                entityMap.put(entity.key(), entity);
            }
        }
    }

    /**
     * 提取
     *
     * @param key 标记
     * @return 实体
     */
    public final Optional<T> extract(K key) {
        if (entityMap.containsKey(key)) {
            return Optional.of(entityMap.get(key));
        }
        return Optional.empty();
    }

    /**
     * 强制提取
     *
     * @param key                    标记
     * @param customRuntimeException 如果实体不存在于注册器则抛出自定义异常
     * @return 实体
     */
    public final T extractRequired(K key, RuntimeException customRuntimeException) {
        if (entityMap.containsKey(key)) {
            return entityMap.get(key);
        }
        throw customRuntimeException;
    }

    /**
     * 提取，失败使用默认值
     *
     * @param key          标记
     * @param defaultValue 默认值
     * @return 实体
     */
    public final T extractOrDefault(K key, T defaultValue) {
        if (entityMap.containsKey(key)) {
            return entityMap.get(key);
        }
        return defaultValue;
    }
}
