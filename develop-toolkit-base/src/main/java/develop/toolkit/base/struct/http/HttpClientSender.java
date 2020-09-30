package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.DateTimeAdvice;
import develop.toolkit.base.utils.StringAdvice;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private Object requestBody;

    private boolean onlyPrintFailed;

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
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public HttpClientSender headerAuthorization(String value) {
        this.headers.put("Authorization", value);
        return this;
    }

    public HttpClientSender headerContentType(String value) {
        this.headers.put("Content-Type", value);
        return this;
    }

    public HttpClientSender parameter(String parameter, String value) {
        this.parameters.put(parameter, value);
        return this;
    }

    public HttpClientSender parameters(Map<String, String> parameters) {
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
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

    public HttpClientSender onlyPrintFailed(boolean onlyPrintFailed) {
        this.onlyPrintFailed = onlyPrintFailed;
        return this;
    }

    public HttpClientSender bodyJson(String json) {
        headers.put("Content-Type", "application/json;charset=utf-8");
        this.requestBody = json;
        return this;
    }

    public HttpClientSender bodyXml(String xml) {
        headers.put("Content-Type", "application/xml;charset=utf-8");
        this.requestBody = xml;
        return this;
    }

    public HttpClientSender bodyText(String text) {
        this.requestBody = text;
        return this;
    }

    public HttpClientSender bodyMultiPartFormData(MultiPartFormDataBody multiPartFormDataBody) {
        headers.put("Content-Type", "multipart/form-data; boundary=" + multiPartFormDataBody.getBoundary());
        this.requestBody = multiPartFormDataBody.buildBodyPublisher();
        return this;
    }

    public HttpClientSender bodyFormUrlencoded(FormUrlencodedBody formUrlencodedBody) {
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        this.requestBody = formUrlencodedBody.buildBodyPublisher();
        return this;
    }

    public HttpClientSender bodyBytes(byte[] bytes) {
        this.requestBody = bytes;
        return this;
    }

    public void download(Path path, OpenOption... openOptions) throws IOException {
        send(HttpResponse.BodyHandlers::ofByteArray).save(path, openOptions);
    }

    public void downloadQuietly(Path path, OpenOption... openOptions) {
        sendQuietly(HttpResponse.BodyHandlers::ofByteArray)
                .ifPresent(receiver -> receiver.save(path, openOptions));
    }

    public HttpClientReceiver<String> send() throws IOException {
        return send(new StringBodySenderHandler());
    }

    public Optional<HttpClientReceiver<String>> sendQuietly() {
        try {
            return Optional.of(send(new StringBodySenderHandler()));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public <BODY> Optional<HttpClientReceiver<BODY>> sendQuietly(SenderHandler<BODY> senderHandler) {
        try {
            return Optional.of(send(senderHandler));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public <BODY> HttpClientReceiver<BODY> send(SenderHandler<BODY> senderHandler) throws IOException {
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(httpClient.version())
                .uri(URI.create(url + StringAdvice.urlParametersFormat(parameters, true)));
        headers.forEach(builder::header);
        final HttpRequest request = builder
                .method(method, requestBody == null ? HttpRequest.BodyPublishers.noBody() : senderHandler.bodyPublisher(requestBody))
                .timeout(readTimeout)
                .build();
        HttpClientReceiver<BODY> receiver = null;
        try {
            Instant start = Instant.now();
            HttpResponse<BODY> response = httpClient.send(request, senderHandler.bodyHandler());
            Instant end = Instant.now();
            receiver = new HttpClientReceiver<>(
                    response.statusCode(),
                    response.headers().map(),
                    response.body(),
                    start.until(end, ChronoUnit.MILLIS)
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (log.isDebugEnabled() && (!onlyPrintFailed || receiver == null || !receiver.isSuccess())) {
                printDebug(request, receiver);
            }
        }
        return receiver;
    }

    private String printBody(Object body) {
        if (body == null) {
            return "(No content)";
        } else if (body instanceof String) {
            return (String) body;
        } else if (body.getClass().isArray()) {
            return "(Bytes data)";
        } else {
            return "(Unknown data)";
        }
    }

    private void printDebug(HttpRequest request, HttpClientReceiver<?> receiver) {
        StringBuilder sb = new StringBuilder("\n=========================================================================================================\n");
        sb
                .append("\nlabel: ").append(debugLabel == null ? "(Undefined)" : debugLabel)
                .append("\nhttp request:\n  url: ")
                .append(request.uri().toString()).append("\n  headers:\n");
        request
                .headers()
                .map()
                .forEach((k, v) -> sb.append("      ").append(k).append(": ").append(StringUtils.join(v, ";")).append("\n"));
        sb.append("  body: ").append(printBody(requestBody)).append("\n");
        if (receiver != null) {
            sb
                    .append("\nhttp response:\n  status: ").append(receiver.getHttpStatus()).append("\n    headers:\n");
            for (Map.Entry<String, List<String>> entry : receiver.getHeaders().entrySet()) {
                sb.append("      ").append(entry.getKey()).append(": ").append(StringUtils.join(entry.getValue(), ";")).append("\n");
            }
            sb.append("  cost: ").append(DateTimeAdvice.millisecondPretty(receiver.getCostTime())).append("\n");
            sb.append("  body: ").append(printBody(receiver.getBody()));
        }
        sb.append("\n\n=========================================================================================================\n");
        log.debug(sb.toString());
    }
}
