package develop.toolkit.base.utils;

import develop.toolkit.base.components.Counter;
import develop.toolkit.base.struct.KeyValuePairs;
import develop.toolkit.base.struct.ListInMap;
import develop.toolkit.base.struct.TwoValues;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数组增强工具
 *
 * @author qiushui on 2020-07-17.
 */
@SuppressWarnings("unused")
public final class ArrayAdvice {

    /**
     * 检查元素存在
     */
    public static <E> boolean contains(E[] array, Object target, Function<E, ?> function) {
        if (array != null) {
            for (E item : array) {
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
    public static <E> boolean contains(E[] array, Object target) {
        if (array != null) {
            for (E item : array) {
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
    public static <E> Optional<E> getFirstMatch(E[] array, Object target, Function<E, ?> function) {
        if (array != null) {
            for (E item : array) {
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
    public static <E> Optional<E> getFirstMatch(E[] array, Object target) {
        if (array != null && target != null) {
            for (E item : array) {
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
    public static <E> Optional<E> getFirstTrue(E[] array, Predicate<E> predicate) {
        if (array != null) {
            for (E item : array) {
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
    public static <E> Optional<E> getFirstFalse(E[] array, Predicate<E> predicate) {
        if (array != null) {
            for (E item : array) {
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
    public static <E> List<E> getAllMatch(E[] array, Object target, Function<E, ?> function) {
        if (array == null) {
            return null;
        }
        return Stream
                .of(array)
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
    public static <E> boolean allMatch(E[] array, Predicate<E> predicate) {
        if (predicate == null || array == null) {
            return false;
        }
        for (E e : array) {
            if (!predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 任意一个匹配
     */
    public static <E> boolean anyMatch(E[] array, Predicate<E> predicate) {
        if (array != null && predicate != null) {
            for (E e : array) {
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
    public static <E> boolean allAccept(E[] array, Function<E, ?> function) {
        if (array == null || array.length == 0) {
            return false;
        }
        Object targetValue = function == null ? array[0] : function.apply(array[0]);
        for (int i = 1, size = array.length; i < size; i++) {
            Object itemValue = function == null ? array[i] : function.apply(array[i]);
            if ((targetValue != null && !targetValue.equals(itemValue)) || (targetValue == null && itemValue != null)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 分组
     */
    public static <E, K, V> ListInMap<K, V> grouping(E[] array, Function<E, K> keySupplier, Function<E, V> valueSupplier) {
        ListInMap<K, V> map = new ListInMap<>();
        for (E item : array) {
            map.putItem(keySupplier.apply(item), valueSupplier.apply(item));
        }
        return map;
    }

    public static <K, V> ListInMap<K, V> grouping(V[] array, Function<V, K> keySupplier) {
        ListInMap<K, V> map = new ListInMap<>();
        for (V item : array) {
            map.putItem(keySupplier.apply(item), item);
        }
        return map;
    }

    public static <K, V> Map<K, V> groupingUniqueKey(V[] array, Function<V, K> keySupplier) {
        Map<K, V> map = new HashMap<>();
        for (V item : array) {
            map.put(keySupplier.apply(item), item);
        }
        return map;
    }

    /**
     * 分组求数量
     */
    public static <E, K> Counter<K> groupingCount(E[] array, Function<E, K> keySupplier) {
        Counter<K> counter = new Counter<>();
        for (E item : array) {
            counter.add(keySupplier.apply(item));
        }
        return counter;
    }

    /**
     * 并集
     */
    @SafeVarargs
    public static <E> Set<E> union(E[] master, E[]... other) {
        Set<E> set = new HashSet<>(Set.of(master));
        for (E[] array : other) {
            set.addAll(Set.of(array));
        }
        return set;
    }

    /**
     * 交集
     */
    @SafeVarargs
    public static <E> Set<E> intersection(E[] master, E[]... other) {
        Set<E> set = new HashSet<>(Set.of(master));
        for (E[] array : other) {
            set.removeIf(Predicate.not(item -> contains(array, item)));
        }
        return set;
    }

    /**
     * 补集
     */
    public static <E> Set<E> complementary(E[] master, E[] other) {
        Set<E> set = new HashSet<>(Set.of(master));
        set.removeIf(item -> contains(other, item));
        return set;
    }

    /**
     * 合并多数组
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E> E[] merge(Class<E> clazz, E[]... arrays) {
        E[] mergeArray = (E[]) Array.newInstance(
                clazz,
                Stream.of(arrays).mapToInt(array -> array.length).sum()
        );
        int i = 0;
        for (E[] array : arrays) {
            for (E item : array) {
                if (item != null) {
                    mergeArray[i++] = item;
                }
            }
        }
        return mergeArray;
    }

    /**
     * 关联
     * 将集合target按条件与集合master配对
     */
    public static <E, T> ListInMap<E, T> associate(E[] master, T[] target, BiPredicate<E, T> predicate) {
        ListInMap<E, T> map = new ListInMap<>();
        for (E e : master) {
            for (T t : target) {
                if (predicate.test(e, t)) {
                    map.putItem(e, t);
                }
            }
        }
        return map;
    }

    /**
     * 关联 （明确是单个的）
     * 将集合target按条件与集合master配对
     */
    public static <E, T> KeyValuePairs<E, T> associateOne(E[] master, T[] target, BiPredicate<E, T> predicate) {
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
    public static <E> TwoValues<List<E>, List<E>> partition(E[] collection, Predicate<E> predicate) {
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
    public static <T, S> List<TwoValues<T, S>> zip(T[] master, S[] other) {
        if (master.length != other.length) {
            throw new IllegalArgumentException("list size must be same");
        }
        List<TwoValues<T, S>> list = new LinkedList<>();
        for (int i = 0; i < master.length; i++) {
            list.add(TwoValues.of(master[i], other[i]));
        }
        return list;
    }

    /**
     * 分页处理
     */
    public static <E> void pagingProcess(E[] array, int size, Consumer<E[]> consumer) {
        final int total = array.length;
        final int page = total % size == 0 ? (total / size) : (total / size + 1);
        for (int i = 0; i < page; i++) {
            int fromIndex = i * size;
            int toIndex = fromIndex + Math.min(total - fromIndex, size);
            E[] subArray = ArrayUtils.subarray(array, fromIndex, toIndex);
            consumer.accept(subArray);
        }
    }

    /**
     * 指定排序
     * 把master的元素值按sortTarget的元素值排序，条件按predicate
     */
    public static <T, S> List<T> sort(T[] master, S[] sortTarget, BiPredicate<T, S> predicate) {
        return Stream
                .of(sortTarget)
                .map(s -> ArrayAdvice.getFirstTrue(master, c -> predicate.test(c, s)).orElse(null))
                .collect(Collectors.toList());
    }

    public static <T, S> List<T> sort(T[] master, Collection<S> sortTarget, BiPredicate<T, S> predicate) {
        return sortTarget
                .stream()
                .map(s -> ArrayAdvice.getFirstTrue(master, c -> predicate.test(c, s)).orElse(null))
                .collect(Collectors.toList());
    }
}
