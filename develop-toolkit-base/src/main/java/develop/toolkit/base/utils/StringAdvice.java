package develop.toolkit.base.utils;

import develop.toolkit.base.struct.TwoValues;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字符串增强工具
 *
 * @author qiushui on 2018-09-06.
 */
@SuppressWarnings("unused")
public final class StringAdvice {

    /**
     * 判断是null和空
     */
    public static boolean isEmpty(String content) {
        return content == null || content.isEmpty();
    }

    /**
     * 判断不是null和空
     */
    public static boolean isNotEmpty(String content) {
        return content != null && !content.isEmpty();
    }

    /**
     * null的话默认为空字符串
     */
    public static String defaultEmpty(String content) {
        return content != null ? content : "";
    }

    /**
     * 空字符串的话默认为默认值
     */
    public static String emptyOr(String content, String defaultValue) {
        return isEmpty(content) ? defaultValue : content;
    }

    /**
     * 头尾添加字符串
     */
    public static String headTail(String content, String sign) {
        return sign + content + sign;
    }

    /**
     * 从index位置切断字符串
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
     */
    public static String cutTail(String string, String tail) {
        return string.endsWith(tail) ? string.substring(0, string.length() - tail.length()) : string;
    }

    /**
     * 切掉头部字符串
     */
    public static String cutHead(String string, String head) {
        return string.startsWith(head) ? string.substring(head.length()) : string;
    }

    /**
     * 正则取值
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
     */
    public static List<String> regexMatchStartEnd(String string, String start, String end) {
        return regexMatch(string, String.format("(?<=%s)(.+?)(?=%s)", start, end));
    }

    /**
     * 间隔美化
     */
    public static String intervalFormat(String separator, Object... objs) {
        return Stream.of(objs).map(o -> o == null ? "null" : o.toString()).collect(Collectors.joining(separator));
    }

    /**
     * 处理成url参数格式
     */
    public static String urlParametersFormat(Map<String, Object> parameters, boolean needQuestionMark) {
        if (parameters.isEmpty()) {
            return "";
        }
        return (needQuestionMark ? "?" : "") + parameters
                .entrySet()
                .stream()
                .filter(kv -> kv.getValue() != null)
                .map(kv -> String.format("%s=%s", kv.getKey(), URLEncoder.encode(kv.getValue().toString(), StandardCharsets.UTF_8)))
                .collect(Collectors.joining("&"));
    }
}
