package com.alibaba.alink.Utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class TimeUtil {
    public static double getHour(String start, String end) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date s = simpleDateFormat.parse(start);
            Date e = simpleDateFormat.parse(end);
            double between = e.getTime() - s.getTime();
            double day = between / (24 * 60* 60 * 1000);
            double hour = day*24;
            return hour;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static double getMinute(String start, String end) {
        return getHour(start,end)*60.0;
    }

    public static double getSecond(String start, String end) {
        return getMinute(start,end)*60.0;
    }
}
