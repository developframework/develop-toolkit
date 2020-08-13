package develop.toolkit.base.struct;

import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Map里有集合结构
 *
 * @author qiushui on 2018-07-09.
 */
@SuppressWarnings("unused")
@Deprecated
public class CollectionInMap<K, V> extends LinkedHashMap<K, Collection<V>> {

	private static final long serialVersionUID = 3068493190714636107L;
	private final Supplier<Collection<V>> supplier;

	public CollectionInMap() {
		this.supplier = LinkedList::new;
	}

	public CollectionInMap(Supplier<Collection<V>> supplier) {
		this.supplier = supplier;
	}

	public CollectionInMap(int initialCapacity, Supplier<Collection<V>> supplier) {
		super(initialCapacity);
		this.supplier = supplier;
	}

	/**
	 * 追加元素
	 *
	 * @param key  map key
	 * @param item 新元素
	 */
	public void putItem(K key, V item) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.add(item);
		} else {
			throw new IllegalStateException("key \"" + key + "\" is not exist.");
		}
	}

	/**
	 * 追加元素
	 * @param key map key
	 * @param items 新元素
	 */
    public void putAllItem(K key, @NonNull Collection<V> items) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.addAll(items);
		} else {
			throw new IllegalStateException("key \"" + key + "\" is not exist.");
		}
	}

	/**
	 * 软追加元素
	 * @param key map key
	 * @param item 新元素
	 */
    public void putItemSoft(K key, V item) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.add(item);
		} else {
			Collection<V> collection = supplier.get();
			collection.add(item);
			put(key, collection);
		}
	}

	/**
	 * 软追加元素
	 * @param key map key
	 * @param items 新元素
	 */
    public void putAllItemSoft(K key, @NonNull Collection<V> items) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.addAll(items);
		} else {
			Collection<V> collection = supplier.get();
			collection.addAll(items);
			put(key, collection);
		}
	}

	/**
	 * 删除元素
	 * @param key map key
	 * @param item 元素
	 */
	public void removeItem(K key, V item) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.remove(item);
		} else {
			throw new IllegalStateException("key \"" + key +  "\" is not exist.");
		}
	}

	/**
	 * 根据条件删除元素
	 * @param key map key
	 * @param filter 条件
	 */
	public void removeIfItem(K key, @NonNull Predicate<? super V> filter) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.removeIf(filter);
		} else {
			throw new IllegalStateException("key \"" + key + "\" is not exist.");
		}
	}
}
