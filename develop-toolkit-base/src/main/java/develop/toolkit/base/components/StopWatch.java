package develop.toolkit.base.components;

import develop.toolkit.base.utils.DateTimeAdvice;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秒表
 *
 * @author qiushui on 2019-03-17.
 */
@SuppressWarnings("unused")
public final class StopWatch {

    public static final String DEFAULT_NAME = "default";

    private final Map<String, Instant> startInstantMap = new ConcurrentHashMap<>();

    private StopWatch(String name) {
        pause(name);
    }

    public void pause(String name) {
        startInstantMap.put(name, Instant.now());
    }

    public long end() {
        return end(DEFAULT_NAME);
    }

    public long end(String name) {
        return startInstantMap.get(name).until(Instant.now(), ChronoUnit.MILLIS);
    }

    public long interval(String startName, String endName) {
        return startInstantMap.get(startName).until(startInstantMap.get(endName), ChronoUnit.MILLIS);
    }

    public String formatEnd() {
        return DateTimeAdvice.millisecondPretty(end());
    }

    public String formatEnd(String label) {
        return label + ": " + DateTimeAdvice.millisecondPretty(end());
    }

    public String formatEnd(String label, String name) {
        return label + ": " + DateTimeAdvice.millisecondPretty(end(name));
    }

    public static StopWatch start() {
        return new StopWatch(DEFAULT_NAME);
    }

    public static StopWatch start(String name) {
        return new StopWatch(name);
    }
}
