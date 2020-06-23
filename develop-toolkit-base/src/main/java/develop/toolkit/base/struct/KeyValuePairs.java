package develop.toolkit.base.struct;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 键值对列表
 *
 * @author qiushui on 2018-11-03.
 */
@SuppressWarnings("unused")
public class KeyValuePairs<K, V> extends LinkedList<KeyValuePair<K, V>> {

    private static final long serialVersionUID = -3327179013671312416L;

    /**
     * 添加键值对
     */
    public void addKeyValue(K key, V value) {
        this.add(new KeyValuePair<>(key, value));
    }

    /**
     * 获取所有键
     */
    public List<K> allKey() {
        return this.stream().map(KeyValuePair::getKey).collect(Collectors.toList());
    }

    /**
     * 获取所有值
     */
    public List<V> allValue() {
        return this.stream().map(KeyValuePair::getValue).collect(Collectors.toList());
    }

    /**
     * 转化成Map形式
     */
    public Map<K, V> toMap() {
        Map<K, V> map = new HashMap<>();
        this.forEach(kv -> map.put(kv.getKey(), kv.getValue()));
        return map;
    }

    /**
     * 从Map转化
     */
    public static <K, V> KeyValuePairs<K, V> fromMap(Map<K, V> map) {
        KeyValuePairs<K, V> keyValuePairs = new KeyValuePairs<>();
        map.forEach(keyValuePairs::addKeyValue);
        return keyValuePairs;
    }

    /**
     * 带值初始化
     */
    @SafeVarargs
    public static <K, V> KeyValuePairs<K, V> of(KeyValuePair<K, V>... keyValuePairArray) {
        return of(List.of(keyValuePairArray));
    }

    /**
     * 带值初始化
     */
    public static <K, V> KeyValuePairs<K, V> of(Collection<KeyValuePair<K, V>> keyValuePairCollection) {
        KeyValuePairs<K, V> keyValuePairs = new KeyValuePairs<>();
        keyValuePairs.addAll(keyValuePairCollection);
        return keyValuePairs;
    }
}
