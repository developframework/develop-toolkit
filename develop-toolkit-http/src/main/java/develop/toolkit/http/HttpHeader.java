package develop.toolkit.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Http头信息
 */
@Getter
@EqualsAndHashCode
public class HttpHeader {

    private String headerName;

    private String value;

    public HttpHeader(String headerName, String value) {
        this.headerName = headerName;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", headerName, value);
    }
}
