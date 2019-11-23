package develop.toolkit.base.struct.range;

/**
 * 整型范围
 */
public class IntRange extends Range<Integer> {

    public IntRange(Integer start, Integer end) {
        super(start, end);
    }

    /**
     * 生成整型数组
     *
     * @return
     */
    public final Integer[] generate() {
        Integer[] array = new Integer[end - start];
        for (int i = 0; i < array.length; i++) {
            array[i] = start + i;
        }
        return array;
    }
}
