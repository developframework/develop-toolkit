package develop.toolkit.http.request.body;

import java.nio.charset.Charset;

/**
 * Plain富文本Body
 *
 * @author qiuzhenhao
 */
public class PlainRawHttpRequestBody extends RawHttpRequestDataBody {

    public PlainRawHttpRequestBody(String text) {
        super(text);
    }

    @Override
    protected String getContentType(Charset charset) {
        return "text/plain;charset=" + charset.displayName();
    }
}
