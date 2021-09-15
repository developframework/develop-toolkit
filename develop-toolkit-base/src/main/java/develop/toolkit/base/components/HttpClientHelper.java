package develop.toolkit.base.components;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * Http发送助手
 *
 * @author qiushui on 2020-09-10.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClientHelper {

    private final HttpClient httpClient;

    private final boolean onlyPrintFailed;

    private final Duration readTimeout;

    public static Builder builder() {
        return new Builder();
    }

    public static HttpClientHelper buildDefault() {
        return builder().build();
    }

    public static HttpClientHelper buildCustomize(HttpClient httpClient, boolean onlyPrintFailed, Duration readTimeout) {
        return new HttpClientHelper(httpClient, onlyPrintFailed, readTimeout);
    }

    public HttpClientSender request(String method, String url) {
        return new HttpClientSender(httpClient, method, url, readTimeout).onlyPrintFailed(onlyPrintFailed);
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

        private Duration connectTimeout = Duration.ofSeconds(10L);

        private Duration readTimeout = Duration.ofSeconds(30L);

        private InetSocketAddress proxyAddress;

        private Executor executor;

        public Builder onlyPrintFailed(boolean onlyPrintFailed) {
            this.onlyPrintFailed = onlyPrintFailed;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder proxyAddress(InetSocketAddress proxyAddress) {
            this.proxyAddress = proxyAddress;
            return this;
        }

        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder ssl(InputStream pkcs12, String password) {
            try {
                KeyStore ks = KeyStore.getInstance("PKCS12");
                char[] passwordChars = password.toCharArray();
                ks.load(pkcs12, passwordChars);
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, passwordChars);
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
                    .connectTimeout(connectTimeout);
            if (sslContext != null) {
                builder.sslContext(sslContext);
            }
            if (proxyAddress != null) {
                builder.proxy(ProxySelector.of(proxyAddress));
            }
            if (executor != null) {
                builder.executor(executor);
            }
            final HttpClient httpClient = builder.build();
            return new HttpClientHelper(httpClient, onlyPrintFailed, readTimeout);
        }
    }
}
