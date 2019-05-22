package develop.toolkit.world.verify;

import develop.toolkit.world.person.IdentificationCard;

/**
 * 所有验证
 *
 * @author qiushui on 2019-02-26.
 */
public final class WorldVerify {

    /**
     * 不严格验证手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileRelaxed(String mobile) {
        return mobile != null && mobile.matches(Regex.MOBILE_RELAXED);
    }

    /**
     * 不严格验证身份证
     *
     * @param card
     * @return
     */
    public static boolean isIdentificationCardRelaxed(String card) {
        return card != null && card.matches(Regex.IDENTIFICATION_CARD_RELAXED);
    }

    /**
     * 严格验证身份证
     *
     * @param card
     * @return
     */
    public static boolean isIdentificationCard(String card) {
        return IdentificationCard.isValid(card);
    }
}
