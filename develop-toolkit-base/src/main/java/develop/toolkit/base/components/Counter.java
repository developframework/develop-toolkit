package develop.toolkit.base.components;

import develop.toolkit.base.struct.KeyValuePairs;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 计数器
 *
 * @author qiushui on 2020-03-25.
 */
@SuppressWarnings("unused")
public class Counter<K> {

    private final ConcurrentHashMap<K, Integer> map = new ConcurrentHashMap<>();

    /**
     * 加
     */
    public void add(K key, final int count) {
        map.compute(key, (k, v) -> v == null ? count : (v + count));
    }

    /**
     * 加1
     */
    public void add(K key) {
        add(key, 1);
    }

    /**
     * 减
     */
    public void subtract(K key, final int count) {
        map.compute(key, (k, v) -> (v == null || v == count) ? 0 : (v - count));
    }

    /**
     * 减1
     */
    public void subtract(K key) {
        subtract(key, 1);
    }

    /**
     * 取值
     */
    public int get(K key) {
        return map.getOrDefault(key, 0);
    }

    /**
     * 获得所有键集合
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * 转化成KeyValuePairs
     */
    public KeyValuePairs<K, Integer> toKeyValuePairs() {
        return KeyValuePairs.fromMap(map);
    }
}
