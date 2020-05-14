package com.bigdata.visualization.modeSplit.pojo;



public class AverData {

    private String method;

    private String totalDistance;

    private String totalTime;

    private String averSpeed;

    public AverData(){}

    public AverData(String method, String totalDistance, String totalTime, String averSpeed) {
        this.method = method;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.averSpeed = averSpeed;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getAverSpeed() {
        return averSpeed;
    }

    public void setAverSpeed(String averSpeed) {
        this.averSpeed = averSpeed;
    }

    @Override
    public String toString() {
        return "AverData{" +
                "method='" + method + '\'' +
                ", totalDistance='" + totalDistance + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", averSpeed='" + averSpeed + '\'' +
                '}';
    }
}
