package develop.toolkit.base.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 判空处理简化操作
 */
@SuppressWarnings("unused")
public final class K {

    /**
     * 如果为null返回默认值
     *
     * @param value           值
     * @param defaultSupplier 默认值提供器
     * @param <T>             泛型
     * @return 值
     */
    public static <T> T def(T value, Supplier<T> defaultSupplier) {
        return value != null ? value : defaultSupplier.get();
    }

    /**
     * 如果为null返回默认值
     *
     * @param value        值
     * @param defaultValue 默认值
     * @param <T>          泛型
     * @return 值
     */
    public static <T> T def(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * 如果不为null则执行consumer
     *
     * @param value 值
     * @param <T>   泛型
     */
    public static <T> void let(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    /**
     * 如果map的取值不为null则消费
     *
     * @param map      map
     * @param key      键
     * @param consumer 消费者
     * @param <KEY>    键类型
     * @param <V>      值类型
     */
    public static <KEY, V> void let(Map<KEY, V> map, KEY key, Consumer<V> consumer) {
        if (map != null) {
            let(map.get(key), consumer);
        }
    }

    /**
     * 如果列表的值不为null则消费
     *
     * @param list     列表
     * @param i        索引
     * @param consumer 消费者
     * @param <T>      列表元素类型
     */
    public static <T> void let(List<T> list, int i, Consumer<T> consumer) {
        if (list != null) {
            let(list.get(i), consumer);
        }
    }

    /**
     * 如果数组的值不为null则消费
     *
     * @param array    数组
     * @param i        索引
     * @param consumer 消费者
     * @param <T>      数组元素类型
     */
    public static <T> void let(T[] array, int i, Consumer<T> consumer) {
        if (array != null) {
            let(array[i], consumer);
        }
    }

    /**
     * 如果不为null则返回转化值
     *
     * @param value    值
     * @param function 转化函数
     * @return 转化值
     */
    public static <T, R> R map(T value, Function<T, R> function) {
        return value == null ? null : function.apply(value);
    }

    /**
     * 如果map的取值不为null则转化
     *
     * @param map      map
     * @param key      键
     * @param function 函数
     * @param <KEY>    键类型
     * @param <V>      值类型
     * @param <T>      转化类型
     * @return 转化值
     */
    public static <KEY, V, T> T map(Map<KEY, V> map, KEY key, Function<V, T> function) {
        return map == null ? null : map(map.get(key), function);
    }

    /**
     * 如果列表的值不为null则消费
     *
     * @param list     列表
     * @param i        索引
     * @param function 函数
     * @param <V>      列表值类型
     * @param <T>      转化类型
     * @return 转化值
     */
    public static <V, T> T map(List<V> list, int i, Function<V, T> function) {
        return list == null ? null : map(list.get(i), function);
    }

    /**
     * 如果数组的值不为null则消费
     *
     * @param array    数组
     * @param i        索引
     * @param function 函数
     * @param <V>      列表值类型
     * @param <T>      转化类型
     * @return 转化值
     */
    public static <V, T> T map(V[] array, int i, Function<V, T> function) {
        return array == null ? null : map(array[i], function);
    }
}
