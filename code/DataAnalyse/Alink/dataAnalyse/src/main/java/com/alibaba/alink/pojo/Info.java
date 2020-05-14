package com.alibaba.alink.pojo;

public class Info {

    public String longitude;

    public String latitude;

    public String timeStamp;

    public Info(){};

    public Info(String longitude, String latitude, String timeStamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeStamp = timeStamp;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "{"+longitude+","+latitude +","+
                 timeStamp + "}";
    }
}
