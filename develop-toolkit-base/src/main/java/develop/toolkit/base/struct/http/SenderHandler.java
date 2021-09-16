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

    default HttpRequest.BodyPublisher bodyPublisher(HttpRequestBody<?> requestBody) {
        if (requestBody == null) {
            return HttpRequest.BodyPublishers.noBody();
        } else if (requestBody instanceof RawRequestBody) {
            return HttpRequest.BodyPublishers.ofString(((RawRequestBody) requestBody).getBody());
        } else if (requestBody instanceof FormUrlencodedBody) {
            return HttpRequest.BodyPublishers.ofString(((FormUrlencodedBody) requestBody).getBody());
        } else if (requestBody instanceof ByteRequestBody) {
            return HttpRequest.BodyPublishers.ofByteArray(((ByteRequestBody) requestBody).getBody());
        } else if (requestBody instanceof MultiPartFormDataBody) {
            return HttpRequest.BodyPublishers.ofByteArray(((MultiPartFormDataBody) requestBody).getBody());
        } else {
            throw new AssertionError();
        }
    }

    HttpResponse.BodyHandler<BODY> bodyHandler();
}
