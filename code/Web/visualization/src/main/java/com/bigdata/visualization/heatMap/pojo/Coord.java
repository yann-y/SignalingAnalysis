package com.bigdata.visualization.heatMap.pojo;

import org.springframework.beans.factory.annotation.Autowired;

public class Coord {

    private String timeStamp;

    private Integer elevation;

    private Double longitude;

    private Double latitude;

    public Coord() {
    }

    public Coord(String timeStamp, Integer elevation, Double longitude, Double latitude) {
        this.timeStamp = timeStamp;
        this.elevation = elevation;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "timeStamp='" + timeStamp + '\'' +
                ", elevation=" + elevation +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
