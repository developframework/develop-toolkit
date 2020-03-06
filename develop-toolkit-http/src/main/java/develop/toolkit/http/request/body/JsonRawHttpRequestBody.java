package develop.toolkit.http.request.body;

import java.nio.charset.Charset;

/**
 * Json富文本Body
 */
public class JsonRawHttpRequestBody extends RawHttpRequestDataBody {

    public JsonRawHttpRequestBody(String json) {
       super(json);
    }

    @Override
    protected String getContentType(Charset charset) {
        return "application/json;charset=" + charset.displayName();
    }
}
