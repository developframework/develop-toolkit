package develop.toolkit.base.utils;

import develop.toolkit.base.constants.DateFormatConstants;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间增强工具
 *
 * @author qiushui on 2019-02-20.
 */
@SuppressWarnings("unused")
public final class DateTimeAdvice {

    public static String now() {
        return format(LocalDateTime.now());
    }

    /**
     * 格式化Date
     */
    public static String format(Date date) {
        return format(date, DateFormatConstants.STANDARD);
    }

    /**
     * 解析标准日期时间字符串
     */
    public static LocalDateTime parseStandard(CharSequence dateStr) {
        return LocalDateTime.parse(dateStr, DateFormatConstants.STANDARD_FORMATTER);
    }

    /**
     * 格式化Date
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化LocalDateTime
     */
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DateFormatConstants.STANDARD);
    }

    /**
     * 格式化LocalDateTime
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * Date转到LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date转到LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return toLocalDateTime(date).toLocalDate();
    }

    /**
     * Date转到LocalTime
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        return toLocalDateTime(date).toLocalTime();
    }

    /**
     * Date转到Instant
     */
    public static Instant toInstant(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant();
    }

    /**
     * 从LocalDateTime转到Date
     */
    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 从LocalDate转到Date
     */
    public static Date fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return fromLocalDateTime(localDate.atTime(LocalTime.MIN));
    }

    /**
     * 从LocalTime转到Date
     */
    public static Date fromLocalTime(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), localTime);
        return fromLocalDateTime(localDateTime);
    }

    /**
     * 从Instant转到Date
     */
    public static Date fromInstant(Instant instant) {
        if (instant == null) {
            return null;
        }
        return Date.from(instant);
    }

    /**
     * 判断是否同一个月
     */
    public static boolean isSameMonth(LocalDate day1, LocalDate day2) {
        return day1.getYear() == day2.getYear() && day1.getMonth().getValue() == day2.getMonth().getValue();
    }

    /**
     * 毫秒美化
     */
    public static String millisecondPretty(long millisecond) {
        if (millisecond < 1000) {
            return millisecond + "ms";
        } else if (millisecond < 60_000) {
            return (millisecond / 1000) + "s" + (millisecond % 1000) + "ms";
        } else if (millisecond < 3600_000) {
            long minute = millisecond / 60_000;
            long second = (millisecond - minute * 60_000) / 1000;
            long milli = millisecond % 1000;
            return minute + "m" + second + "s" + milli + "ms";
        } else if (millisecond < 86_400_000) {
            long hour = millisecond / (3600_000);
            long minute = (millisecond - hour * 3600_000) / 60_000;
            long second = (millisecond - hour * 3600_000 - minute * 60_000) / 1000;
            long milli = millisecond % 1000;
            return hour + "h" + minute + "m" + second + "s" + milli + "ms";
        } else {
            long day = millisecond / (86_400_000);
            long hour = (millisecond - day * 86_400_000) / (3600_000);
            long minute = (millisecond - day * 86_400_000 - hour * 3600_000) / 60_000;
            long second = (millisecond - day * 86_400_000 - hour * 3600_000 - minute * 60_000) / 1000;
            long milli = millisecond % 1000;
            return day + "d" + hour + "h" + minute + "m" + second + "s" + milli + "ms";
        }
    }
}
