package develop.toolkit.base.struct.http;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 发送器扩展逻辑
 *
 * @author qiushui on 2020-09-11.
 */
@FunctionalInterface
public interface SenderHandler<BODY> {

    default HttpRequest.BodyPublisher bodyPublisher(Object requestBody) {
        if (requestBody instanceof HttpRequest.BodyPublisher) {
            return (HttpRequest.BodyPublisher) requestBody;
        } else if (requestBody instanceof String) {
            return HttpRequest.BodyPublishers.ofString((String) requestBody);
        } else if (requestBody.getClass().isArray()) {
            return HttpRequest.BodyPublishers.ofByteArray((byte[]) requestBody);
        } else {
            throw new AssertionError();
        }
    }

    HttpResponse.BodyHandler<BODY> bodyHandler();
}
