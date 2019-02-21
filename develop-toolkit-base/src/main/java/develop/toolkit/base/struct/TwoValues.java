package develop.toolkit.base.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 两个值的结构体
 *
 * @author qiushui on 2019-02-21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoValues<T, S> {

    private T firstValue;

    private S secondValue;
}
