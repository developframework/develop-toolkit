package develop.toolkit.base.struct;

import lombok.*;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return key + " -> " + value;
    }
}
