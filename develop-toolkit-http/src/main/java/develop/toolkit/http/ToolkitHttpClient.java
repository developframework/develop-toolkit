package develop.toolkit.http;

import develop.toolkit.http.request.HttpRequestData;
import develop.toolkit.http.response.DefaultHttpResponseBodyProcessor;
import develop.toolkit.http.response.HttpResponseData;
import develop.toolkit.http.response.HttpResponseDataBodyProcessor;


public interface ToolkitHttpClient {

    /**
     * 发送请求
     *
     * @param <T>
     * @param httpRequestData
     * @param httpResponseDataBodyProcessor
     * @return
     */
    <T, Y, P extends HttpResponseDataBodyProcessor<T, Y>> HttpResponseData<T, Y> request(HttpRequestData httpRequestData, P httpResponseDataBodyProcessor) throws HttpFailedException;

    /**
     * @param httpRequestData
     * @return
     * @throws HttpFailedException
     */
    default HttpResponseData<String, String> request(HttpRequestData httpRequestData) throws HttpFailedException {
        return request(httpRequestData, new DefaultHttpResponseBodyProcessor());
    }
}
