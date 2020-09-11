package develop.toolkit.base.struct.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Http接收器
 *
 * @author qiushui on 2020-09-10.
 */
@Getter
@AllArgsConstructor
public final class HttpClientReceiver<T> {

    private final int httpStatus;

    private final Map<String, List<String>> headers;

    private final T body;

    private final long costTime;

    public String getHeader(String header) {
        return StringUtils.join(headers.getOrDefault(header, List.of()), ";");
    }
}
