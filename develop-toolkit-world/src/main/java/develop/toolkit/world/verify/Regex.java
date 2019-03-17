package develop.toolkit.world.verify;

/**
 * 常用的正则表达式
 *
 * @author qiushui on 2019-02-26.
 */
public interface Regex {

    // 不严格的手机号
    String MOBILE_RELAXED = "^1\\d{10}$";

    String MOBILE = "^$";

    // 不严格的身份证
    String IDENTIFICATION_CARD_RELAXED = "^\\d{15}|(\\d{17}[\\dXx])$";
}
