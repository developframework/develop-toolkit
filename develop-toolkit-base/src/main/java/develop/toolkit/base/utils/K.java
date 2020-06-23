package develop.toolkit.base.utils;

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
     * 如果不为null则返回转化值
     *
     * @param value    值
     * @param function 转化函数
     * @return 转化值
     */
    public static <T, R> R map(T value, Function<T, R> function) {
        return value != null ? function.apply(value) : null;
    }
}
