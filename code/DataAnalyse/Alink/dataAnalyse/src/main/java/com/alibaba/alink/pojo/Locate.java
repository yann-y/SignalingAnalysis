package com.alibaba.alink.pojo;

public class Locate {

    private String latitude;

    private String longitude;

    private String mode;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Locate{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
