package develop.toolkit.base.utils;

import lombok.NonNull;

/**
 * 比较增强工具
 *
 * @author qiushui on 2019-08-23.
 */
@SuppressWarnings("unused")
public final class CompareAdvice {

    /**
     * 小于
     */
    public static <T extends Comparable<T>> boolean lt(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) < 0;
    }

    /**
     * 小于等于
     */
    public static <T extends Comparable<T>> boolean lte(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * 大于
     */
    public static <T extends Comparable<T>> boolean gt(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) > 0;
    }

    /**
     * 大于等于
     */
    public static <T extends Comparable<T>> boolean gte(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * 等于
     */
    public static <T extends Comparable<T>> boolean eq(@NonNull T a, @NonNull T b) {
        return a.compareTo(b) == 0;
    }

    /**
     * 在之间（闭区间）
     */
    public static <T extends Comparable<T>> boolean between(@NonNull T a, @NonNull T start, @NonNull T end) {
        return gte(a, start) && lte(a, end);
    }

    /**
     * 在之间（左闭区间）
     */
    public static <T extends Comparable<T>> boolean betweenLeft(@NonNull T a, @NonNull T start, @NonNull T end) {
        return gte(a, start) && lt(a, end);
    }

    /**
     * 在之间（右闭区间）
     */
    public static <T extends Comparable<T>> boolean betweenRight(@NonNull T a, @NonNull T start, @NonNull T end) {
        return gt(a, start) && lte(a, end);
    }

    /**
     * 在之间（开区间）
     */
    public static <T extends Comparable<T>> boolean betweenOpen(@NonNull T a, @NonNull T start, @NonNull T end) {
        return gt(a, start) && lt(a, end);
    }

    /**
     * 返回两者中较大值
     */
    public static <T extends Comparable<T>> T max(@NonNull T a, @NonNull T b) {
        return gte(a, b) ? a : b;
    }

    /**
     * 返回两者中较小值
     */
    public static <T extends Comparable<T>> T min(@NonNull T a, @NonNull T b) {
        return lte(a, b) ? a : b;
    }

    /**
     * 调整边界值
     */
    public static <T extends Comparable<T>> T adjustRange(T x, T start, T end) {
        if (gt(start, end)) {
            throw new IllegalArgumentException("start great than end");
        } else if (lt(x, start)) {
            return start;
        } else if (gt(x, end)) {
            return end;
        } else {
            return x;
        }
    }
}
