package develop.toolkit.http.response;

import develop.toolkit.base.utils.CompareAdvice;

import java.io.IOException;

/**
 * 响应体Body处理器
 *
 * @param <T> 请求成功时的返回类型
 * @param <Y> 请求失败时的返回类型
 */
public interface HttpResponseDataBodyProcessor<T, Y> {

    /**
     * 实现如何判断请求成功
     */
    default boolean checkSuccess(HttpResponseData httpResponseData) throws IOException {
        return CompareAdvice.between(httpResponseData.getHttpStatus(), 200, 300);
    }

    /**
     * 实现如何解析请求成功时的Body
     */
    T parseBodyContent(byte[] data) throws IOException;

    /**
     * 实现请求失败时的处理
     */
    Y error(byte[] data) throws IOException;
}
