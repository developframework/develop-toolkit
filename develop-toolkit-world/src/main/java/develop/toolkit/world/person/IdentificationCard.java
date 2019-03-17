package develop.toolkit.world.person;

import develop.toolkit.world.normal.Region;
import develop.toolkit.world.verify.Regex;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 中国公民身份证
 *
 * @author qiushui on 2019-02-27.
 */
@Getter
@EqualsAndHashCode(of = "card")
@NoArgsConstructor
public class IdentificationCard implements Serializable {

    private static final long serialVersionUID = -3165541953812379182L;

    /* 身份证号 */
    private String card;

    /* 地区 */
    private Region region;

    /* 性别 */
    private Sex sex;

    /* 生日 */
    private LocalDate birthday;

    public IdentificationCard(String card) {
        if (!isValid(card)) {
            throw new IllegalArgumentException("invalid identification card");
        }
        this.card = card;
    }

    public IdentificationCard(String card, RegionParser regionParser) {
        this(card);
        parse(regionParser);
    }

    @Override
    public String toString() {
        return card;
    }

    /**
     * 15位身份证
     *
     * @return
     */
    public boolean isLength15() {
        return card.length() == 15;
    }

    /**
     * 18位身份证
     *
     * @return
     */
    public boolean isLength18() {
        return card.length() == 18;
    }

    /**
     * 验证身份证号有效
     *
     * @param card
     * @return
     */
    public static boolean isValid(String card) {
        if (card != null && card.matches(Regex.IDENTIFICATION_CARD_RELAXED)) {
            if (card.length() == 15) {
                return true;
            } else {
                return card.charAt(17) == computeLastCode(card.substring(0, card.length() - 1));
            }
        }
        return false;
    }

    /**
     * 解析
     *
     * @param regionParser
     */
    public void parse(RegionParser regionParser) {
        if (regionParser != null) {
            region = regionParser.parseRegion(Integer.parseInt(card.substring(0, 6)));
        }
        switch (card.length()) {
            case 15: {
                birthday = LocalDate.parse("19" + card.substring(6, 12), DateTimeFormatter.ofPattern("yyyyMMdd"));
                sex = card.charAt(14) % 2 == 0 ? Sex.FEMALE : Sex.MALE;
            }
            break;
            case 18: {
                birthday = LocalDate.parse(card.substring(6, 14), DateTimeFormatter.ofPattern("yyyyMMdd"));
                sex = card.charAt(16) % 2 == 0 ? Sex.FEMALE : Sex.MALE;
            }
            break;
        }
    }

    /**
     * 获得年龄
     *
     * @return
     */
    public int getAge() {
        LocalDate now = LocalDate.now();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();

        boolean flag;
        if (birthday.getMonth().getValue() > month) {
            flag = true;
        } else if (birthday.getMonth().getValue() < month) {
            flag = false;
        } else {
            flag = birthday.getDayOfMonth() > day;
        }
        return now.getYear() - birthday.getYear() - (flag ? 1 : 0);
    }

    /**
     * 计算身份证最后一位校验码
     *
     * @param front17Chars 前17位数字字符串
     * @return 校验码
     */
    public static char computeLastCode(String front17Chars) {
        final int[] VERIFY_NUMBERS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        final char[] TARGET_CHARS = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        for (int i = 0; i < front17Chars.length(); i++) {
            sum += (front17Chars.charAt(i) - '0') * VERIFY_NUMBERS[i];
        }
        return TARGET_CHARS[sum % 11];
    }

    /**
     * 地区解析接口
     */
    public interface RegionParser {

        Region parseRegion(int code);
    }
}
