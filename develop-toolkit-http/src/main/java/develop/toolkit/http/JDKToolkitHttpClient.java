package develop.toolkit.http;

import develop.toolkit.http.request.HttpRequestData;
import develop.toolkit.http.response.HttpResponseData;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

/**
 * 基于JDK 11 HttpClient实现
 */
public class JDKToolkitHttpClient implements ToolkitHttpClient {

    @Getter
    private HttpClient httpClient;

    public JDKToolkitHttpClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30L))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public JDKToolkitHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponseData request(HttpRequestData requestData) throws HttpFailedException {
        if (requestData.getBody() != null) {
            requestData.getBody().prepare(requestData);
        }
        final byte[] data = requestData.serializeBody();
        final HttpRequest.BodyPublisher bodyPublisher = data != null ? HttpRequest.BodyPublishers.ofByteArray(data) : HttpRequest.BodyPublishers.noBody();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .timeout(Duration.ofSeconds(30L))
                .uri(URI.create(requestData.getWholeUrl()))
                .method(requestData.getHttpMethod().name(), bodyPublisher);
        requestData.getHeaders().forEach(builder::header);

        try {
            Instant start = Instant.now();
            final HttpResponse<byte[]> httpResponse = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            final long costTime = Instant.now().toEpochMilli() - start.toEpochMilli();
            final HttpResponseData responseData = new HttpResponseData(httpResponse.statusCode(), httpResponse.body());
            responseData.setCostTime(costTime);
            responseData.setHeaders(httpResponse.headers().map());
            return responseData;
        } catch (Exception e) {
            throw new HttpFailedException("http failed: %s", e, e.getMessage());
        }
    }
}
