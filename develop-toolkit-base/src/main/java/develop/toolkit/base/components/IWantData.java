package develop.toolkit.base.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 我想要的数据
 *
 * @author qiushui on 2021-10-30.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IWantData<T> {

    private final boolean success;

    private final String message;

    private final T data;

    /**
     * 成功获取
     *
     * @param data 数据
     */
    public static <T> IWantData<T> ok(T data) {
        return new IWantData<>(true, "OK", data);
    }

    /**
     * 成功获取
     */
    public static <T> IWantData<T> ok() {
        return ok(null);
    }

    /**
     * 获取失败
     *
     * @param message 失败信息
     */
    public static <T> IWantData<T> fail(String message) {
        return new IWantData<>(false, message, null);
    }
}
