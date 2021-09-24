package develop.toolkit.base.components;

import develop.toolkit.base.struct.http.HttpClientGlobalOptions;
import develop.toolkit.base.struct.http.HttpPostProcessor;
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
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Http发送助手
 *
 * @author qiushui on 2020-09-10.
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClientHelper {

    private final HttpClient httpClient;

    private final HttpClientGlobalOptions options;

    public static Builder builder() {
        return new Builder();
    }

    public static HttpClientHelper buildDefault() {
        return builder().build();
    }

    public HttpClientSender request(String method, String url) {
        return new HttpClientSender(httpClient, method, url, options);
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

        private SSLContext sslContext;

        private Duration connectTimeout = Duration.ofSeconds(10L);

        private InetSocketAddress proxyAddress;

        private Executor executor;

        private final HttpClientGlobalOptions globalOptions = new HttpClientGlobalOptions();

        public Builder onlyPrintFailed(boolean onlyPrintFailed) {
            globalOptions.onlyPrintFailed = onlyPrintFailed;
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

        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            globalOptions.readTimeout = readTimeout;
            return this;
        }

        public Builder addGlobalPostProcessor(HttpPostProcessor globalPostProcessor) {
            globalOptions.postProcessors.add(globalPostProcessor);
            return this;
        }

        public Builder constant(String key, String value) {
            globalOptions.constants.putConstant(key, value);
            return this;
        }

        public Builder constantMap(Map<String, String> constantMap) {
            globalOptions.constants.putConstantMap(constantMap);
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
                throw new IllegalStateException(e.getMessage());
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
            return new HttpClientHelper(builder.build(), globalOptions);
        }
    }
}
