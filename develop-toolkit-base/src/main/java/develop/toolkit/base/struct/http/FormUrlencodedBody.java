package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.StringAdvice;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiushui on 2020-09-15.
 */
@RequiredArgsConstructor
public final class FormUrlencodedBody {

    private final Map<String, Object> pairs;

    public FormUrlencodedBody() {
        pairs = new HashMap<>();
    }

    public String buildBody() {
        return StringAdvice.urlParametersFormat(pairs, false);
    }

    public FormUrlencodedBody addPair(String name, Object value) {
        pairs.put(name, value);
        return this;
    }
}
