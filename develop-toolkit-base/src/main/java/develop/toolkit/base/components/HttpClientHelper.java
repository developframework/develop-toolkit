package develop.toolkit.base.components;

import develop.toolkit.base.struct.http.HttpClientSender;
import develop.toolkit.base.utils.K;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;

/**
 * Http发送助手
 *
 * @author qiushui on 2020-09-10.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClientHelper {

    private final HttpClient httpClient;

    private final boolean onlyPrintFailed;

    public static Builder builder() {
        return new Builder();
    }

    public static HttpClientHelper buildDefault() {
        return builder().build();
    }

    public static HttpClientHelper buildCustomize(HttpClient httpClient, boolean onlyPrintFailed) {
        return new HttpClientHelper(httpClient, onlyPrintFailed);
    }

    public HttpClientSender request(String method, String url) {
        return new HttpClientSender(httpClient, method, url).onlyPrintFailed(onlyPrintFailed);
    }

    public HttpClientSender get(String url) {
        return request("GET", url);
    }

    public HttpClientSender post(String url) {
        return request("POST", url);
    }

    public HttpClientSender put(String url) {
        return request("PUT", url);
    }

    public HttpClientSender delete(String url) {
        return request("DELETE", url);
    }

    public static class Builder {

        private boolean onlyPrintFailed = true;

        private SSLContext sslContext;

        private Duration connectTimeout;

        private InetSocketAddress proxyAddress;

        public Builder onlyPrintFailed(boolean onlyPrintFailed) {
            this.onlyPrintFailed = onlyPrintFailed;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder proxyAddress(InetSocketAddress proxyAddress) {
            this.proxyAddress = proxyAddress;
            return this;
        }

        public Builder ssl(InputStream pkcs12, String password) {
            try {
                KeyStore ks = KeyStore.getInstance("PKCS12");
                char[] passwordChar = password.toCharArray();
                ks.load(pkcs12, passwordChar);
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, passwordChar);
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            } catch (Exception e) {
                throw new RuntimeException("read pkcs12 failed:" + e.getMessage());
            }
            return this;
        }

        public HttpClientHelper build() {
            final HttpClient.Builder builder = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .connectTimeout(K.def(connectTimeout, () -> Duration.ofSeconds(5L)));
            if (sslContext != null) {
                builder.sslContext(sslContext);
            }
            if (proxyAddress != null) {
                builder.proxy(ProxySelector.of(proxyAddress));
            }
            final HttpClient httpClient = builder.build();
            return new HttpClientHelper(httpClient, onlyPrintFailed);
        }
    }
}
