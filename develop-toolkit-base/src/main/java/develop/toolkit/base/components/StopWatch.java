package develop.toolkit.base.components;

import develop.toolkit.base.utils.DateTimeAdvice;

import java.time.Instant;

/**
 * 秒表
 *
 * @author qiushui on 2019-03-17.
 */
public final class StopWatch {

    private Instant start;

    private Instant end;

    private StopWatch() {
        start = Instant.now();
    }

    public String end() {
        end = Instant.now();
        final long millisecond = end.toEpochMilli() - start.toEpochMilli();
        return DateTimeAdvice.millisecondPretty(millisecond);
    }

    public static StopWatch start() {
        return new StopWatch();
    }
}
