package develop.toolkit.http.request.body;

import java.nio.charset.Charset;

/**
 * Plain富文本Body
 *
 * @author qiuzhenhao
 */
public class PlainRawHttpRequestBody extends RawHttpRequestDataBody {

    private String text;

    public PlainRawHttpRequestBody(String text) {
        this.text = text;
    }

    @Override
    protected String getContentType(Charset charset) {
        return "text/plain;charset=" + charset.displayName();
    }

    @Override
    public byte[] serializeBody(Charset charset) {
        return text.getBytes(charset);
    }
}
