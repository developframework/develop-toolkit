package develop.toolkit.http.request.body;

import develop.toolkit.http.request.HttpRequestData;

import java.nio.charset.Charset;

/**
 *
 */
public interface HttpRequestDataBody {

    /**
     * 序列化成字节
     *
     * @param charset
     * @return
     */
    default byte[] serializeBody(Charset charset) {
        return body(charset).getBytes();
    }

    /**
     * 字符串格式body
     *
     * @return
     */
    String body(Charset charset);

    /**
     * 准备
     *
     * @param httpRequestData
     */
    void prepare(HttpRequestData httpRequestData);
}
