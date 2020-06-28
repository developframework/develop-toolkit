package develop.toolkit.world.verify;

/**
 * 车牌验证
 *
 * @author qiushui on 2020-05-12.
 */
public final class CarPlateVerify {

    private static final String PROVINCES = "京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新";

    private static final String PLATE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ";

    private static final String PLATE_CHARS_NUMBER = "0123456789";

    private static final String PLATE_CHARS_OTHER = "学挂";

    /**
     * 参考https://my.oschina.net/chenyoca/blog/1571062
     *
     * @param plate
     * @return
     */
    public static boolean checkValid(String plate) {
        String provinceShortName = String.valueOf(plate.charAt(0));
        if (!PROVINCES.contains(provinceShortName)) {
            return false;
        }
        if (plate.length() == 7) {
            for (int i = 1; i < 7; i++) {
                if (!PLATE_CHARS.contains(String.valueOf(plate.charAt(i)))) {
                    return false;
                }
            }
        } else if (plate.length() == 8) {

        }
        return true;
    }
}
