package develop.toolkit.base.utils;

import lombok.NonNull;

/**
 * 比较增强工具
 *
 * @author qiushui on 2019-08-23.
 */
public final class CompareAdvice {

    /**
     * 小于
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean lt(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) < 0;
    }

    /**
     * 小于等于
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean lte(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * 大于
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean gt(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) > 0;
    }

    /**
     * 大于等于
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean gte(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * 等于
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean eq(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) == 0;
    }

    /**
     * 在之间（闭区间）
     *
     * @param a
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    public static <T extends Comparable<T>> boolean between(@NonNull T a, @NonNull T start, @NonNull T end) {
        return gte(a, start) && lte(a, end);
    }

    /**
     * 返回两者中较大值
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> T max(@NonNull T a, @NonNull T b) {
        return gt(a, b) ? a : b;
    }

    /**
     * 返回两者中较小值
     *
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> T min(@NonNull T a, @NonNull T b) {
        return lt(a, b) ? a : b;
    }
}
