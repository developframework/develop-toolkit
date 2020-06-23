package develop.toolkit.base.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计学指标
 *
 * @author qiushui on 2020-01-02.
 */
@SuppressWarnings("unused")
public final class StatisticsAdvice {

    /**
     * 最大值
     */
    public static double max(Collection<Number> numbers) {
        return numbers
                .stream()
                .mapToDouble(Number::doubleValue)
                .max().orElseThrow();
    }

    /**
     * 最小值
     */
    public static double min(Collection<Number> numbers) {
        return numbers
                .stream()
                .mapToDouble(Number::doubleValue)
                .min().orElseThrow();
    }

    /**
     * 平均值
     */
    public static double average(Collection<Number> numbers) {
        return numbers
                .stream()
                .mapToDouble(Number::doubleValue)
                .average().orElseThrow();
    }

    /**
     * 方差
     */
    public static double variance(Collection<Number> numbers) {
        final double average = average(numbers);
        return numbers
                .stream()
                .mapToDouble(number -> Math.pow(number.doubleValue() - average, 2))
                .average().orElseThrow();
    }

    /**
     * 标准差
     */
    public static double standardDeviation(Collection<Number> numbers) {
        return Math.sqrt(variance(numbers));
    }

    /**
     * 中位数
     */
    public static double median(Collection<Number> numbers) {
        final List<Double> list = numbers
                .stream()
                .sorted()
                .map(Number::doubleValue)
                .collect(Collectors.toList());
        if (list.size() % 2 == 0) {
            int half = list.size() / 2;
            return (list.get(half) + list.get(half + 1)) / 2;
        } else {
            return list.get(list.size() / 2 + 1);
        }
    }
}
