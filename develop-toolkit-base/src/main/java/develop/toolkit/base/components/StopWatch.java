package develop.toolkit.base.components;

import develop.toolkit.base.utils.DateTimeAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秒表
 *
 * @author qiushui on 2019-03-17.
 */
public final class StopWatch {

    public static final String DEFAULT_NAME = "default";

    private Map<String, Instant> startInstantMap = new ConcurrentHashMap<>();

    private StopWatch() {
        start(DEFAULT_NAME);
    }

    public void start(String name) {
        startInstantMap.put(name, Instant.now());
    }

    public long end() {
        return end(DEFAULT_NAME);
    }

    public long end(String name) {
        final Instant end = Instant.now();
        return end.toEpochMilli() - startInstantMap.get(name).toEpochMilli();
    }

    public String formatEnd(String label) {
        return label + ": " + DateTimeAdvice.millisecondPretty(end());
    }

    public String formatEnd(String label, String name) {
        return label + ": " + DateTimeAdvice.millisecondPretty(end(name));
    }

    public static StopWatch start() {
        return new StopWatch();
    }
}
