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

    /**
     * 是否是字节
     *
     * @param obj
     * @return
     */
    public static boolean isByte(Object obj) {
        var clazz = obj.getClass();
        return clazz == byte.class || clazz == Byte.class;
    }

    /**
     * 是否是短整型
     *
     * @param obj
     * @return
     */
    public static boolean isShort(Object obj) {
        var clazz = obj.getClass();
        return clazz == short.class || clazz == Short.class;
    }

    /**
     * 是否是整型
     *
     * @param obj
     * @return
     */
    public static boolean isInt(Object obj) {
        var clazz = obj.getClass();
        return clazz == int.class || clazz == Integer.class;
    }

    /**
     * 是否是长整型
     *
     * @param obj
     * @return
     */
    public static boolean isLong(Object obj) {
        var clazz = obj.getClass();
        return clazz == long.class || clazz == Long.class;
    }

    /**
     * 是否是单精度浮点型
     *
     * @param obj
     * @return
     */
    public static boolean isFloat(Object obj) {
        var clazz = obj.getClass();
        return clazz == float.class || clazz == Float.class;
    }

    /**
     * 是否是双精度浮点型
     *
     * @param obj
     * @return
     */
    public static boolean isDouble(Object obj) {
        var clazz = obj.getClass();
        return clazz == double.class || clazz == Double.class;
    }

    /**
     * 是否是字符型
     *
     * @param obj
     * @return
     */
    public static boolean isChar(Object obj) {
        var clazz = obj.getClass();
        return clazz == char.class || clazz == Character.class;
    }

    /**
     * 是否是布尔型
     *
     * @param obj
     * @return
     */
    public static boolean isBoolean(Object obj) {
        var clazz = obj.getClass();
        return clazz == boolean.class || clazz == Boolean.class;
    }

    /**
     * 值是否在数组里
     *
     * @param obj
     * @param targets
     * @param <T>
     * @return
     */
    public static <T> boolean valueIn(@NonNull T obj, T... targets) {
        for (T target : targets) {
            if (obj.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 值是否不在数组里
     *
     * @param obj
     * @param targets
     * @param <T>
     * @return
     */
    public static <T> boolean valueNotIn(@NonNull T obj, T... targets) {
        for (T target : targets) {
            if (obj.equals(target)) {
                return false;
            }
        }
        return true;
    }
}
