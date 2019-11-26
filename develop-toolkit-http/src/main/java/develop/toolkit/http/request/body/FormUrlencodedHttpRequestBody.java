package develop.toolkit.http.request.body;

import develop.toolkit.http.request.HttpRequestData;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * application/x-www-form-urlencoded 格式Body
 */
public class FormUrlencodedHttpRequestBody implements HttpRequestDataBody {

    @Getter
    protected Map<String, Object> parameters = new LinkedHashMap<>();

    public void addParameter(String parameterName, String value) {
        parameters.put(parameterName, value);
    }

    @Override
    public byte[] serializeBody(Charset charset) {
        return serializeParameters(charset).getBytes(charset);
    }

    @Override
    public void prepare(HttpRequestData httpRequestData) {
        httpRequestData.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + httpRequestData.getCharset().displayName());
    }

    /**
     * 序列化参数
     *
     * @return
     */
    protected String serializeParameters(Charset charset) {
        return parameters.entrySet().stream().map(parameter -> {
            try {
                return String.format("%s=%s", parameter.getKey(), URLEncoder.encode(parameter.getValue().toString(), charset.displayName()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }).collect(Collectors.joining("&"));
    }
}
