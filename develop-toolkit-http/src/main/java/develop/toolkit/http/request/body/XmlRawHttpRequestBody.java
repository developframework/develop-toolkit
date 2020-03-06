package develop.toolkit.http.request.body;

import java.nio.charset.Charset;

/**
 * Xml富文本Body
 *
 * @author qiuzhenhao
 */
public class XmlRawHttpRequestBody extends RawHttpRequestDataBody {

    public XmlRawHttpRequestBody(String xml) {
        super(xml);
    }

    @Override
    protected String getContentType(Charset charset) {
        return "application/xml;charset=" + charset.displayName();
    }
}
