package develop.toolkit.base.utils;

import develop.toolkit.base.struct.CollectionInMap;
import develop.toolkit.base.struct.TwoValues;
import lombok.NonNull;

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
     * @param <R>
     * @return
     */
    public static <E, R> boolean contains(@NonNull Collection<E> collection, R target, @NonNull Function<E, R> function) {
        for (E item : collection) {
            R value = function.apply(item);
            if (target == null) {
                return value == null;
            } else if (target.equals(value)) {
                return true;
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
     * @param <R>
     * @return
     */
    public static <E, R> Optional<E> getFirstMatch(@NonNull Collection<E> collection, R target, @NonNull Function<E, R> function) {
        for (E item : collection) {
            R value = function.apply(item);
            if (target == null) {
                return value == null ? Optional.ofNullable(item) : Optional.empty();
            } else if (target.equals(value)) {
                return Optional.ofNullable(item);
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
    public static <E> Optional<E> getFirstTrue(@NonNull Collection<E> collection, @NonNull Predicate<E> predicate) {
        for (E item : collection) {
            if (predicate.test(item)) {
                return Optional.ofNullable(item);
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
    public static <E> Optional<E> getFirstFalse(@NonNull Collection<E> collection, @NonNull Predicate<E> predicate) {
        for (E item : collection) {
            if (!predicate.test(item)) {
                return Optional.ofNullable(item);
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
     * @param <R>
     * @return
     */
    public static <E, R> List<E> getAllMatch(@NonNull Collection<E> collection, R target, @NonNull Function<E, R> function) {
        return collection.stream().filter(item -> {
            R value = function.apply(item);
            if (target == null) {
                return value == null;
            } else {
                return target.equals(value);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 转化为Map
     *
     * @param collection
     * @param keySupplier
     * @param valueSupplier
     * @param <E>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <E, K, V> Map<K, V> toMap(@NonNull Collection<E> collection, @NonNull Function<E, K> keySupplier, @NonNull Function<E, V> valueSupplier) {
        Map<K, V> map = new HashMap<>();
        for (E item : collection) {
            map.put(keySupplier.apply(item), valueSupplier.apply(item));
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
                    map.addItemSoft(e, t);
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
            throw new IllegalArgumentException("list size must be some");
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
