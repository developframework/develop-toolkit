package develop.toolkit.http.response;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
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

    private Map<String, String> headers;

    public HttpResponseData(int httpStatus, byte[] data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public void parseHeaders(Map<String, List<String>> headerFields) {
        this.headers = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> headerFieldsEntry : headerFields.entrySet()) {
            final String headerName = headerFieldsEntry.getKey();
            headers.put(headerName, StringUtils.join(headerFieldsEntry.getValue()));
        }
    }
}
