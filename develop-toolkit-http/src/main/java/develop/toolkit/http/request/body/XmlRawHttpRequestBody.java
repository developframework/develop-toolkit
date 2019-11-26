package develop.toolkit.http.request.body;

import java.nio.charset.Charset;

/**
 * Xml富文本Body
 *
 * @author qiuzhenhao
 */
public class XmlRawHttpRequestBody extends RawHttpRequestDataBody {

    private String xml;

    public XmlRawHttpRequestBody(String xml) {
        this.xml = xml;
    }

    @Override
    protected String getContentType(Charset charset) {
        return "application/xml;charset=" + charset.displayName();
    }

    @Override
    public byte[] serializeBody(Charset charset) {
        return xml.getBytes(charset);
    }
}
