import develop.toolkit.http.JDKToolkitHttpClient;
import develop.toolkit.http.ToolkitHttpClient;
import develop.toolkit.http.request.HttpMethod;
import develop.toolkit.http.request.HttpRequestData;
import develop.toolkit.http.request.body.FormDataHttpRequestBody;
import develop.toolkit.http.response.DefaultHttpResponseBodyProcessor;
import develop.toolkit.http.response.HttpResponseData;

public class Test {

    public static void main(String[] args) {
        ToolkitHttpClient client = new JDKToolkitHttpClient();
        HttpRequestData requestData = new HttpRequestData(HttpMethod.POST, "http://localhost:9090/data");
        String json = "{\"time\": \"2019-10-01 12:00:00\"}";
        FormDataHttpRequestBody form = new FormDataHttpRequestBody();
        form.addParameter("time", "2019-10-01 12:00:00");
        requestData.setBody(form);
        HttpResponseData<String, String> responseData = client.request(requestData, new DefaultHttpResponseBodyProcessor());
        if (responseData.isSuccess()) {
            System.out.println(responseData.getSuccessBody());
        }
    }
}
