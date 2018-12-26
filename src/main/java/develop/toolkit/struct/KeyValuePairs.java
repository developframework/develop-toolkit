package develop.toolkit.struct;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 键值对列表
 *
 * @author qiushui on 2018-11-03.
 */
public class KeyValuePairs<K, V> extends LinkedList<KeyValuePair<K, V>> {

    public void addKeyValue(K key, V value) {
        this.add(new KeyValuePair<>(key, value));
    }

    public List<K> allKey() {
        return this.stream().map(KeyValuePair::getKey).collect(Collectors.toList());
    }

    public List<V> allValue() {
        return this.stream().map(KeyValuePair::getValue).collect(Collectors.toList());
    }
}
