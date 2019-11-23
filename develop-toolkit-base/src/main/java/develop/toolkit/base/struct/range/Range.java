package develop.toolkit.base.struct.range;

import develop.toolkit.base.utils.CompareAdvice;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

/**
 * 范围结构体
 *
 * @param <T>
 */
@Getter
@EqualsAndHashCode
public class Range<T extends Comparable<T>> {

    protected T start;

    protected T end;

    public Range(T start, T end) {
        Validate.isTrue(CompareAdvice.gte(end, start), "Start value must be smaller or equal to end value.");
        this.start = start;
        this.end = end;
    }
}
