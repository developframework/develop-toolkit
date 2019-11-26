package develop.toolkit.http.request.body;

import develop.toolkit.http.request.HttpRequestData;

import java.nio.charset.Charset;

/**
 * 富文本Body
 */
public abstract class RawHttpRequestDataBody implements HttpRequestDataBody {

    @Override
    public void prepare(HttpRequestData httpRequestData) {
        httpRequestData.addHeader("Content-Type", getContentType(httpRequestData.getCharset()));
    }

    protected abstract String getContentType(Charset charset);

}
