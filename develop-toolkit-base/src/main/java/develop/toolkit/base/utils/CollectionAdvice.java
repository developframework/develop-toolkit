package develop.toolkit.base.utils;

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
    public static <E, R> boolean contains(@NonNull Collection<E> collection, R target, Function<E, R> function) {
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
    public static <E, R> Optional<E> getFirstMatch(@NonNull Collection<E> collection, R target, Function<E, R> function) {
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
     * 获得全部匹配的元素
     *
     * @param collection
     * @param target
     * @param function
     * @param <E>
     * @param <R>
     * @return
     */
    public static <E, R> List<E> getAllMatch(@NonNull Collection<E> collection, R target, Function<E, R> function) {
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
}
