package develop.toolkit.world.normal;

import lombok.Data;

import java.io.Serializable;

/**
 * 地区
 *
 * @author qiushui on 2019-02-26.
 */
@Data
public class Region implements Serializable {

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
}
