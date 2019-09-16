package develop.toolkit.world.normal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 地址
 *
 * @author qiushui on 2019-02-26.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Address {

    private Region region;

    private String detailedAddress;

    @Override
    public String toString() {
        return region + detailedAddress;
    }
}
