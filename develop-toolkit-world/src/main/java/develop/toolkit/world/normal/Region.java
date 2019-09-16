package develop.toolkit.world.normal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 地区
 *
 * @author qiushui on 2019-02-26.
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"provinceCode", "cityCode", "countyCode"})
public class Region implements Serializable {

    private static final long serialVersionUID = 1431053980547777108L;

    /* 省级 */
    private String province;

    /* 市级 */
    private String city;

    /* 县级 */
    private String county;

    /* 省级编码 */
    private Integer provinceCode;

    /* 市级编码 */
    private Integer cityCode;

    /* 县级编码 */
    private Integer countyCode;

    @Override
    public String toString() {
        return province + city + county;
    }
}
