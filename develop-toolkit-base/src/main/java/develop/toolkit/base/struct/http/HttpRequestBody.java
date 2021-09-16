package develop.toolkit.base.struct.http;

/**
 * @author qiushui on 2021-09-16.
 */
public interface HttpRequestBody<BODY> {

    BODY getBody();

    static String bodyToString(HttpRequestBody<?> requestBody) {
        return requestBody == null ? "(No content)" : requestBody.toString();
    }
}
