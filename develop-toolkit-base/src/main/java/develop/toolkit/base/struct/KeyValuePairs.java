package develop.toolkit.base.struct;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 键值对列表
 *
 * @author qiushui on 2018-11-03.
 */
public class KeyValuePairs<K, V> extends LinkedList<KeyValuePair<K, V>> {

    /**
     * 添加键值对
     *
     * @param key
     * @param value
     */
    public void addKeyValue(K key, V value) {
        this.add(new KeyValuePair<>(key, value));
    }

    /**
     * 获取所有键
     *
     * @return
     */
    public List<K> allKey() {
        return this.stream().map(KeyValuePair::getKey).collect(Collectors.toList());
    }

    /**
     * 获取所有值
     *
     * @return
     */
    public List<V> allValue() {
        return this.stream().map(KeyValuePair::getValue).collect(Collectors.toList());
    }

    /**
     * 转化成Map形式
     *
     * @return
     */
    public Map<K, V> toMap() {
        Map<K, V> map = new HashMap<>();
        this.forEach(kv -> map.put(kv.getKey(), kv.getValue()));
        return map;
    }

    /**
     * 从Map转化
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> KeyValuePairs<K, V> fromMap(Map<K, V> map) {
        KeyValuePairs<K, V> keyValuePairs = new KeyValuePairs<>();
        map.forEach(keyValuePairs::addKeyValue);
        return keyValuePairs;
    }

    /**
     * 带值初始化
     *
     * @param keyValuePairArray
     * @param <K>
     * @param <V>
     * @return
     */
    @SafeVarargs
    public static <K, V> KeyValuePairs<K, V> of(KeyValuePair<K, V>... keyValuePairArray) {
        KeyValuePairs<K, V> keyValuePairs = new KeyValuePairs<>();
        keyValuePairs.addAll(List.of(keyValuePairArray));
        return keyValuePairs;
    }
}
