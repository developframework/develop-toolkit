package develop.toolkit.base.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 两个值的结构体
 *
 * @author qiushui on 2019-02-21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoValues<T, S> implements Serializable {

    private static final long serialVersionUID = -6681837347885235332L;
    private T firstValue;

    private S secondValue;

    /**
     * 带值初始化
     */
    public static <T, S> TwoValues<T, S> of(T firstValue, S secondValue) {
        return new TwoValues<>(firstValue, secondValue);
    }
}
