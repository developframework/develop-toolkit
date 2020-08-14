package develop.toolkit.base.struct;

import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Map里有集合结构
 *
 * @author qiushui on 2020-08-13.
 */
@SuppressWarnings("unused")
public abstract class AbstractCollectionInMap<K, V, COLLECTION extends Collection<V>> extends LinkedHashMap<K, COLLECTION> {

	private static final long serialVersionUID = 3068493190714636107L;
	protected final Supplier<COLLECTION> supplier;

	public AbstractCollectionInMap(Supplier<COLLECTION> supplier) {
		this.supplier = supplier;
	}

	/**
	 * 追加元素
	 *
	 * @param key  map key
	 * @param item 新元素
	 */
	public final void putItem(K key, V item) {
		getInternal(key).add(item);
	}

	/**
	 * 追加元素
	 *
	 * @param key   map key
	 * @param items 新元素
	 */
	public final void putAllItem(K key, @NonNull Set<V> items) {
		getInternal(key).addAll(items);
	}

	private COLLECTION getInternal(K key) {
		COLLECTION collection = get(key);
		if (collection == null) {
			collection = supplier.get();
			put(key, collection);
		}
		return collection;
	}

	/**
	 * 删除元素
	 *
	 * @param key  map key
	 * @param item 元素
	 */
	public final void removeItem(K key, V item) {
		COLLECTION collection = get(key);
		if (collection != null) {
			collection.remove(item);
		} else {
			throw new IllegalStateException("key \"" + key + "\" is not exist.");
		}
	}

	/**
	 * 根据条件删除元素
	 *
	 * @param key    map key
	 * @param filter 条件
	 */
	public final void removeIfItem(K key, @NonNull Predicate<? super V> filter) {
		COLLECTION collection = get(key);
		if (collection != null) {
			collection.removeIf(filter);
		} else {
			throw new IllegalStateException("key \"" + key + "\" is not exist.");
		}
	}
}
