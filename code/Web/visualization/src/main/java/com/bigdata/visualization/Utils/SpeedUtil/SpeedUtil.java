package com.bigdata.visualization.Utils.SpeedUtil;

public class SpeedUtil {

    public static double getSpeedBySecond(double time,double distance)
    {
        return distance/time;
    }

    public static double getSpeedByHour(double time,double distance)
    {
        return getSpeedBySecond(time,distance)*3.6;
    }
}
