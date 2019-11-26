package develop.toolkit.http.request;

import develop.toolkit.http.request.body.HttpRequestDataBody;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Http请求体
 *
 * @author qiuzhenhao
 */
@Getter
public class HttpRequestData {

    private HttpMethod httpMethod;

    private String url;

    private Map<String, String> headers = new HashMap<>();

    private Map<String, Object> urlParameters = new HashMap<>();

    @Setter
    private Charset charset = StandardCharsets.UTF_8;

    @Setter
    private HttpRequestDataBody body;

    public HttpRequestData(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public void addHeader(String headerName, String value) {
        headers.put(headerName, value);
    }

    public void addUrlParameter(String parameterName, Object value) {
        urlParameters.put(parameterName, value);
    }

    public String getWholeUrl() {
        return urlParameters.isEmpty() ? url : (url + "?" + serializeParameters());
    }

    /**
     * 序列化参数
     *
     * @return
     */
    protected String serializeParameters() {
        return urlParameters.entrySet().stream().map(parameter -> {
            try {
                return String.format("%s=%s", parameter.getKey(), URLEncoder.encode(parameter.getValue().toString(), charset.displayName()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }).collect(Collectors.joining("&"));
    }

    /**
     * 序列化
     *
     * @return
     */
    public byte[] serializeBody() {
        if (body == null) {
            return null;
        }
        return body.serializeBody(charset);
    }
}
