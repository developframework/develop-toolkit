package develop.toolkit.world.verify;

/**
 * 常用的正则表达式
 *
 * @author qiushui on 2019-02-26.
 */
public interface Regex {

    // 不严格的手机号
    String MOBILE_RELAXED = "^1\\d{10}$";

    // 不严格的身份证
    String IDENTIFICATION_CARD_RELAXED = "^\\d{15}|(\\d{17}[\\dXx])$";

    // 年
    String YEAR = "^\\d{4}$";

    // 月
    String MONTH = "^\\d{4}-((0[1-9])|(1[0-2]))$";

    // 日
    String DATE = "^\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2]\\d)|(3[0-1]))$";

    // 时间
    String TIME = "^(([0-1]\\d)|(2[0-3]))(:[0-5]\\d){2}$";

    // 日期时间
    String DATE_TIME = "^\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2]\\d)|(3[0-1]))\\s(([0-1]\\d)|(2[0-3]))(:[0-5]\\d){2}$";

}
