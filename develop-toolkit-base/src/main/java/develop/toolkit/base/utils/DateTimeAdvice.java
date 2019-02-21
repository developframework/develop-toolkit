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
public final class DateTimeAdvice {

    public static String now() {
        return format(LocalDateTime.now());
    }

    /**
     * 格式化Date
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, DateFormatConstants.STANDARD);
    }

    /**
     * 格式化Date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化LocalDateTime
     *
     * @param localDateTime
     * @return
     */
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DateFormatConstants.STANDARD);
    }

    /**
     * 格式化LocalDateTime
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * Date转到LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date转到LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return toLocalDateTime(date).toLocalDate();
    }

    /**
     * Date转到LocalTime
     *
     * @param date
     * @return
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        return toLocalDateTime(date).toLocalTime();
    }

    /**
     * Date转到Instant
     *
     * @param date
     * @return
     */
    public static Instant toInstant(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant();
    }

    /**
     * 从LocalDateTime转到Date
     *
     * @param localDateTime
     * @return
     */
    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 从LocalDate转到Date
     *
     * @param localDate
     * @return
     */
    public static Date fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        LocalTime localTime = LocalTime.of(0, 0, 0, 0);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return fromLocalDateTime(localDateTime);
    }

    /**
     * 从LocalTime转到Date
     *
     * @param localTime
     * @return
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
     *
     * @param instant
     * @return
     */
    public static Date fromInstant(Instant instant) {
        if (instant == null) {
            return null;
        }
        return Date.from(instant);
    }
}