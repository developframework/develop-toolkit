package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.StringAdvice;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiushui on 2020-09-15.
 */
@RequiredArgsConstructor
public final class FormUrlencodedBody implements HttpRequestBody<String> {

    private final Map<String, Object> pairs;

    public FormUrlencodedBody() {
        pairs = new HashMap<>();
    }

    @Override
    public String getBody() {
        return StringAdvice.urlParametersFormat(pairs, false);
    }

    @Override
    public String toString() {
        return getBody();
    }

    public FormUrlencodedBody addPair(String name, Object value) {
        pairs.put(name, value);
        return this;
    }
}
