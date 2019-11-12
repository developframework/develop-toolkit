package develop.toolkit.base.struct;

import lombok.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Map里有集合结构
 *
 * @author qiushui on 2018-07-09.
 */
public class CollectionInMap<K, V> extends ConcurrentHashMap<K, Collection<V>> implements Serializable {

    private static final long serialVersionUID = 3068493190714636107L;
	private CollectionProvider collectionProvider;

    public CollectionInMap() {
        this.collectionProvider = k -> new LinkedList();
    }

	public CollectionInMap(@NonNull CollectionProvider collectionProvider) {
		this.collectionProvider = collectionProvider;
	}

	public CollectionInMap(int initialCapacity, @NonNull CollectionProvider collectionProvider) {
		super(initialCapacity);
		this.collectionProvider = collectionProvider;
	}

	/**
	 * 追加元素
	 * @param key map key
	 * @param item 新元素
	 */
    public void putItem(K key, V item) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.add(item);
		} else {
			throw new IllegalStateException("key \"" + "\" is not exist.");
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
			throw new IllegalStateException("key \"" + "\" is not exist.");
		}
	}

	/**
	 * 软追加元素
	 * @param key map key
	 * @param item 新元素
	 */
    @SuppressWarnings("unchecked")
    public void putItemSoft(K key, V item) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.add(item);
		} else {
			Collection<V> collection = collectionProvider.provide(key);
			collection.add(item);
			put(key, collection);
		}
	}

	/**
	 * 软追加元素
	 * @param key map key
	 * @param items 新元素
	 */
    @SuppressWarnings("unchecked")
    public void putAllItemSoft(K key, @NonNull Collection<V> items) {
        if (containsKey(key)) {
			Collection<V> collection = get(key);
			collection.addAll(items);
		} else {
			Collection<V> collection = collectionProvider.provide(key);
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

	/**
	 * 集合提供器
	 * @param <V>
	 */
	@FunctionalInterface
	public interface CollectionProvider<K, V> {

		Collection<V> provide(K key);
	}
}
