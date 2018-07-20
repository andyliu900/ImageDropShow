package com.moping.imageshow.util;

public class TimeUtil {

    /**
     * 计算两个时间差，换算成差额天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differentDaysByMillisecond(long startTime, long endTime) {
        int days = (int)((endTime - startTime) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 计算两个时间差，换算成差额小时数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differentHoursByMillisecond(long startTime, long endTime) {
        int hours = (int)((endTime - startTime) / (1000 * 3600));
        return hours;
    }

}
