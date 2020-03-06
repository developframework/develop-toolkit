package develop.toolkit.http.request.body;

import develop.toolkit.http.request.HttpRequestData;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qiuzhenhao
 */
public class FormDataHttpRequestBody implements HttpRequestDataBody {

    @Getter
    private String boundary;

    @Getter
    protected Map<String, Object> parameters = new LinkedHashMap<>();

    public void addParameter(String parameterName, String value) {
        parameters.put(parameterName, value);
    }

    public FormDataHttpRequestBody() {
        this.boundary = "----WebKitFormBoundary" + RandomStringUtils.randomAlphabetic(10);
    }

    public FormDataHttpRequestBody(String boundary) {
        this.boundary = boundary;
    }

    @Override
    public String body(Charset charset) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (entry != null) {
                sb
                        .append(boundary)
                        .append("\r\n")
                        .append(String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n%s\r\n", entry.getKey(), URLEncoder.encode(entry.getValue().toString(), charset)));
            }
        }
        return sb.toString();
    }

    @Override
    public void prepare(HttpRequestData httpRequestData) {
        httpRequestData.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }
}
