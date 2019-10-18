package develop.toolkit.support.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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

    public String ofString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public <T> T parseJson(Class<T> clazz, ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(body, clazz);
    }

    public <T> T parseXml(Class<T> clazz, XmlMapper xmlMapper) throws IOException {
        return xmlMapper.readValue(body, clazz);
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
        sb.append("    body: ").append(ofString());
        return sb.toString();
    }
}
