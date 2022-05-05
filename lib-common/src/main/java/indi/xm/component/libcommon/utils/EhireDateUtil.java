package indi.xm.component.libcommon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: albert.fang
 * @date: 2021/12/28 13:37
 */
public class EhireDateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final int DATE_LENGTH = 12;
    public static final String YY_MM = "yyyy.MM";

    private static final DateTimeFormatter FORMAT_LONG_TRANS = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    private static final DateTimeFormatter FORMAT_YYMM_TRANS = DateTimeFormatter.ofPattern(YY_MM);
    private static final DateTimeFormatter FORMAT_YYMMDD_TRANS = DateTimeFormatter.ofPattern(YYYY_MM_DD);

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param str:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:23
     */
    public static Date getDate(String str) {
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * YYYY_MM_DD
     *
     * @param str:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:23
     */
    public static Date getDate2(String str) {
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);

        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

    /**
     * yyyy-MM-dd HH:mm:ss 或 YYYY_MM_DD
     *
     * @param str:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:23
     */
    public static Date getDate12(String str) {
        try {
            if (str.length() > DATE_LENGTH) {
                return getDate(str);
            } else {
                return getDate2(str);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转日期
     *
     * @param str:
     * @param formatStr:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:24
     */
    public static Date getDate(String str, String formatStr) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * 日期转字符串 YYYY_MM_DD_HH_MM_SS
     *
     * @param d:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:27
     */
    public static String dateFormat(Date d) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(d);
    }

    /**
     * 日期转字符串 YYYY_MM_DD
     *
     * @param d:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:27
     */
    public static String dateFormat2(Date d) {
        return new SimpleDateFormat(YYYY_MM_DD).format(d);
    }

    /**
     * 日期转字符串
     *
     * @param d:
     * @param str:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:26
     */
    public static String dateFormat(Date d, String str) {
        return new SimpleDateFormat(str).format(d);
    }

    /**
     * date转LocalDateTime
     *
     * @param date:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:29
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * localDateTime转Date
     *
     * @param localDateTime:
     * @return :
     * @author: albert.fang
     * @date: 2021/12/28 14:29
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 得到当前时间，截取到日期
     * get now to day
     *
     * @return 返回的是截取之后的日期类型对象 而并非是字符串对象
     */
    public static Date getDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        String format = sdf.format(date);
        Date parse = null;
        try {
            parse = sdf.parse(format);
        } catch (ParseException e) {
            return null;
        }
        return parse;
    }

    /**
     * 以 <span>d</span> 为界限，得到之后或者之前的日期
     * get after or past d many days
     *
     * @param d    指定日期
     * @param days 多少天，传入正数表示d之后的多少天，传入负数表示d之前的多少天
     * @return 日期
     */
    public static Date getAfOrPadMaDs(Date d, int days) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(d);
        instance.add(Calendar.DATE, days);
        return instance.getTime();
    }

    /**
     * 以 <span>d</span> 为界限，得到之后或者之前的日期
     * get after or past d many minutes
     *
     * @param d                                   指定日期
     * @param minutes，传入正数表示d之后的多少分，传入负数表示d之前的多少天
     * @return 日期
     */
    public static Date getAfOrPadMaMs(Date d, int minutes) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(d);
        instance.add(Calendar.MINUTE, minutes);
        return instance.getTime();
    }

    /**
     * 以 <span>d</span> 为界限，得到之后或者之前的日期
     * get after or past d many months
     *
     * @param d                                    指定日期
     * @param months，传入正数表示d之后的多少个月，传入负数表示d之前的多少个月
     * @return 日期
     */
    public static Date getAfOrPadMaMonths(Date d, int months) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(d);
        instance.add(Calendar.MONTH, months);
        return instance.getTime();
    }

    /**
     * 将时间戳还原成日期
     * timeStamp 13位时间戳
     *
     * @param timeStamp
     * @return
     */
    public static Date getDateFromTimestamp(Long timeStamp) {
        return new Date(timeStamp);
    }

    /**
     * 获取当前时间戳
     *
     * @param getSec 是否截取到秒，true 到秒
     * @return java.lang.String 时间戳字符串
     * @author Albert.fang
     * @date 2022/1/28 10:35
     */
    public static Long getTimeSpan(boolean getSec) {
        long currentTimeMillis = System.currentTimeMillis();
        if (getSec) {
            return currentTimeMillis / 1000;
        }
        return currentTimeMillis;
    }


    /**
     * 今天结束的秒数
     *
     * @return long
     * @author cheng.liang
     * @date 2022/1/12 11:13
     */
    public static long toTodaySecond() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(now.plusDays(1).toLocalDate(), LocalTime.of(0, 0, 0));
        return Duration.between(now, end).getSeconds();
    }

    /**
     * 获取当前时间的将来整点的天
     *
     * @param number :
     * @return java.util.Date
     * @author cheng.liang
     * @date 2022/2/11 14:10
     */
    public static Date getFutureZeroDate(int number) {
        LocalDate localDate = LocalDate.now().plusDays(number);
        return localDateTime2Date(LocalDateTime.of(localDate, LocalTime.of(0, 0, 0)));
    }

    /**
     * 获取当前日期年份
     *
     * @return 返回服务器当前时间年份
     */
    public static int getYear() {
        return LocalDate.now().getYear();
    }

    /**
     * @return 返回服务器当前时间年月日
     */
    public static String getYmd() {
        return LocalDate.now().format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * @return 返回服务器当前时间前一年的年月日
     */
    public static String getYmdLastYear() {
        return LocalDate.now().minusYears(1).format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * @return 返回当前时间前一周的年月日
     */
    public static String getYmdLastWeek() {
        return LocalDate.now().minusWeeks(1).format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * @return 返回当前时间前两周的年月日
     */
    public static String getYmdLastTwoWeek() {
        return LocalDate.now().minusWeeks(2).format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * @return 返回当前时间前一个月的年月日
     */
    public static String getYmdLastMonth() {
        return LocalDate.now().minusMonths(1).format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * @return 返回当前时间前两个月的年月日
     */
    public static String getYmdLastTwoMonth() {
        return LocalDate.now().minusMonths(2).format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * @return 返回当前时间前半年（六个月）的年月日
     */
    public static String getYmdLastHalfYear() {
        return LocalDate.now().minusMonths(6).format(FORMAT_YYMMDD_TRANS);
    }

    /**
     * 时间戳格式转换为固定格式日期字符
     *
     * @param time:
     * @return :
     */
    public static String timeStampFormat(String time) {
        String format;
        try {
            format = LocalDate.parse(time, FORMAT_LONG_TRANS).format(FORMAT_YYMM_TRANS);
        } catch (DateTimeParseException var1) {
            format = "";
        }
        return format;
    }
}
