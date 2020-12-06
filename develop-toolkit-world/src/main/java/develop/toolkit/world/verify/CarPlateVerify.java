package develop.toolkit.world.verify;

/**
 * 车牌验证
 *
 * @author qiushui on 2020-05-12.
 */
public final class CarPlateVerify {

    /**
     * 参考https://my.oschina.net/chenyoca/blog/1571062
     */
    public static boolean checkValid(String plate) {
        final String PROVINCES = "京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新";
        String provinceShortName = String.valueOf(plate.charAt(0));
        if (!PROVINCES.contains(provinceShortName)) {
            return false;
        }
        if (plate.length() == 7) {
            return validNormal(plate);
        } else if (plate.length() == 8) {
            return validNewEnergy(plate);
        } else {
            return false;
        }
    }

    /**
     * 普通民用车牌
     */
    private static boolean validNormal(String plate) {
        final String PLATE_CHARS_ORG = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        final String PLATE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
        if (PLATE_CHARS_ORG.contains(String.valueOf(plate.charAt(1)))) {
            for (int i = 2; i < 7; i++) {
                String ch = String.valueOf(plate.charAt(i));
                if (!PLATE_CHARS.contains(ch)) {
                    return i == 6 && "学挂".contains(ch);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 新能源车牌
     */
    private static boolean validNewEnergy(String plate) {
        final String PLATE_CHARS_ORG = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        if (PLATE_CHARS_ORG.contains(String.valueOf(plate.charAt(1)))) {
            if (
                    "123456789DF".contains(String.valueOf(plate.charAt(2))) &&
                            "ABCDEFGHJKLMNPQRSTUVWXYZ123456789".contains(String.valueOf(plate.charAt(3))) &&
                            "123456789DF".contains(String.valueOf(plate.charAt(7)))
            ) {
                for (int i = 4; i < 6; i++) {
                    if ("0123456789".contains(String.valueOf(plate.charAt(i)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
