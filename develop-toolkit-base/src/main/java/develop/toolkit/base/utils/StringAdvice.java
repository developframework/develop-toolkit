package develop.toolkit.base.utils;

import develop.toolkit.base.struct.TwoValues;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串增强工具
 *
 * @author qiushui on 2018-09-06.
 */
public final class StringAdvice {

    /**
     * 判断是null和空
     *
     * @param content
     * @return
     */
    public static boolean isEmpty(String content) {
        return content == null || content.isEmpty();
    }

    /**
     * 判断不是null和空
     *
     * @param content
     * @return
     */
    public static boolean isNotEmpty(String content) {
        return content != null && !content.isEmpty();
    }

	/**
	 * 从index位置切断字符串
	 * @param string
	 * @param index
	 * @return
	 */
    public static TwoValues<String, String> cutOff(String string, int index) {
        if (index > string.length() || index < 0) {
            return null;
		}
        return TwoValues.of(
                string.substring(0, index),
                string.substring(index)
        );
	}

    /**
     * 切掉尾部字符串
     *
     * @param string
     * @param tail
     * @return
     */
    public static String cutTail(String string, String tail) {
        return string.endsWith(tail) ? string.substring(0, string.length() - tail.length()) : string;
    }

    /**
     * 切掉头部字符串
     *
     * @param string
     * @param head
     * @return
     */
    public static String cutHead(String string, String head) {
        return string.startsWith(head) ? string.substring(head.length() + 1) : string;
    }

    /**
     * 正则取值
     *
     * @param string
     * @param regex
     * @return
     */
    public static List<String> regexMatch(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        List<String> list = new ArrayList<>(matcher.groupCount());
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 正则开头结尾匹配抓取中间字符串值
     *
     * @param string
     * @param start
     * @param end
     * @return
     */
    public static List<String> regexMatchStartEnd(String string, String start, String end) {
        return regexMatch(string, String.format("(?<=%s)(.+?)(?=%s)", start, end));
    }
}
