package develop.toolkit.base.components;

import develop.toolkit.base.utils.DateTimeAdvice;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 批量任务计时
 *
 * @author qiushui on 2020-07-23.
 */
@SuppressWarnings("unused")
public final class BatchTask {

    @Getter
    private final int total;

    @Getter
    private int current;

    private long sumCostTime;

    private Instant start;

    public BatchTask(int total) {
        this.total = total;
    }

    /**
     * 开始任务
     */
    public void start() {
        start = Instant.now();
        current++;
    }

    /**
     * 完成单次任务
     */
    public String finishOnce(String message) {
        if (current > total) {
            throw new IllegalStateException("The task have been finished.");
        }
        final long costTime = start.until(Instant.now(), ChronoUnit.MILLIS);
        sumCostTime += costTime;

        final long avgTime = sumCostTime / current;
        final long surplus = (total - current) * avgTime;
        LocalDateTime finishTime = LocalDateTime.now().plusSeconds(surplus / 1000);
        return String.format(
                "%d/%d\t(%.02f%%) [cur: %s | avg: %s | sum: %s | sur: %s]\tfinish at: %s - %s",
                current,
                total,
                (float) current / (float) total * 100,
                DateTimeAdvice.millisecondPretty(costTime),
                DateTimeAdvice.millisecondPretty(avgTime),
                DateTimeAdvice.millisecondPretty(sumCostTime),
                DateTimeAdvice.millisecondPretty(surplus),
                DateTimeAdvice.format(finishTime),
                message
        );
    }
}
