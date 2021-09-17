package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.StringAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量可以被{{}}占位符取代
 *
 * @author qiushui on 2021-09-17.
 */
public final class HttpClientConstants {

    private final Map<String, String> constants = new HashMap<>();

    public void putConstant(String key, String value) {
        constants.put(key, value);
    }

    public void putConstantMap(Map<String, String> constantMap) {
        constants.putAll(constantMap);
    }

    public String replace(String string) {
        if (!constants.isEmpty()) {
            for (String key : StringAdvice.regexMatchStartEnd(string, "\\{\\{", "\\}\\}")) {
                final String value = constants.get(key);
                if (value != null) {
                    string = string.replace("{{" + key + "}}", value);
                }
            }
        }
        return string;
    }
}
