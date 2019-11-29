package develop.toolkit.base.utils;

import develop.toolkit.base.struct.TwoValues;

/**
 * 字符串增强工具
 *
 * @author qiushui on 2018-09-06.
 * @since 0.1
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
}
