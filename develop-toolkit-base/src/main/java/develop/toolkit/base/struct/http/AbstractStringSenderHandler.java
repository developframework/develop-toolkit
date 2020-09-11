package develop.toolkit.base.struct.http;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author qiushui on 2020-09-11.
 */
public abstract class AbstractStringSenderHandler<T> implements SenderHandler<String, T> {

    @Override
    public HttpRequest.BodyPublisher bodyPublisher(Object requestBody) {
        return HttpRequest.BodyPublishers.ofString((String) requestBody);
    }

    @Override
    public HttpResponse.BodyHandler<String> bodyHandler() {
        return HttpResponse.BodyHandlers.ofString();
    }
}
