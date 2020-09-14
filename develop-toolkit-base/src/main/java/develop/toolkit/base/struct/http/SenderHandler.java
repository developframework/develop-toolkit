package develop.toolkit.base.struct.http;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author qiushui on 2020-09-11.
 */
@FunctionalInterface
public interface SenderHandler<BODY> {

    default HttpRequest.BodyPublisher bodyPublisher(Object requestBody) {
        return HttpRequest.BodyPublishers.ofString((String) requestBody);
    }

    HttpResponse.BodyHandler<BODY> bodyHandler();
}
