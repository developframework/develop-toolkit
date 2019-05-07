package develop.toolkit.world.normal;

import develop.toolkit.base.struct.TwoValues;
import lombok.Getter;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;

/**
 * 四季
 *
 * @author qiushui on 2019-04-28.
 */
public enum Season {

    SPRING(1),

    SUMMER(2),

    AUTUMN(3),

    WINTER(4);

    @Getter
    private int value;

    Season(int value) {
        this.value = value;
    }

    /**
     * 日期范围
     *
     * @return
     */
    public TwoValues<MonthDay, MonthDay> range() {
        switch (this) {
            case SPRING: {
                return new TwoValues<>(
                        MonthDay.of(1, 1),
                        MonthDay.of(3, 31)
                );
            }
            case SUMMER: {
                return new TwoValues<>(
                        MonthDay.of(4, 1),
                        MonthDay.of(6, 30)
                );
            }
            case AUTUMN: {
                return new TwoValues<>(
                        MonthDay.of(7, 1),
                        MonthDay.of(9, 30)
                );
            }
            case WINTER: {
                return new TwoValues<>(
                        MonthDay.of(10, 1),
                        MonthDay.of(12, 31)
                );
            }
            default:
                throw new AssertionError();
        }
    }

    /**
     * 这一天是这个季节的第几天
     *
     * @param day
     * @return
     */
    public int getDayOfSeason(LocalDate day) {
        if (isDayAt(MonthDay.from(day), this)) {
            return (int) range().getFirstValue().atYear(day.getYear()).until(day, ChronoUnit.DAYS);
        }
        return -1;
    }

    /**
     * 日期落在哪个季节
     *
     * @param monthDay
     * @return
     */
    public static Season dayAt(MonthDay monthDay) {
        for (Season season : Season.values()) {
            TwoValues<MonthDay, MonthDay> range = season.range();
            if (monthDay.compareTo(range.getFirstValue()) >= 0 && monthDay.compareTo(range.getSecondValue()) <= 0) {
                return season;
            }
        }
        throw new AssertionError();
    }

    /**
     * 日期是否是某个季节
     *
     * @param monthDay
     * @param season
     * @return
     */
    public static boolean isDayAt(MonthDay monthDay, Season season) {
        return dayAt(monthDay) == season;
    }

    /**
     * 值
     *
     * @param value
     * @return
     */
    public static Season valueOf(int value) {
        for (Season season : Season.values()) {
            if (season.getValue() == value) {
                return season;
            }
        }
        return null;
    }
}
