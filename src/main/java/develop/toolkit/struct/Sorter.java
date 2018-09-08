package develop.toolkit.struct;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 分拣器
 *
 * @author qiushui on 2018-07-09.
 * @since 0.4
 */
public class Sorter<K, V> {

	private CollectionInMap<K, V> map = new CollectionInMap<>(key -> new LinkedList());

	private SortFunction<K, V> sortFunction;

	public Sorter(@NonNull SortFunction<K, V> sortFunction) {
		this.sortFunction = sortFunction;
	}

	/**
	 * 分拣
	 * @param collection
	 */
	public void sort(Collection<V> collection) {
		for (V item : collection) {
			K key = sortFunction.sort(item);
			map.addItemSoft(key, item);
		}
	}

	/**
	 * 清空
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * 返回单列列表
	 * @param key
	 * @return
	 */
	public List<V> getSingleList(K key) {
		if (map.containsKey(key)) {
			return new ArrayList<>(map.get(key));
		} else {
			throw new IllegalStateException("key \"" + key + "\" is not exist.");
		}
	}

	@FunctionalInterface
	public interface SortFunction<K, V> {

		/**
		 * 返回需要投递的key
		 *
		 * @param item
		 * @return
		 */
		K sort(V item);
	}
}
