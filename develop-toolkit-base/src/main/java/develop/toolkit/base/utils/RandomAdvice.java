package develop.toolkit.base.utils;

import develop.toolkit.base.struct.range.IntRange;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public final class RandomAdvice {

    /**
     * 随机count个整数
     */
    public static int[] nextInts(final int startInclusive, final int endExclusive, final int count) {
        Validate.isTrue(endExclusive - startInclusive >= count, "Difference value must be greater or equal to end value.");
        List<Integer> list = new ArrayList<>(List.of(new IntRange(startInclusive, endExclusive).generate()));
        int[] result = new int[count];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.remove(RandomUtils.nextInt(0, list.size()));
        }
        return result;
    }

    /**
     * 随机count个元素
     */
    public static <T> List<T> nextElements(final List<T> list, final int count) {
        return IntStream
                .of(nextInts(0, list.size(), count))
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }

    /**
     * 随机一个元素
     */
    public static <T> T nextElement(final List<T> list) {
        return list.get(RandomUtils.nextInt(0, list.size()));
    }
}
