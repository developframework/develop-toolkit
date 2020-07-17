package develop.toolkit.base.constants;

import java.time.format.DateTimeFormatter;

/**
 * 日期时间格式化常量
 *
 * @author qiushui on 2019-02-20.
 */
@SuppressWarnings("unused")
public interface DateFormatConstants {

    String STANDARD = "yyyy-MM-dd HH:mm:ss";
    String COMPACT = "yyyyMMddHHmmss";
    String DATE = "yyyy-MM-dd";
    String TIME = "HH:mm:ss";
    String MYSQL_FORMAT_DATETIME = "%Y-%m-%d %H:%i:%S";
    String MYSQL_FORMAT_DATE = "%Y-%m-%d";
    String MYSQL_FORMAT_TIME = "%H:%i:%s";
    String MYSQL_FORMAT_MONTH = "%Y-%m";

    DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern(STANDARD);
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE);
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME);
}
