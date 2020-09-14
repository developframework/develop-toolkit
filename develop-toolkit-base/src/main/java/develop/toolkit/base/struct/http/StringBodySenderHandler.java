package develop.toolkit.base.struct.http;

import java.net.http.HttpResponse;

/**
 * @author qiushui on 2020-09-11.
 */
public class StringBodySenderHandler implements SenderHandler<String> {

    @Override
    public HttpResponse.BodyHandler<String> bodyHandler() {
        return HttpResponse.BodyHandlers.ofString();
    }
}
