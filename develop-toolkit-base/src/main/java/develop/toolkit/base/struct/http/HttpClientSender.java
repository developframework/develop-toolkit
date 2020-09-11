package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.StringAdvice;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Http发送器
 *
 * @author qiushui on 2020-09-10.
 */
@Slf4j
@Getter
public final class HttpClientSender {

    private final HttpClient httpClient;

    private final String method;

    private final String url;

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, Object> parameters = new HashMap<>();

    private Duration readTimeout = Duration.ofSeconds(10L);

    private String debugLabel;

    private Object body;

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

    public HttpClientSender bodyJson(String json) {
        headers.put("Content-Type", "application/json;charset=utf-8");
        this.body = json;
        return this;
    }

    public HttpClientSender bodyXml(String xml) {
        headers.put("Content-Type", "application/xml;charset=utf-8");
        this.body = xml;
        return this;
    }

    public HttpClientSender bodyBytes(byte[] bytes) {
        this.body = bytes;
        return this;
    }

    public <T> HttpClientReceiver<T> send(HttpResponse.BodyHandler<T> bodyHandler) throws IOException {
        return send(bodyPublisher(body), bodyHandler);
    }

    public <T> HttpClientReceiver<T> send(HttpRequest.BodyPublisher bodyPublisher, HttpResponse.BodyHandler<T> bodyHandler) throws IOException {
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(httpClient.version())
                .uri(URI.create(url + StringAdvice.urlParametersFormat(parameters, true)));
        headers.forEach(builder::header);
        final HttpRequest request = builder
                .method(method, bodyPublisher)
                .timeout(readTimeout)
                .build();
        HttpClientReceiver<T> receiver = null;
        try {
            Instant start = Instant.now();
            HttpResponse<T> response = httpClient.send(request, bodyHandler);
            Instant end = Instant.now();
            receiver = new HttpClientReceiver<>(response, start.until(end, ChronoUnit.MILLIS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (log.isDebugEnabled()) {
                String content;
                if (body == null) {
                    content = "(No content)";
                } else if (body instanceof String) {
                    content = (String) body;
                } else if (body.getClass().isArray()) {
                    content = "(Bytes data)";
                } else {
                    content = "(Unknown data)";
                }
                StringBuilder sb = new StringBuilder();
                sb
                        .append("\nlabel: ").append(debugLabel == null ? "(未定义)" : debugLabel)
                        .append("\nhttp request:\n    url: ")
                        .append(request.uri().toString()).append("\n    headers:\n");
                request
                        .headers()
                        .map()
                        .forEach((k, v) -> sb.append("        ").append(k).append(": ").append(StringUtils.join(v, ";")).append("\n"));
                sb.append("    body: ").append(content).append("\n");
                if (receiver != null) {
                    sb.append(receiver.toString());
                }
                log.debug(sb.toString());
            }
        }
        return receiver;
    }

    private HttpRequest.BodyPublisher bodyPublisher(Object body) {
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        } else if (body instanceof String) {
            return HttpRequest.BodyPublishers.ofString((String) body);
        } else if (body.getClass().isArray()) {
            return HttpRequest.BodyPublishers.ofByteArray((byte[]) body);
        } else {
            throw new AssertionError();
        }
    }
}
