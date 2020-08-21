package develop.toolkit.base.struct;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Map里有列表结构
 *
 * @author qiushui on 2020-08-13.
 */
@SuppressWarnings("unused")
public class ListInMap<K, V> extends AbstractCollectionInMap<K, V, List<V>> {

    private static final long serialVersionUID = -6928970809459612701L;

    public ListInMap() {
        super(LinkedList::new);
    }

    public ListInMap(Supplier<List<V>> supplier) {
        super(supplier);
    }
}
