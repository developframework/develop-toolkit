package develop.toolkit.support.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
