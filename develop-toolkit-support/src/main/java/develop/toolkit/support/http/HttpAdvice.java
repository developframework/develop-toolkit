package develop.toolkit.support.http;

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
    public static HttpAdviceResponse get(HttpClient httpClient, String url, Map<String, String> headers, Map<String, Object> parameters) throws IOException {
        return send(
                httpClient,
                builder(url, headers, parameters)
                        .GET()
                        .build()
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
    public static HttpAdviceResponse sendFormUrlencoded(HttpClient httpClient, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, Object> parameters, Map<String, String> form) throws IOException {
        return send(
                httpClient,
                builder(url, headers, parameters)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .method(
                                httpMethod.name(),
                                form == null ? HttpRequest.BodyPublishers.noBody() :
                                        HttpRequest.BodyPublishers.ofString(
                                                parameters
                                                        .entrySet()
                                                        .stream()
                                                        .map(kv -> String.format("%s=%s", kv.getKey(), kv.getValue()))
                                                        .collect(Collectors.joining("&")),
                                                StandardCharsets.UTF_8))
                        .build()
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
    public static HttpAdviceResponse sendJson(HttpClient httpClient, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, Object> parameters, String json) throws IOException {
        return send(
                httpClient,
                builder(url, headers, parameters)
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .method(
                                httpMethod.name(),
                                json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)
                        )
                        .build()
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
    public static HttpAdviceResponse sendXml(HttpClient httpClient, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, Object> parameters, String xml) throws IOException {
        return send(
                httpClient,
                builder(url, headers, parameters)
                        .header("Content-Type", "application/xml;charset=UTF-8")
                        .method(
                                httpMethod.name(),
                                xml == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(xml, StandardCharsets.UTF_8)
                        )
                        .build()
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

    private static HttpAdviceResponse send(HttpClient httpClient, HttpRequest httpRequest) throws IOException {
        try {
            HttpResponse<byte[]> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            return new HttpAdviceResponse(
                    httpResponse.statusCode(),
                    httpResponse.headers().map(),
                    httpResponse.body()
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
