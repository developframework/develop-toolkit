package develop.toolkit.base.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 三个值的结构体
 *
 * @author qiushui on 2019-02-21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreeValues<T, S, U> implements Serializable {

    private static final long serialVersionUID = 1597807032555169555L;
    private T firstValue;

    private S secondValue;

    private U thirdValue;

    /**
     * 带值初始化
     */
    public static <T, S, U> ThreeValues<T, S, U> of(T firstValue, S secondValue, U thirdValue) {
        return new ThreeValues<>(firstValue, secondValue, thirdValue);
    }

    public static <T> ThreeValues<T, T, T> of(T[] objs) {
        return new ThreeValues<>(objs[0], objs[1], objs[2]);
    }
}
