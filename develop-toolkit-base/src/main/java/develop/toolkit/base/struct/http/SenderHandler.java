package develop.toolkit.base.struct.http;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author qiushui on 2020-09-11.
 */
public interface SenderHandler<BODY, E> {

    HttpRequest.BodyPublisher bodyPublisher(Object requestBody);

    HttpResponse.BodyHandler<BODY> bodyHandler();

    E convert(BODY body);
}
