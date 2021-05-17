package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.DateTimeAdvice;
import develop.toolkit.base.utils.StringAdvice;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
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

    public HttpClientSender parameter(String parameter, Object value) {
        this.parameters.put(parameter, value);
        return this;
    }

    public HttpClientSender parameters(Map<String, Object> parameters) {
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
        this.requestBody = formUrlencodedBody.buildBody();
        return this;
    }

    public HttpClientSender bodyBytes(byte[] bytes) {
        this.requestBody = bytes;
        return this;
    }

    public void download(Path path, OpenOption... openOptions) {
        send(HttpResponse.BodyHandlers::ofByteArray).ifSuccess(r -> r.save(path, openOptions));
    }

    public HttpClientReceiver<String> send() {
        return send(new StringBodySenderHandler());
    }

    /**
     * 核心发送逻辑
     *
     * @param senderHandler 发送器扩展逻辑
     * @param <BODY>        响应内容
     * @return receiver
     */
    public <BODY> HttpClientReceiver<BODY> send(SenderHandler<BODY> senderHandler) {
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(httpClient.version())
                .uri(URI.create(url + StringAdvice.urlParametersFormat(parameters, true)));
        headers.forEach(builder::header);
        final HttpRequest request = builder
                .method(method, requestBody == null ? HttpRequest.BodyPublishers.noBody() : senderHandler.bodyPublisher(requestBody))
                .timeout(readTimeout)
                .build();
        final HttpClientReceiver<BODY> receiver = new HttpClientReceiver<>();
        try {
            Instant start = Instant.now();
            HttpResponse<BODY> response = httpClient.send(request, senderHandler.bodyHandler());
            receiver.setCostTime(start.until(Instant.now(), ChronoUnit.MILLIS));
            receiver.setHttpStatus(response.statusCode());
            receiver.setHeaders(response.headers().map());
            receiver.setBody(response.body());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (HttpConnectTimeoutException e) {
            receiver.setConnectTimeout(true);
        } catch (HttpTimeoutException e) {
            receiver.setReadTimeout(true);
        } catch (IOException e) {
            e.printStackTrace();
            receiver.setErrorMessage(e.getMessage());
        } finally {
            if (log.isDebugEnabled() && (!onlyPrintFailed || !receiver.isSuccess())) {
                printLog(request, receiver);
            }
        }
        return receiver;
    }

    private String printBody(Object body) {
        if (body == null) {
            return "(No content)";
        } else if (body instanceof String) {
            return (String) body;
        } else {
            return "(Binary byte data)";
        }
    }

    private void printLog(HttpRequest request, HttpClientReceiver<?> receiver) {
        StringBuilder sb = new StringBuilder("\n=========================================================================================================\n");
        sb
                .append("\nlabel: ").append(debugLabel == null ? "(Undefined)" : debugLabel)
                .append("\nhttp request:\n  method: ").append(method).append("\n  url: ")
                .append(request.uri().toString()).append("\n  headers:\n");
        request
                .headers()
                .map()
                .forEach((k, v) -> sb.append("    ").append(k).append(": ").append(StringUtils.join(v, ";")).append("\n"));
        sb.append("  body: ").append(printBody(requestBody)).append("\n").append("\nhttp response:\n");
        if (receiver.isConnectTimeout()) {
            sb.append("  (connect timeout ").append(httpClient.connectTimeout().map(Duration::getSeconds).orElse(0L)).append("s)");
        } else if (receiver.isReadTimeout()) {
            sb.append("  (read timeout ").append(readTimeout.getSeconds()).append("s)");
        } else if (receiver.getErrorMessage() != null) {
            sb.append("  (ioerror ").append(receiver.getErrorMessage()).append(")");
        } else if (receiver.getHeaders() != null) {
            sb.append("  status: ").append(receiver.getHttpStatus()).append("\n  headers:\n");
            for (Map.Entry<String, List<String>> entry : receiver.getHeaders().entrySet()) {
                sb.append("    ").append(entry.getKey()).append(": ").append(StringUtils.join(entry.getValue(), ";")).append("\n");
            }
            sb.append("  cost: ").append(DateTimeAdvice.millisecondPretty(receiver.getCostTime())).append("\n");
            sb.append("  body: ").append(printBody(receiver.getBody()));
        }
        sb.append("\n\n=========================================================================================================\n");
        log.debug(sb.toString());
    }
}
