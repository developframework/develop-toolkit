package develop.toolkit.support.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Http增强工具
 */
@Slf4j
public final class HttpAdvice {

    /**
     * GET请求
     *
     * @param httpClient
     * @param url
     * @param headers
     * @param parameters
     * @return
     * @throws IOException
     */
    public static HttpAdviceResponse get(String label, HttpClient httpClient, String url, Map<String, String> headers, Map<String, Object> parameters) throws IOException {
        return send(
                label,
                httpClient,
                HttpMethod.GET,
                builder(url, headers, parameters),
                null
        );
    }

    /**
     * 发送x-www-form-urlencoded格式请求
     *
     * @param httpClient
     * @param httpMethod
     * @param url
     * @param headers
     * @param parameters
     * @param form
     * @return
     * @throws IOException
     */
    public static HttpAdviceResponse sendFormUrlencoded(String label, HttpClient httpClient, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, Object> parameters, Map<String, String> form) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, parameters).header("Content-Type", "application/x-www-form-urlencoded"),
                form
                        .entrySet()
                        .stream()
                        .map(kv -> String.format("%s=%s", kv.getKey(), kv.getValue()))
                        .collect(Collectors.joining("&"))
        );
    }

    /**
     * 发送json请求
     *
     * @param httpClient
     * @param httpMethod
     * @param url
     * @param headers
     * @param parameters
     * @param json
     * @return
     * @throws IOException
     */
    public static HttpAdviceResponse sendJson(String label, HttpClient httpClient, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, Object> parameters, String json) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, parameters).header("Content-Type", "application/json;charset=UTF-8"),
                json
        );
    }

    /**
     * 发送xml请求
     *
     * @param httpClient
     * @param httpMethod
     * @param url
     * @param headers
     * @param parameters
     * @param xml
     * @return
     * @throws IOException
     */
    public static HttpAdviceResponse sendXml(String label, HttpClient httpClient, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, Object> parameters, String xml) throws IOException {
        return send(
                label,
                httpClient,
                httpMethod,
                builder(url, headers, parameters).header("Content-Type", "application/xml;charset=UTF-8"),
                xml
        );
    }

    private static HttpRequest.Builder builder(String url, Map<String, String> headers, Map<String, Object> parameters) {
        if (parameters != null) {
            url += parameters
                    .entrySet()
                    .stream()
                    .map(kv -> String.format("%s=%s", kv.getKey(), kv.getValue()))
                    .collect(Collectors.joining("&", "?", ""));
        }
        HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(url));
        if (headers != null) {
            headers.forEach(builder::header);
        }
        return builder;
    }

    private static HttpAdviceResponse send(String label, HttpClient httpClient, HttpMethod httpMethod, HttpRequest.Builder builder, String content) throws IOException {
        HttpAdviceResponse response = null;
        HttpRequest httpRequest = null;
        try {
            httpRequest = builder.method(
                    httpMethod.name(),
                    content == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8)
            ).build();
            HttpResponse<byte[]> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            response = new HttpAdviceResponse(
                    httpResponse.statusCode(),
                    httpResponse.headers().map(),
                    httpResponse.body()
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
