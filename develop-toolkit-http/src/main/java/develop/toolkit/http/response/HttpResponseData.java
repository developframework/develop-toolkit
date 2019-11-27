package develop.toolkit.http.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Http响应体
 *
 * @author qiuzhenhao
 */
@Getter
@Setter
public class HttpResponseData<T, Y> {

    private int httpStatus;

    private byte[] data;

    private boolean success;

    private T successBody;

    private Y errorBody;

    private Map<String, List<String>> headers;

    public HttpResponseData(int httpStatus, byte[] data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
