package develop.toolkit.base.components;

import develop.toolkit.base.struct.http.HttpClientSender;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Http发送助手
 *
 * @author qiushui on 2020-09-10.
 */
@Slf4j
public final class HttpClientHelper {

    private final HttpClient httpClient;

    public HttpClientHelper(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

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
        return new HttpClientSender(httpClient, "GET", url);
    }

    public HttpClientSender post(String url) {
        return new HttpClientSender(httpClient, "POST", url);
    }

    public HttpClientSender put(String url) {
        return new HttpClientSender(httpClient, "PUT", url);
    }

    public HttpClientSender delete(String url) {
        return new HttpClientSender(httpClient, "DELETE", url);
    }
}
