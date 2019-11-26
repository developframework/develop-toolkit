package develop.toolkit.http;

import develop.toolkit.http.request.HttpRequestData;
import develop.toolkit.http.response.HttpResponseData;
import develop.toolkit.http.response.HttpResponseDataBodyProcessor;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

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
    public <T, Y, P extends HttpResponseDataBodyProcessor<T, Y>> HttpResponseData<T, Y> request(HttpRequestData requestData, P httpResponseDataBodyProcessor) throws HttpFailedException {
        if (requestData.getBody() != null) {
            requestData.getBody().prepare(requestData);
        }
        final byte[] data = requestData.serializeBody();
        final HttpRequest.BodyPublisher bodyPublisher = data != null ? HttpRequest.BodyPublishers.ofByteArray(data) : HttpRequest.BodyPublishers.noBody();
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .timeout(Duration.ofSeconds(30L))
                .uri(URI.create(requestData.getWholeUrl()))
                .method(requestData.getHttpMethod().name(), bodyPublisher);
        requestData.getHeaders().forEach(httpRequestBuilder::header);

        try {
            final HttpResponse<byte[]> httpResponse = httpClient.send(httpRequestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());
            final HttpResponseData<T, Y> responseData = new HttpResponseData<>(httpResponse.statusCode(), httpResponse.body());
            responseData.parseHeaders(httpResponse.headers().map());
            responseData.setSuccess(httpResponseDataBodyProcessor.checkSuccess(responseData));
            if (responseData.isSuccess()) {
                responseData.setSuccessBody(httpResponseDataBodyProcessor.parseBodyContent(responseData));
            } else {
                responseData.setErrorBody(httpResponseDataBodyProcessor.error(responseData));
            }
            return responseData;
        } catch (Exception e) {
            throw new HttpFailedException("http failed: %s", e, e.getMessage());
        }
    }
}
