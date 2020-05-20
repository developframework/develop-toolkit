package develop.toolkit.base.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpAdviceResponse {

    private int httpStatus;

    private Map<String, List<String>> headers;

    private byte[] body;

    private long costTime;

    public String bodyOfString() {
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
        sb.append("    body: ").append(bodyOfString());
        return sb.toString();
    }
}
