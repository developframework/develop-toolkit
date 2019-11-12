package develop.toolkit.base.utils;

import develop.toolkit.base.struct.CollectionInMap;
import develop.toolkit.base.struct.TwoValues;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合增强工具
 *
 * @author qiushui on 2018-12-20.
 */
public final class CollectionAdvice {

    /**
     * 检查元素
     *
     * @param collection
     * @param target
     * @param function
     * @param <E>
     * @return
     */
    public static <E> boolean contains(Collection<E> collection, Object target, Function<E, ?> function) {
        if (collection != null) {
            for (E item : collection) {
                Object value = function == null ? item : function.apply(item);
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
     * 获得第一个匹配的元素
     *
     * @param collection
     * @param target
     * @param function
     * @param <E>
     * @return
     */
    public static <E> Optional<E> getFirstMatch(Collection<E> collection, Object target, Function<E, ?> function) {
        if (collection != null) {
            for (E item : collection) {
                Object value = function == null ? item : function.apply(item);
                if (target == null) {
                    return value == null ? Optional.ofNullable(item) : Optional.empty();
                } else if (target.equals(value)) {
                    return Optional.ofNullable(item);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获得第一个判断是true的元素
     *
     * @param collection
     * @param predicate
     * @param <E>
     * @return
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
     *
     * @param collection
     * @param predicate
     * @param <E>
     * @return
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
     *
     * @param collection
     * @param target
     * @param function
     * @param <E>
     * @return
     */
    public static <E> List<E> getAllMatch(Collection<E> collection, Object target, Function<E, ?> function) {
        if (collection == null) {
            return null;
        }
        return collection.stream().filter(item -> {
            Object value = function == null ? item : function.apply(item);
            if (target == null) {
                return value == null;
            } else {
                return target.equals(value);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 全部匹配
     *
     * @param collection
     * @param predicate
     * @param <E>
     * @return
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
     *
     * @param collection
     * @param predicate
     * @param <E>
     * @return
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
     *
     * @param collection
     * @param function
     * @param <E>
     * @return
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
     *
     * @param collection
     * @param keySupplier
     * @param valueSupplier
     * @param <E>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <E, K, V> CollectionInMap<K, V> grouping(Collection<E> collection, Function<E, K> keySupplier, Function<E, V> valueSupplier) {
        CollectionInMap<K, V> map = new CollectionInMap<>(k -> new LinkedList());
        for (E item : collection) {
            map.putItemSoft(keySupplier.apply(item), valueSupplier.apply(item));
        }
        return map;
    }

    /**
     * 分组求数量
     *
     * @param collection
     * @param keySupplier
     * @param <E>
     * @param <K>
     * @return
     */
    public static <E, K> Map<K, Integer> groupingCount(Collection<E> collection, Function<E, K> keySupplier) {
        Map<K, Integer> map = new HashMap<>();
        for (E item : collection) {
            K key = keySupplier.apply(item);
            Integer v;
            if ((v = map.get(key)) != null) {
                map.put(key, ++v);
            } else {
                map.put(key, 1);
            }
        }
        return map;
    }

    /**
     * 并集
     *
     * @param master
     * @param other
     * @param <E>
     * @return
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
     *
     * @param master
     * @param other
     * @param <E>
     * @return
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
     * 关联
     * 将集合target按条件与集合master配对
     *
     * @param master
     * @param target
     * @param predicate
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> CollectionInMap<E, T> associate(Collection<E> master, Collection<T> target, AssociatePredicate<E, T> predicate) {
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
     * 划分
     * 按条件把集合拆分成满足条件和不满足条件的两个集合
     *
     * @param collection
     * @param predicate
     * @param <E>
     * @return
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
     *
     * @param master
     * @param other
     * @param <E>
     * @return
     */
    public static <E> List<TwoValues<E, E>> zip(List<E> master, List<E> other) {
        if (master.size() != other.size()) {
            throw new IllegalArgumentException("list size must be same");
        }
        List<TwoValues<E, E>> list = new LinkedList<>();
        for (int i = 0; i < master.size(); i++) {
            list.add(TwoValues.of(master.get(i), other.get(i)));
        }
        return list;
    }

    public interface AssociatePredicate<E, T> {
        boolean test(E master, T target);
    }
}
