package develop.toolkit.http;

import develop.toolkit.http.request.HttpRequestData;
import develop.toolkit.http.response.HttpResponseData;


public interface ToolkitHttpClient {

    /**
     * 发送请求
     *
     * @param httpRequestData
     * @return
     */
    HttpResponseData request(HttpRequestData httpRequestData) throws HttpFailedException;
}
