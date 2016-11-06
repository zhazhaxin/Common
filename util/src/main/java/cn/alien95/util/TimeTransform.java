package cn.alien95.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by linlongxin on 2016/1/17.
 * 时间戳类
 */
public class TimeTransform {

    private static final long YEAR_OF_MILLISECONDS = 1000 * 60 * 60 * 24 * 365;
    private static final long MOUTH_OF_MILLISECONDS = 1000 * 60 * 60 * 24 * 30;
    private static final long DAY_OF_MILLISECONDS = 1000 * 60 * 60 * 24;
    private static final long HOUR_OF_MILLISECONDS = 1000 * 60 * 60;
    private static final long MINUTES_OF_MILLISECONDS = 1000 * 60;

    /**
     * 按照格式转换成对应日期
     *
     * @param milliseconds 时间戳毫秒为单位
     * @param format       yyyy-MM-dd  HH:mm:ss 类似的日期
     * @return
     */
    public static String getCustomFormat(long milliseconds, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date(milliseconds);
        return formatter.format(date);
    }

    /**
     * 时间转换成星期几
     *
     * @param milliseconds 时间戳，单位毫秒
     * @return 返回星期几
     */
    public static int getDayOfWeek(long milliseconds) {
        Date date = new Date(milliseconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Calendar.DAY_OF_WEEK;
    }

    /**
     * 时间转换成对应日期 如：1月7日（周四）
     *
     * @param milliseconds
     * @return
     */
    public static String getDateAndDayOfWeek(long milliseconds) {
        Date date = new Date(milliseconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("MM月dd日");
        return formatter.format(calendar) + "(周" + Calendar.DAY_OF_WEEK + ")";

    }

    /**
     * 把时间戳转换成最近的时间
     *
     * @param milliseconds
     * @return
     */
    public static String getRecentlyDate(long milliseconds) {
        long currentMilliseconds = System.currentTimeMillis();
        long timeDistance = currentMilliseconds - milliseconds;
        SimpleDateFormat dateFormat;
        if (timeDistance > 0 && timeDistance < 1000) { //一秒内
            return "刚刚";
            //过去不超过3天的时间
        } else if (timeDistance > 1000 && timeDistance <= DAY_OF_MILLISECONDS * 3) {
            if (timeDistance / 1000 < 60) {
                return timeDistance / 1000 + "秒前";
            } else if (timeDistance / MINUTES_OF_MILLISECONDS < 60) {
                return timeDistance / MINUTES_OF_MILLISECONDS + "分钟前";
            } else if (timeDistance / HOUR_OF_MILLISECONDS < 24) {
                return timeDistance / HOUR_OF_MILLISECONDS + "小时前";
            } else if (timeDistance / DAY_OF_MILLISECONDS <= 3) {
                return timeDistance / DAY_OF_MILLISECONDS + "天前";
            }
            //将来不超过三天时间
        } else if (timeDistance < 0 && timeDistance >= DAY_OF_MILLISECONDS * -3) {
            timeDistance = timeDistance * -1;
            if (timeDistance / 1000 < 60) {
                return timeDistance / 1000 + "秒后";
            } else if (timeDistance / MINUTES_OF_MILLISECONDS < 60) {
                return timeDistance / MINUTES_OF_MILLISECONDS + "分钟后";
            } else if (timeDistance / HOUR_OF_MILLISECONDS < 24) {
                return timeDistance / HOUR_OF_MILLISECONDS + "小时后";
            } else if (timeDistance / DAY_OF_MILLISECONDS <= 3) {
                return timeDistance / DAY_OF_MILLISECONDS + "天后";
            }
            //过去或将来超过3天，但小于一年
        } else if (Math.abs(timeDistance) < YEAR_OF_MILLISECONDS) {
            dateFormat = new SimpleDateFormat("MM-dd");
            return dateFormat.format(new Date(milliseconds));
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getSimple(long milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        return simpleDateFormat.format(new Date(milliseconds));
    }

}
