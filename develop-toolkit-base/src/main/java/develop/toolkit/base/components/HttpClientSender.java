package develop.toolkit.base.components;

import develop.toolkit.base.struct.http.*;
import develop.toolkit.base.utils.StringAdvice;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Http发送器
 *
 * @author qiushui on 2020-09-10.
 */
@Getter
public final class HttpClientSender {

    private final HttpClient httpClient;

    private final String method;

    private final String url;

    private final Map<String, String> headers = new LinkedHashMap<>();

    private final Map<String, Object> parameters = new LinkedHashMap<>();

    private final List<HttpPostProcessor> postProcessors;

    private Duration readTimeout;

    private String debugLabel;

    private HttpRequestBody<?> requestBody;

    private String requestStringBody;

    private boolean onlyPrintFailed;

    private URI uri;

    private final HttpClientConstants constants;

    protected HttpClientSender(HttpClient httpClient, String method, String url, HttpClientGlobalOptions options) {
        this.httpClient = httpClient;
        this.method = method;
        this.readTimeout = options.readTimeout;
        this.postProcessors = new LinkedList<>(options.postProcessors);
        this.constants = options.constants;
        this.url = constants.replace(url);
    }

    public HttpClientSender header(String header, String value) {
        this.headers.put(header, constants.replace(value));
        return this;
    }

    public HttpClientSender headers(Map<String, String> customHeaders) {
        if (customHeaders != null) {
            customHeaders.forEach((k, v) -> this.headers.put(k, constants.replace(v)));
        }
        return this;
    }

    public HttpClientSender headerAuthorization(String value) {
        this.headers.put("Authorization", constants.replace(value));
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

    public HttpClientSender addPostProcessor(HttpPostProcessor postProcessor) {
        postProcessors.add(postProcessor);
        return this;
    }

    public HttpClientSender bodyJson(String json) {
        headers.put("Content-Type", "application/json;charset=utf-8");
        this.requestBody = new RawRequestBody(json);
        return this;
    }

    public HttpClientSender bodyXml(String xml) {
        headers.put("Content-Type", "application/xml;charset=utf-8");
        this.requestBody = new RawRequestBody(xml);
        return this;
    }

    public HttpClientSender bodyText(String text) {
        headers.put("Content-Type", "text/plain;charset=utf-8");
        this.requestBody = new RawRequestBody(text);
        return this;
    }

    public HttpClientSender bodyBytes(byte[] bytes) {
        this.requestBody = new ByteRequestBody(bytes);
        return this;
    }

    public HttpClientSender bodyMultiPartFormData(MultiPartFormDataBody multiPartFormDataBody) {
        headers.put("Content-Type", "multipart/form-data; boundary=" + multiPartFormDataBody.getBoundary());
        this.requestBody = multiPartFormDataBody;
        return this;
    }

    public HttpClientSender bodyFormUrlencoded(FormUrlencodedBody formUrlencodedBody) {
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        this.requestBody = formUrlencodedBody;
        return this;
    }

    public void download(Path path, OpenOption... openOptions) {
        send(HttpResponse.BodyHandlers::ofByteArray).ifSuccess(r -> r.save(path, openOptions));
    }

    public HttpClientReceiver<String> send() {
        return send(new StringBodySenderHandler());
    }

    public CompletableFuture<HttpClientReceiver<String>> sendAsync() {
        return sendAsync(new StringBodySenderHandler());
    }

    /**
     * 核心发送逻辑
     *
     * @param senderHandler 发送器扩展逻辑
     * @param <BODY>        响应内容
     * @return receiver
     */
    public <BODY> HttpClientReceiver<BODY> send(SenderHandler<BODY> senderHandler) {
        this.uri = URI.create(url + StringAdvice.urlParametersFormat(parameters, true));
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(httpClient.version())
                .uri(uri);
        headers.forEach(builder::header);
        final HttpRequest request = builder
                .method(method, senderHandler.bodyPublisher(requestBody))
                .timeout(readTimeout)
                .build();
        requestStringBody = HttpRequestBody.bodyToString(requestBody);
        final HttpClientReceiver<BODY> receiver = new HttpClientReceiver<>();
        Instant start = Instant.now();
        try {
            HttpResponse<BODY> response = httpClient.send(request, senderHandler.bodyHandler());
            receiver.setHttpStatus(response.statusCode());
            receiver.setHeaders(response.headers().map());
            receiver.setBody(response.body());
        } catch (HttpConnectTimeoutException e) {
            receiver.setConnectTimeout(true);
        } catch (HttpTimeoutException e) {
            receiver.setReadTimeout(true);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            receiver.setErrorMessage(e.getMessage());
        } finally {
            receiver.setCostTime(start.until(Instant.now(), ChronoUnit.MILLIS));
            for (HttpPostProcessor postProcessor : postProcessors) {
                postProcessor.process(this, receiver);
            }
        }

        return receiver;
    }

    /**
     * 核心发送逻辑（异步）
     *
     * @param senderHandler 发送器扩展逻辑
     * @param <BODY>        响应内容
     * @return completableFuture
     */
    public <BODY> CompletableFuture<HttpClientReceiver<BODY>> sendAsync(SenderHandler<BODY> senderHandler) {
        this.uri = URI.create(url + StringAdvice.urlParametersFormat(parameters, true));
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(httpClient.version())
                .uri(uri);
        headers.forEach(builder::header);
        final HttpRequest request = builder
                .method(method, senderHandler.bodyPublisher(requestBody))
                .timeout(readTimeout)
                .build();
        requestStringBody = HttpRequestBody.bodyToString(requestBody);
        final Instant start = Instant.now();
        return httpClient
                .sendAsync(request, senderHandler.bodyHandler())
                .handle((response, e) -> {
                    final HttpClientReceiver<BODY> receiver = new HttpClientReceiver<>();
                    if (e == null) {
                        receiver.setHttpStatus(response.statusCode());
                        receiver.setHeaders(response.headers().map());
                        receiver.setBody(response.body());
                    } else if (e instanceof HttpConnectTimeoutException) {
                        receiver.setConnectTimeout(true);
                    } else if (e instanceof HttpTimeoutException) {
                        receiver.setReadTimeout(true);
                    } else if (e instanceof InterruptedException || e instanceof IOException) {
                        e.printStackTrace();
                        receiver.setErrorMessage(e.getMessage());
                    }
                    receiver.setCostTime(start.until(Instant.now(), ChronoUnit.MILLIS));
                    doPostProcessors(receiver);
                    return receiver;
                });
    }

    private <BODY> void doPostProcessors(HttpClientReceiver<BODY> receiver) {
        for (HttpPostProcessor postProcessor : postProcessors) {
            postProcessor.process(this, receiver);
        }
    }
}
