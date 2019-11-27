package develop.toolkit.http.response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 简单型响应体处理器
 * 转化为字符串
 *
 * @author qiuzhenhao
 */
public class DefaultHttpResponseBodyProcessor implements HttpResponseDataBodyProcessor<String, String> {

    @Override
    public String parseBodyContent(byte[] data) throws IOException {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public String error(byte[] data) throws IOException {
        return new String(data, StandardCharsets.UTF_8);
    }
}
