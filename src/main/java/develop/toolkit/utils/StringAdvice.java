package develop.toolkit.utils;

/**
 * 字符串增强工具
 *
 * @author qiushui on 2018-09-06.
 * @since 0.1
 */
public final class StringAdvice {

	/**
	 * 从index位置切断字符串
	 * @param string
	 * @param index
	 * @return
	 */
	public static String[] cutOff(String string, int index) {
        if (index > string.length() || index < 0) {
			throw new IllegalArgumentException();
		}
		String[] array = new String[2];
		array[0] = string.substring(0, index);
		array[1] = string.substring(index);
		return array;
	}
}
