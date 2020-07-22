package develop.toolkit.base.utils;

import develop.toolkit.base.components.Counter;
import develop.toolkit.base.struct.CollectionInMap;
import develop.toolkit.base.struct.KeyValuePairs;
import develop.toolkit.base.struct.TwoValues;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合增强工具
 *
 * @author qiushui on 2018-12-20.
 */
@SuppressWarnings("unused")
public final class CollectionAdvice {

    /**
     * 检查元素存在
     */
    public static <E> boolean contains(Collection<E> collection, Object target, Function<E, ?> function) {
        if (collection != null) {
            for (E item : collection) {
                Object value = function.apply(item);
                if (target == null) {
                    return value == null;
                } else if (target.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查元素存在
     */
    public static <E> boolean contains(Collection<E> collection, Object target) {
        if (collection != null) {
            for (E item : collection) {
                if (target == null) {
                    return item == null;
                } else if (target.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得第一个匹配的元素
     */
    public static <E> Optional<E> getFirstMatch(Collection<E> collection, Object target, Function<E, ?> function) {
        if (collection != null) {
            for (E item : collection) {
                final Object value = function.apply(item);
                if (target != null) {
                    if (target.equals(value)) {
                        return Optional.ofNullable(item);
                    }
                } else if (value == null) {
                    return Optional.ofNullable(item);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获得第一个匹配的元素
     */
    public static <E> Optional<E> getFirstMatch(Collection<E> collection, Object target) {
        if (collection != null && target != null) {
            for (E item : collection) {
                if (target.equals(item)) {
                    return Optional.of(item);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获得第一个判断是true的元素
     */
    public static <E> Optional<E> getFirstTrue(Collection<E> collection, Predicate<E> predicate) {
        if (collection != null) {
            for (E item : collection) {
                if (predicate.test(item)) {
                    return Optional.ofNullable(item);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获得第一个判断是false的元素
     */
    public static <E> Optional<E> getFirstFalse(Collection<E> collection, Predicate<E> predicate) {
        if (collection != null) {
            for (E item : collection) {
                if (!predicate.test(item)) {
                    return Optional.ofNullable(item);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获得全部匹配的元素
     */
    public static <E> List<E> getAllMatch(Collection<E> collection, Object target, Function<E, ?> function) {
        if (collection == null) {
            return null;
        }
        return collection
                .stream()
                .filter(item -> {
                    Object value = function == null ? item : function.apply(item);
                    if (target == null) {
                        return value == null;
                    } else {
                        return target.equals(value);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 全部匹配
     */
    public static <E> boolean allMatch(Collection<E> collection, Predicate<E> predicate) {
        if (predicate == null || collection == null) {
            return false;
        }
        for (E e : collection) {
            if (!predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 任意一个匹配
     */
    public static <E> boolean anyMatch(Collection<E> collection, Predicate<E> predicate) {
        if (collection != null && predicate != null) {
            for (E e : collection) {
                if (predicate.test(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断所有元素的处理值相等
     */
    public static <E> boolean allAccept(Collection<E> collection, Function<E, ?> function) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        List<E> list = new ArrayList<>(collection);
        Object targetValue = function == null ? list.get(0) : function.apply(list.get(0));
        for (int i = 1, size = list.size(); i < size; i++) {
            Object itemValue = function == null ? list.get(i) : function.apply(list.get(i));
            if ((targetValue != null && !targetValue.equals(itemValue)) || (targetValue == null && itemValue != null)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 分组
     */
    public static <E, K, V> CollectionInMap<K, V> grouping(Collection<E> collection, Function<E, K> keySupplier, Function<E, V> valueSupplier) {
        CollectionInMap<K, V> map = new CollectionInMap<>();
        collection.forEach(item -> map.putItemSoft(keySupplier.apply(item), valueSupplier.apply(item)));
        return map;
    }

    public static <K, V> CollectionInMap<K, V> grouping(Collection<V> collection, Function<V, K> keySupplier) {
        CollectionInMap<K, V> map = new CollectionInMap<>();
        collection.forEach(item -> map.putItemSoft(keySupplier.apply(item), item));
        return map;
    }

    public static <K, V> Map<K, V> groupingUniqueKey(Collection<V> collection, Function<V, K> keySupplier) {
        Map<K, V> map = new HashMap<>();
        collection.forEach(item -> map.put(keySupplier.apply(item), item));
        return map;
    }

    /**
     * 分组求数量
     */
    public static <E, K> Counter<K> groupingCount(Collection<E> collection, Function<E, K> keySupplier) {
        Counter<K> counter = new Counter<>();
        collection.forEach(item -> counter.add(keySupplier.apply(item)));
        return counter;
    }

    /**
     * 并集
     */
    @SafeVarargs
    public static <E> Set<E> union(Collection<E> master, Collection<E>... other) {
        Set<E> set = new HashSet<>(master);
        for (Collection<E> collection : other) {
            set.addAll(collection);
        }
        return set;
    }

    /**
     * 交集
     */
    @SafeVarargs
    public static <E> Set<E> intersection(Collection<E> master, Collection<E>... other) {
        Set<E> set = new HashSet<>(master);
        for (Collection<E> collection : other) {
            set.removeIf(Predicate.not(collection::contains));
        }
        return set;
    }

    /**
     * 补集
     */
    public static <E> Set<E> complementary(Collection<E> master, Collection<E> other) {
        Set<E> set = new HashSet<>(master);
        set.removeIf(other::contains);
        return set;
    }

    /**
     * 合并多集合
     */
    @SafeVarargs
    public static <E> Collection<E> merge(Supplier<Collection<E>> supplier, Collection<E>... collections) {
        Collection<E> collection = supplier.get();
        for (Collection<E> coll : collections) {
            if (coll != null) {
                collection.addAll(coll);
            }
        }
        return collection;
    }

    /**
     * 关联
     * 将集合target按条件与集合master配对
     */
    public static <E, T> CollectionInMap<E, T> associate(Collection<E> master, Collection<T> target, BiPredicate<E, T> predicate) {
        CollectionInMap<E, T> map = new CollectionInMap<>();
        for (E e : master) {
            for (T t : target) {
                if (predicate.test(e, t)) {
                    map.putItemSoft(e, t);
                }
            }
        }
        return map;
    }

    /**
     * 关联 （明确是单个的）
     * 将集合target按条件与集合master配对
     */
    public static <E, T> KeyValuePairs<E, T> associateOne(Collection<E> master, Collection<T> target, BiPredicate<E, T> predicate) {
        final KeyValuePairs<E, T> keyValuePairs = new KeyValuePairs<>();
        for (E e : master) {
            final T matchT = getFirstTrue(target, t -> predicate.test(e, t)).orElse(null);
            keyValuePairs.addKeyValue(e, matchT);
        }
        return keyValuePairs;
    }

    /**
     * 划分
     * 按条件把集合拆分成满足条件和不满足条件的两个集合
     */
    public static <E> TwoValues<List<E>, List<E>> partition(Collection<E> collection, Predicate<E> predicate) {
        List<E> match = new LinkedList<>();
        List<E> notMatch = new LinkedList<>();
        for (E e : collection) {
            if (predicate.test(e)) {
                match.add(e);
            } else {
                notMatch.add(e);
            }
        }
        return TwoValues.of(match, notMatch);
    }

    /**
     * 压缩
     * 将两个集合的元素按索引捆绑到一起
     */
    public static <T, S> List<TwoValues<T, S>> zip(List<T> master, List<S> other) {
        if (master.size() != other.size()) {
            throw new IllegalArgumentException("list size must be same");
        }
        List<TwoValues<T, S>> list = new LinkedList<>();
        for (int i = 0; i < master.size(); i++) {
            list.add(TwoValues.of(master.get(i), other.get(i)));
        }
        return list;
    }

    /**
     * 分页处理
     */
    public static <T> void pagingProcess(List<T> list, int size, Consumer<List<T>> consumer) {
        final int total = list.size();
        final int page = total % size == 0 ? (total / size) : (total / size + 1);
        for (int i = 0; i < page; i++) {
            int fromIndex = i * size;
            int toIndex = fromIndex + Math.min(total - fromIndex, size);
            List<T> subList = list.subList(fromIndex, toIndex);
            consumer.accept(subList);
        }
    }

    /**
     * 指定排序
     * 把master的元素值按sortTarget的元素值排序，条件按predicate
     */
    public static <T, S> List<T> sort(Collection<T> master, Collection<S> sortTarget, BiPredicate<T, S> predicate) {
        return sortTarget
                .stream()
                .map(s -> CollectionAdvice.getFirstTrue(master, c -> predicate.test(c, s)).orElse(null))
                .collect(Collectors.toList());
    }

    public static <T, S> List<T> sort(Collection<T> master, S[] sortTarget, BiPredicate<T, S> predicate) {
        return Stream
                .of(sortTarget)
                .map(s -> CollectionAdvice.getFirstTrue(master, c -> predicate.test(c, s)).orElse(null))
                .collect(Collectors.toList());
    }
}
