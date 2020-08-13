package develop.toolkit.base.struct;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Map里有集合结构
 *
 * @author qiushui on 2020-08-13.
 */
@SuppressWarnings("unused")
public class SetInMap<K, V> extends AbstractCollectionInMap<K, V, Set<V>> {

    private static final long serialVersionUID = 3068493190714636107L;

    public SetInMap() {
        super(HashSet::new);
    }

    public SetInMap(Supplier<Set<V>> supplier) {
        super(supplier);
    }
}
