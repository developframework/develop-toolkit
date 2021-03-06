package develop.toolkit.base.struct;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * 键值对结构体
 *
 * @author qiushui on 2018-05-24.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "key")
@NoArgsConstructor
@AllArgsConstructor
public class KeyValuePair<K, V> implements Serializable {

    private static final long serialVersionUID = -6101907039622686690L;

    protected K key;

    protected V value;

    /**
     * 美化成字符串
     */
    public String formatString(String separator) {
        return key + separator + value;
    }

    @Override
    public String toString() {
        return formatString(":");
    }

    /**
     * 带值初始化
     */
    public static <K, V> KeyValuePair<K, V> of(K key, V value) {
        return new KeyValuePair<>(key, value);
    }

    public static <T> KeyValuePair<T, T> of(T[] objs) {
        return new KeyValuePair<>(objs[0], objs[1]);
    }

    /**
     * 从Entry初始化
     */
    public static <K, V> KeyValuePair<K, V> of(Map.Entry<K, V> entry) {
        return new KeyValuePair<>(entry.getKey(), entry.getValue());
    }

}
