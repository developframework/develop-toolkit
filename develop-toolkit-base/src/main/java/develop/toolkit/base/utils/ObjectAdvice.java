package develop.toolkit.base.utils;

import lombok.NonNull;

import java.util.function.Supplier;

/**
 * 实例对象处理增强工具
 *
 * @author qiushui on 2019-02-20.
 */
public final class ObjectAdvice {

    /**
     * 赋值
     *
     * @param obj             值
     * @param defaultSupplier 默认值提供器
     * @param <T>
     * @return
     */
    public static <T> T assign(T obj, @NonNull Supplier<T> defaultSupplier) {
        return obj != null ? obj : defaultSupplier.get();
    }
}
