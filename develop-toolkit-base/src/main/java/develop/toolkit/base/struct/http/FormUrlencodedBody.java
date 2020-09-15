package develop.toolkit.base.struct.http;

import develop.toolkit.base.struct.KeyValuePairs;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author qiushui on 2020-09-15.
 */
public class FormUrlencodedBody {

    private final KeyValuePairs<String, Object> pairs = new KeyValuePairs<>();

    public HttpRequest.BodyPublisher buildBodyPublisher() {
        return HttpRequest.BodyPublishers.ofString(
                pairs
                        .stream()
                        .filter(pair -> pair.getValue() != null)
                        .map(pair -> pair.getKey() + "=" + URLEncoder.encode(pair.getValue().toString(), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&"))
        );
    }

    public FormUrlencodedBody addPair(String name, Object value) {
        pairs.addKeyValue(name, value);
        return this;
    }
}
