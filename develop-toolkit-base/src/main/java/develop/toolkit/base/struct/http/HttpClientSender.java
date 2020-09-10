package develop.toolkit.base.struct.http;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiushui on 2020-09-10.
 */
@Getter
public final class HttpClientSender {

    private final HttpClient httpClient;

    private final String method;

    private final String url;

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, String> parameters = new HashMap<>();

    private Duration readTimeout = Duration.ofSeconds(10L);

    private String debugLabel;

    private byte[] body;

    public HttpClientSender(HttpClient httpClient, String method, String url) {
        this.httpClient = httpClient;
        this.method = method;
        this.url = url;
    }

    public HttpClientSender header(String header, String value) {
        this.headers.put(header, value);
        return this;
    }

    public HttpClientSender headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpClientSender parameter(String parameter, String value) {
        this.parameters.put(parameter, value);
        return this;
    }

    public HttpClientSender parameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public HttpClientSender readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpClientSender debugLabel(String debugLabel) {
        this.debugLabel = debugLabel;
        return this;
    }

    public HttpClientSender body(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpClientSender body(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public HttpClientReceiver send() throws IOException, InterruptedException {
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(httpClient.version())
                .uri(URI.create(url));
        final HttpRequest request = builder
                .method(
                        method,
                        body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(body)
                )
                .timeout(readTimeout)
                .build();
        Instant start = Instant.now();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        Instant end = Instant.now();
        return new HttpClientReceiver(
                response.statusCode(),
                response.headers().map(),
                response.body(),
                start.until(end, ChronoUnit.MILLIS)
        );
    }
}
