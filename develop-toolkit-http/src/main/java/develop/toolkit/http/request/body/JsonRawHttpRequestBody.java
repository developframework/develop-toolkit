package develop.toolkit.http.request.body;

import java.nio.charset.Charset;

/**
 * Json富文本Body
 */
public class JsonRawHttpRequestBody extends RawHttpRequestDataBody {

    private String json;

    public JsonRawHttpRequestBody(String json) {
        this.json = json;
    }

    @Override
    protected String getContentType(Charset charset) {
        return "application/json;charset=" + charset.displayName();
    }

    @Override
    public byte[] serializeBody(Charset charset) {
        return json.getBytes(charset);
    }
}
