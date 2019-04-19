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
}
