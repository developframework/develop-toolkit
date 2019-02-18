package develop.toolkit.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

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
    public static <E, R> boolean isInCollection(Collection<E> collection, R target, Function<E, R> function) {
        for (E item : collection) {
            R value = function.apply(item);
            if (target.equals(value)) {
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
    public static <E, R> Optional<E> getFirstMatchInCollection(Collection<E> collection, R target, Function<E, R> function) {
        for (E item : collection) {
            R value = function.apply(item);
            if (target.equals(value)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

}
