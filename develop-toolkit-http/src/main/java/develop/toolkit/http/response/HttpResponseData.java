package develop.toolkit.http.response;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Http响应体
 */
@Getter
@Setter
public class HttpResponseData {

    private int httpStatus;

    private byte[] data;

    private Map<String, List<String>> headers;

    private long costTime;

    public HttpResponseData(int httpStatus, byte[] data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public String stringBody() {
        return new String(data, StandardCharsets.UTF_8);
    }
}
