package develop.toolkit.base.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * 我想要的数据
 *
 * @author qiushui on 2021-10-30.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IWantData<V> {

    private final boolean success;

    private final String message;

    private final Map<String, V> data;

    /**
     * 成功获取
     *
     * @param data 数据
     */
    public static <V> IWantData<V> ok(Map<String, V> data) {
        return new IWantData<>(true, "OK", data);
    }

    /**
     * 获取失败
     *
     * @param message 失败信息
     */
    public static <V> IWantData<V> fail(String message) {
        return new IWantData<>(true, message, null);
    }
}
