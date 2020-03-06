module develop.toolkit.http {
    requires develop.toolkit.base;
    requires java.net.http;
    requires lombok;
    requires org.apache.commons.lang3;

    exports develop.toolkit.http.request.body;
    exports develop.toolkit.http.request;
    exports develop.toolkit.http.response;
    exports develop.toolkit.http;
}