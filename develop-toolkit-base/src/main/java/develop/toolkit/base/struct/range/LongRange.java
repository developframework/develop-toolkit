package develop.toolkit.base.struct.range;

/**
 * 长整型范围
 */
public class LongRange extends Range<Long> {

    public LongRange(Long start, Long end) {
        super(start, end);
    }

    /**
     * 生成长整型数组
     *
     * @return
     */
    public final long[] generate() {
        long[] array = new long[(int) (end - start)];
        for (int i = 0; i < array.length; i++) {
            array[i] = start + i;
        }
        return array;
    }
}
