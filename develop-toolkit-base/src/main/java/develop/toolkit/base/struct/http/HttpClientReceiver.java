package develop.toolkit.base.struct.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author qiushui on 2020-09-10.
 */
@Getter
@AllArgsConstructor
public final class HttpClientReceiver {

    private final int httpStatus;

    private final Map<String, List<String>> headers;

    private final byte[] body;

    private final long costTime;

    public String stringBody() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public String getHeader(String header) {
        return StringUtils.join(headers.getOrDefault(header, List.of()), ";");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("\nhttp response:\n    status: ").append(httpStatus).append("\n    headers:\n");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            sb.append("        ").append(entry.getKey()).append(": ").append(StringUtils.join(entry.getValue(), ";")).append("\n");
        }
        sb.append("    body: ").append(stringBody());
        return sb.toString();
    }
}
