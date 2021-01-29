package develop.toolkit.base.components;

import develop.toolkit.base.struct.http.HttpClientSender;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Http发送助手
 *
 * @author qiushui on 2020-09-10.
 */
@Slf4j
@RequiredArgsConstructor
public final class HttpClientHelper {

    private final HttpClient httpClient;

    @Setter
    private boolean onlyPrintFailed = true;

    public HttpClientHelper(Duration connectTimeout) {
        this.httpClient = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    public HttpClientHelper() {
        this(Duration.ofSeconds(5L));
    }

    public HttpClientSender get(String url) {
        return new HttpClientSender(httpClient, "GET", url).onlyPrintFailed(onlyPrintFailed);
    }

    public HttpClientSender post(String url) {
        return new HttpClientSender(httpClient, "POST", url).onlyPrintFailed(onlyPrintFailed);
    }

    public HttpClientSender put(String url) {
        return new HttpClientSender(httpClient, "PUT", url).onlyPrintFailed(onlyPrintFailed);
    }

    public HttpClientSender delete(String url) {
        return new HttpClientSender(httpClient, "DELETE", url).onlyPrintFailed(onlyPrintFailed);
    }

    public HttpClientSender request(String method, String url) {
        return new HttpClientSender(httpClient, method, url).onlyPrintFailed(onlyPrintFailed);
    }
}
