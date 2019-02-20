package develop.toolkit.base.utils;

/**
 * @author qiushui on 2018-10-03.
 * @since 0.1
 */
public class JavaBeanUtils {

    /**
     * 根据属性名称和java类型，获取对应的getter方法名
     *
     * @param property
     * @param javaType
     * @return
     */
    public static String getGetterMethodName(String property, Class<?> javaType) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        if (javaType == boolean.class || javaType == Boolean.class) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }
        return sb.toString();
    }

    /**
     * 根据属性名称获取对应的setter方法名称
     *
     * @param property
     * @return
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param camelcaseString
     * @return
     */
    public static String camelcaseToUnderline(String camelcaseString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelcaseString.length(); i++) {
            char c = camelcaseString.charAt(i);
            if (i != 0 && c >= 'A' && c <= 'Z') {
                sb.append('_').append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转中划线
     *
     * @param camelcaseString
     * @return
     */
    public static String camelcaseToMiddleLine(String camelcaseString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelcaseString.length(); i++) {
            char c = camelcaseString.charAt(i);
            if (i != 0 && c >= 'A' && c <= 'Z') {
                sb.append('-').append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param underlineString
     * @return
     */
    public static String underlineToCamelcase(String underlineString) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < underlineString.length(); i++) {
            char c = underlineString.charAt(i);
            if (c == '_') {
                nextUpperCase = i != 0;
                continue;
            }
            if (nextUpperCase) {
                sb.append((char) (c - 32));
                nextUpperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 中划线转驼峰
     *
     * @param middleLineString
     * @return
     */
    public static String middleLineToCamelcase(String middleLineString) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < middleLineString.length(); i++) {
            char c = middleLineString.charAt(i);
            if (c == '-') {
                nextUpperCase = i != 0;
                continue;
            }
            if (nextUpperCase) {
                sb.append((char) (c - 32));
                nextUpperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
