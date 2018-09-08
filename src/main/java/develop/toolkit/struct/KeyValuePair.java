package develop.toolkit.struct;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 键值对结构体
 *
 * @author qiushui on 2018-05-24.
 * @version 0.1
 */
@Getter
@Setter
public class KeyValuePair<K, V> implements Serializable {

    private static final long serialVersionUID = -6101907039622686690L;

    protected K key;

    protected V value;

    public KeyValuePair() {
    }

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + " -> " + value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if(key != null) {
            hash = hash * 31 + key.hashCode();
        }
        if(value != null) {
            hash = hash * 31 + value.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof KeyValuePair) {
            KeyValuePair otherKeyValuePair = (KeyValuePair) obj;
            if((key == null && otherKeyValuePair.getKey() == null) || key.equals(otherKeyValuePair.getKey())) {
                Object otherValue = otherKeyValuePair.getValue();
                if(value == null && otherValue != null) {
                    return false;
                }
                return value == otherValue || value.equals(otherValue);
            }
        }
        return false;
    }
}
