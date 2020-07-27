package develop.toolkit.base.utils;

import develop.toolkit.base.struct.HttpAdviceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Http增强工具
 */
@Slf4j
@SuppressWarnings("unused")
public final class HttpAdvice {

    /**
     * 默认的httpClient
     */
    public static HttpClient defaultHttpClient() {
        return HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5L))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * 带ssl的httpClient
     *
     * @param sslContext
     * @return
     */
    public static HttpClient sslHttpClient(SSLContext sslContext) {
        return HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5L))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .sslContext(sslContext)
                .build();
    }

    /**
     * 通用请求
     */
    public static HttpAdviceResponse request(
            String label,
            HttpClient httpClient,
            String httpMethod,
            String url,
            Map<String, String> headers,
            Map<String, Object> parameters,
            String content
    ) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, parameters),
                content
        );
    }

    /**
     * GET请求
     */
    public static HttpAdviceResponse get(String label, HttpClient httpClient, String url, Map<String, String> headers, Map<String, Object> parameters) throws IOException {
        return send(
                label,
                httpClient,
                "GET",
                builder(url, headers, parameters),
                null
        );
    }

    public static HttpAdviceResponse post(String label, HttpClient httpClient, String url, Map<String, String> headers, String content) throws IOException {
        return send(
                label,
                httpClient,
                "POST",
                builder(url, headers, null),
                content
        );
    }

    public static HttpAdviceResponse put(String label, HttpClient httpClient, String url, Map<String, String> headers, String content) throws IOException {
        return send(
                label,
                httpClient,
                "PUT",
                builder(url, headers, null),
                content
        );
    }

    public static HttpAdviceResponse delete(String label, HttpClient httpClient, String url, Map<String, String> headers, String content) throws IOException {
        return send(
                label,
                httpClient,
                "DELETE",
                builder(url, headers, null),
                content
        );
    }

    /**
     * 发送x-www-form-urlencoded格式请求
     */
    public static HttpAdviceResponse sendFormUrlencoded(String label, HttpClient httpClient, String httpMethod, String url, Map<String, String> headers, Map<String, Object> form) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, null).header("Content-Type", "application/x-www-form-urlencoded"),
                form
                        .entrySet()
                        .stream()
                        .filter(kv -> kv.getValue() != null)
                        .map(kv -> String.format("%s=%s", kv.getKey(), URLEncoder.encode(kv.getValue().toString(), StandardCharsets.UTF_8)))
                        .collect(Collectors.joining("&"))
        );
    }

    /**
     * 发送json请求
     */
    public static HttpAdviceResponse sendJson(String label, HttpClient httpClient, String httpMethod, String url, Map<String, String> headers, String json) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, null).header("Content-Type", "application/json;charset=UTF-8"),
                json
        );
    }

    /**
     * 发送xml请求
     */
    public static HttpAdviceResponse sendXml(String label, HttpClient httpClient, String httpMethod, String url, Map<String, String> headers, String xml) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, null).header("Content-Type", "application/xml;charset=UTF-8"),
                xml
        );
    }

    private static HttpRequest.Builder builder(String url, Map<String, String> headers, Map<String, Object> parameters) {
        if (parameters != null) {
            url += parameters
                    .entrySet()
                    .stream()
                    .filter(kv -> kv.getValue() != null)
                    .map(kv -> String.format("%s=%s", kv.getKey(), URLEncoder.encode(kv.getValue().toString(), StandardCharsets.UTF_8)))
                    .collect(Collectors.joining("&", "?", ""));
        }
        final HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(url));
        if (headers != null) {
            headers.forEach(builder::header);
        }
        return builder;
    }

    private static HttpAdviceResponse send(String label, HttpClient httpClient, String httpMethod, HttpRequest.Builder builder, String content) throws IOException {
        HttpAdviceResponse response = null;
        HttpRequest httpRequest = null;
        try {
            httpRequest = builder
                    .method(
                            httpMethod,
                            content == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8)
                    )
                    .timeout(Duration.ofSeconds(10L))
                    .build();

            Instant start = Instant.now();
            HttpResponse<byte[]> httpResponse = K.def(httpClient, HttpAdvice::defaultHttpClient).send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            Instant end = Instant.now();
            response = new HttpAdviceResponse(
                    httpResponse.statusCode(),
                    httpResponse.headers().map(),
                    httpResponse.body(),
                    start.until(end, ChronoUnit.MILLIS)
            );
            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (log.isDebugEnabled() && httpRequest != null) {
                StringBuilder sb = new StringBuilder();
                sb
                        .append("\nlabel: ").append(label)
                        .append("\nhttp request:\n    url: ")
                        .append(httpRequest.uri().toString()).append("\n    headers:\n");
                httpRequest
                        .headers()
                        .map()
                        .forEach((k, v) -> sb.append("        ").append(k).append(": ").append(StringUtils.join(v, ";")).append("\n"));
                sb.append("    body: ").append(content != null ? content : "(no content)").append("\n");
                if (response != null) {
                    sb.append(response.toString());
                }
                log.debug(sb.toString());
            }
        }
    }
}
