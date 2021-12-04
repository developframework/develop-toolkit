package develop.toolkit.base.components;

import develop.toolkit.base.struct.http.HttpClientReceiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 并发测试工具
 *
 * @author qiushui on 2021-12-04.
 */
public final class ConcurrentTesting {

    private final ExecutorService service;

    private final int cycleCount;

    private final int interval;

    private final HttpClientHelper helper;

    public ConcurrentTesting(int threadCount, int cycleCount, int interval) {
        this.helper = HttpClientHelper.buildDefault();
        this.service = Executors.newFixedThreadPool(threadCount);
        this.cycleCount = cycleCount;
        this.interval = interval;
    }

    public ConcurrentTesting(HttpClientHelper helper, int threadCount, int cycleCount, int interval) {
        this.helper = helper;
        this.service = Executors.newFixedThreadPool(threadCount);
        this.cycleCount = cycleCount;
        this.interval = interval;
    }

    public void start(Function<HttpClientHelper, HttpClientReceiver<String>> function) {
        start(
                function,
                receiver -> System.out.printf("【%s】%s\t%s%n", Thread.currentThread().getName(), receiver.getHttpStatus(), receiver.getBody())
        );
    }

    public void start(Function<HttpClientHelper, HttpClientReceiver<String>> function, Consumer<HttpClientReceiver<String>> consumer) {
        service.execute(() -> {
            for (int i = 0; i < cycleCount; i++) {
                if (interval > 0) {
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                consumer.accept(function.apply(helper));
            }
        });
    }
}
