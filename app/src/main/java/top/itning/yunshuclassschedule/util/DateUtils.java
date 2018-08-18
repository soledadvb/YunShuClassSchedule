package top.itning.yunshuclassschedule.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 时间工具
 *
 * @author itning
 */
public class DateUtils {
    private static final String TAG = "DateUtils";
    private static final SimpleDateFormat DF = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private static final List<String> TIME_LIST = new ArrayList<>();
    private static final Calendar CAL = Calendar.getInstance();

    static {
        TIME_LIST.add("08:20-09:50");
        TIME_LIST.add("10:05-11:35");
        TIME_LIST.add("12:55-14:25");
        TIME_LIST.add("14:40-16:10");
        TIME_LIST.add("17:30-20:00");
    }

    private DateUtils() {
    }

    /**
     * 获取上课进度
     *
     * @param max 最大进度
     * @return 当前进度
     */
    public static int getNowProgress(int max) {
        try {
            String[] classItemArray = TIME_LIST.get(getWhichClassNow()).split("-");
            String start = classItemArray[0];
            String end = classItemArray[1];
            long startTime = DF.parse(start).getTime();
            long endTime = DF.parse(end).getTime();
            long nowTime = DF.parse(DF.format(new Date())).getTime();
            long totalTime = endTime - startTime;
            if (nowTime <= startTime) {
                return 0;
            } else if (nowTime >= endTime) {
                return max;
            } else {
                double l = (nowTime - startTime) / (double) totalTime;
                return (int) (l * max);
            }
        } catch (ParseException e) {
            Log.e(TAG, "get progress parse exception ", e);
            return 0;
        }
    }

    /**
     * 获取哪节课正在上,或者要上
     *
     * @return 第几节课, 没有返回-1,注意返回从0开始
     */
    public static int getWhichClassNow() {
        int i = 0;
        String endTimeStr = null;
        for (String s : TIME_LIST) {
            String[] timeArray = s.split("-");
            if (isInDateInterval(timeArray[0], timeArray[1])) {
                return i;
            }
            if (i != 0 && isInDateInterval(endTimeStr, timeArray[0])) {
                return i;
            }
            endTimeStr = timeArray[1];
            i++;
        }

        return -1;
    }

    /**
     * 获取时间集合
     *
     * @return 集合
     */
    public static List<String> getTimeList() {
        return TIME_LIST;
    }

    /**
     * 获取星期
     *
     * @return 1~7
     */
    public static int getWeek() {
        CAL.setTime(new Date());
        int i = CAL.get(Calendar.DAY_OF_WEEK);
        if (i == Calendar.SUNDAY) {
            //星期日
            return 7;
        } else {
            return CAL.get(Calendar.DAY_OF_WEEK) - 1;
        }

    }

    /**
     * 检查当前时间是否在给定的开始结束时间内
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 在返回true
     */
    private static boolean isInDateInterval(String start, String end) {
        try {
            return belongCalendar(DF.parse(DF.format(new Date())), DF.parse(start), DF.parse(end));
        } catch (ParseException e) {
            Log.e(TAG, "parse exception:", e);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime   现在
     * @param beginTime 开始
     * @param endTime   结束
     * @return 是返回True
     */
    private static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() < endTime.getTime();
    }
}
