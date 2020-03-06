package develop.toolkit.http.request.body;

import develop.toolkit.http.request.HttpRequestData;
import lombok.Getter;

import java.nio.charset.Charset;

/**
 * 富文本Body
 */
public abstract class RawHttpRequestDataBody implements HttpRequestDataBody {

    @Getter
    private String raw;

    public RawHttpRequestDataBody(String raw) {
        this.raw = raw;
    }

    @Override
    public void prepare(HttpRequestData httpRequestData) {
        httpRequestData.addHeader("Content-Type", getContentType(httpRequestData.getCharset()));
    }

    protected abstract String getContentType(Charset charset);

    @Override
    public String body(Charset charset) {
        return raw;
    }

}
