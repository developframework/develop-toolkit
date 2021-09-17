package develop.toolkit.base.struct.http;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qiushui on 2021-09-17.
 */
public class HttpClientGlobalOptions {

    public boolean onlyPrintFailed = true;

    public Duration readTimeout = Duration.ofSeconds(30L);

    public List<HttpPostProcessor> postProcessors = new LinkedList<>(List.of(new PrintLogHttpPostProcessor()));

    public HttpClientConstants constants = new HttpClientConstants();
}
